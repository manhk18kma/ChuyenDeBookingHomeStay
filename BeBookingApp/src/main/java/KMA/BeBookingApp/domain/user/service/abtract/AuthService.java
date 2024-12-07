package KMA.BeBookingApp.domain.user.service.abtract;

import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.user.dto.request.*;
import KMA.BeBookingApp.domain.user.dto.response.OutboundUserResponse;
import KMA.BeBookingApp.domain.user.dto.response.TokenResponse;
import KMA.BeBookingApp.domain.user.entity.User;

public interface AuthService {

    OutboundUserResponse verifyOauth2GoogleWeb(String code);

    String getAuthenticationName();


    TokenResponse login(LoginRequest request , boolean isAllowed);


    TokenResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    void initChangePassword(InitChangePasswordRequest request , boolean isAllowedRateLimit);

    TokenResponse changePassword(ChangePasswordRequest changePasswordRequest);

    boolean introspect(String token , TokenType tokenType);

    User validateAndGetUserByAuthenticationName();


    OutboundUserResponse verifyOauth2GoogleIos(String idToken);

    OutboundUserResponse verifyOauth2GoogleAndroid(String idToken);

    boolean introspectCaptcha(IntrospectRequest request);
}
