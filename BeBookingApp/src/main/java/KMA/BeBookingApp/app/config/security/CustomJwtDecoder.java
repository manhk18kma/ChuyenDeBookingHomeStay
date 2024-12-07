package KMA.BeBookingApp.app.config.security;


import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;

import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;
//
//@Slf4j
//@Component
//public class CustomJwtDecoder implements JwtDecoder {
//    @Value("${jwt.accessKey}")
//    private String signerKey;
//
//    @Autowired
//    private AuthService authenticationService;
//
//    private NimbusJwtDecoder nimbusJwtDecoder = null;
//
//    @Override
//    public Jwt decode(String token) throws JwtException {
//    var response = authenticationService.introspect(token , TokenType.ACCESS_TOKEN);
//        if (!response) {
//            throw new JwtException("Token không hợp lệ hoặc đã hết hạn.");
//
////            throw new AppException(AppErrorCode.UNAUTHENTICATED);
//        }
//
//    if (Objects.isNull(nimbusJwtDecoder)) {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//        nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }
//    return nimbusJwtDecoder.decode(token);
//}
//
//}



@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.accessKey}")
    private String signerKey;

    @Autowired
    private AuthService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        var response = authenticationService.introspect(token , TokenType.ACCESS_TOKEN);

//        if (!response) throw new JwtException("Token invalid!!!!");
        if (!response) {
            throw new BadCredentialsException("Token invalid!!!!");
        }


        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}


