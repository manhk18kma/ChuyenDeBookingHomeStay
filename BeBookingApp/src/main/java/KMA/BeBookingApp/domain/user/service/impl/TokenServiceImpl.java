package KMA.BeBookingApp.domain.user.service.impl;

import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.user.dto.request.IntrospectRequest;
import KMA.BeBookingApp.domain.user.dto.response.IntrospectResponse;
import KMA.BeBookingApp.domain.user.entity.Role;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.repository.InvalidatedTokenRepository;
import KMA.BeBookingApp.domain.user.repository.RoleRepository;
import KMA.BeBookingApp.domain.user.repository.UserRepository;
import KMA.BeBookingApp.domain.user.service.abtract.TokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenServiceImpl implements TokenService {
    RoleRepository roleRepository;
    @NonFinal
    @Value("${jwt.valid-duration}")
    long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    long refreshableDuration;

    @NonFinal
    @Value("${jwt.reset-password-duration}")
    long resetPasswordDuration;

    @NonFinal
    @Value("${jwt.accessKey}")
    String accessKey;

    @NonFinal
    @Value("${jwt.refreshKey}")
    String refreshKey;

    @NonFinal
    @Value("${jwt.resetKey}")
    String resetPasswordKey;

    InvalidatedTokenRepository invalidatedTokenRepository;

    UserRepository userRepository;

    @Override
    public IntrospectResponse introspectToken(IntrospectRequest request) {
        return null;
    }

    @Override
    public String generateToken(User user, TokenType tokenType) {
        long validTime = getDuration(tokenType);
        String key = getKey(tokenType);

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDateTime = now.plus(validTime, ChronoUnit.SECONDS);

        String subject = tokenType != TokenType.RESET_TOKEN ? user.getEmail() : "";
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer("KMA_BOOKING_APP")
                .issueTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expirationTime(Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .jwtID(UUID.randomUUID().toString());

        if (tokenType != TokenType.RESET_TOKEN) {
            claimsBuilder.claim("userId", user.getId().toString());
            claimsBuilder.claim("roles", getRoles(user));
            claimsBuilder.claim("tokenVersion", user.getTokenVersion());
        }

        Payload payload = new Payload(claimsBuilder.build().toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
    }

    private List<String> getRoles(User user) {
        List<Role> roles = roleRepository.getRolesByUserId(user.getId());
        return roles.stream()
                .map(role -> "ROLE_" + role.getRoleName())
                .collect(Collectors.toList());
    }

    @Override
    public JWTClaimsSet extractClaims(String token, TokenType tokenType) {
        SignedJWT signedJWT = verifyToken(token, tokenType);
        try {
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public SignedJWT verifyToken(String token, TokenType tokenType) {
        try {
            String key = getKey(tokenType);
            JWSVerifier verifier = new MACVerifier(key.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);
            String sub = signedJWT.getJWTClaimsSet().getSubject();
            String tokenVersion = signedJWT.getJWTClaimsSet().getStringClaim("tokenVersion");

            // Kiểm tra người dùng
            User user = userRepository.validateAndGetUserByAuthenticationName(sub)
                    .orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));

            if (!String.valueOf(user.getTokenVersion()).equals(tokenVersion)) {
                throw new AppException(AppErrorCode.UNAUTHENTICATED);
            }


            LocalDateTime expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            // Kiểm tra chữ ký và hạn token
            if (!signedJWT.verify(verifier) || LocalDateTime.now().isAfter(expiryTime)) {
                throw new AppException(AppErrorCode.UNAUTHENTICATED);
            }

            // Kiểm tra xem token có bị vô hiệu hóa không
            if (invalidatedTokenRepository.existsByJwtId(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new AppException(AppErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;

        } catch (JOSEException | ParseException e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
    }

    private long getDuration(TokenType tokenType) {
        long validTime = 0;
        switch (tokenType) {
            case ACCESS_TOKEN -> validTime = validDuration;
            case REFRESH_TOKEN -> validTime = refreshableDuration;
            case RESET_TOKEN -> validTime = resetPasswordDuration;
            default -> throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return validTime;
    }

    private String getKey(TokenType tokenType) {
        String key = "";
        switch (tokenType) {
            case ACCESS_TOKEN -> key = accessKey;
            case REFRESH_TOKEN -> key = refreshKey;
            case RESET_TOKEN -> key = resetPasswordKey;
            default -> throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return key;
    }
}
