package KMA.BeBookingApp.domain.user.service.abtract;

import KMA.BeBookingApp.domain.user.dto.request.*;
import KMA.BeBookingApp.domain.user.dto.response.FailedAttemptsResponse;
import KMA.BeBookingApp.domain.user.dto.response.PrivateUserInfoResponse;
import KMA.BeBookingApp.domain.user.dto.response.TokenResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    TokenResponse outboundAuthenticate (String code , OutBoundLoginRequest request);

    void initUsername(InitUsernameRequest request);

    FailedAttemptsResponse getFailedAttempts(String usernameOrEmail);

    PrivateUserInfoResponse getPrivateInfo();

    void updatePrivateInfo(ChangePrivateInfoRequest request);

    void updateAvatar(MultipartFile avatarFile);

}
