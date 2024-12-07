package KMA.BeBookingApp.domain.user.service.abtract;

import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.user.dto.request.IntrospectRequest;
import KMA.BeBookingApp.domain.user.dto.response.IntrospectResponse;
import KMA.BeBookingApp.domain.user.entity.User;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public interface TokenService {
    IntrospectResponse introspectToken(IntrospectRequest request);

    String generateToken(User user , TokenType tokenType);

    JWTClaimsSet extractClaims(String token, TokenType tokenType);

    SignedJWT verifyToken(String token, TokenType tokenType);
}
