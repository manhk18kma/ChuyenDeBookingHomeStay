package KMA.BeBookingApp.domain.user.service.impl;

import KMA.BeBookingApp.domain.common.enumType.user.Gender;
import KMA.BeBookingApp.domain.common.enumType.user.Platform;
import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.common.service.UploadService;
import KMA.BeBookingApp.domain.user.dto.request.ChangePrivateInfoRequest;
import KMA.BeBookingApp.domain.user.dto.request.InitUsernameRequest;
import KMA.BeBookingApp.domain.user.dto.request.OutBoundLoginRequest;
import KMA.BeBookingApp.domain.user.dto.response.FailedAttemptsResponse;
import KMA.BeBookingApp.domain.user.dto.response.OutboundUserResponse;
import KMA.BeBookingApp.domain.user.dto.response.PrivateUserInfoResponse;
import KMA.BeBookingApp.domain.user.dto.response.TokenResponse;
import KMA.BeBookingApp.domain.user.entity.Role;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.entity.UserHasRole;
import KMA.BeBookingApp.domain.user.repository.RoleRepository;
import KMA.BeBookingApp.domain.user.repository.UserHasRoleRepository;
import KMA.BeBookingApp.domain.user.repository.UserRepository;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import KMA.BeBookingApp.domain.user.service.abtract.TokenService;
import KMA.BeBookingApp.domain.user.service.abtract.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class UserServiceImpl implements UserService {


    AuthService authService;
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserHasRoleRepository userHasRoleRepository;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;
    UploadService uploadService;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_PART_LENGTH = 6;

    private static final int MAX_FAILED_ATTEMPTS = 5;


    @Transactional
    @Override
    public TokenResponse outboundAuthenticate(String code  , OutBoundLoginRequest request) {
        Platform platform = Platform.valueOf(request.getPlatform());

        OutboundUserResponse userInfo;
        switch (platform) {
            case IOS -> {
                userInfo = authService.verifyOauth2GoogleIos(request.getIdToken());
            }
            case WEB -> {
                userInfo = authService.verifyOauth2GoogleWeb(code);

            }
            case ANDROID -> {
                userInfo = authService.verifyOauth2GoogleAndroid(request.getIdToken());

            }
            default -> {
                throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
            }
        }
        log.info(userInfo.toString());


        User user = onboardUserIfNotExist(userInfo , request);

        String accessToken = tokenService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenService.generateToken(user, TokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    @Override
    public void initUsername(InitUsernameRequest request) {
        String email = authService.getAuthenticationName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
        if (user.getIsSetUsername()) {
            throw new AppException(AppErrorCode.USERNAME_IS_SET);
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(AppErrorCode.USERNAME_IS_USED);
        }
        user.setUsername(request.getUsername());
        user.setIsSetUsername(true);
        userRepository.save(user);

        log.info("Username and password initialized for user: {}", email);
    }

    @Override
    public FailedAttemptsResponse getFailedAttempts(String usernameOrEmail) {
        return userRepository.getFailedAttempts(usernameOrEmail)
                .map(failedAttempts -> new FailedAttemptsResponse(MAX_FAILED_ATTEMPTS - failedAttempts))
                .orElseGet(() -> new FailedAttemptsResponse(MAX_FAILED_ATTEMPTS));
    }


    @Override
    public PrivateUserInfoResponse getPrivateInfo() {
        User user = authService.validateAndGetUserByAuthenticationName();
        PrivateUserInfoResponse response = new PrivateUserInfoResponse();
        BeanUtils.copyProperties(user , response);
        return response;
    }

    @Override
    public void updatePrivateInfo(ChangePrivateInfoRequest request) {
        User user = authService.validateAndGetUserByAuthenticationName();
        Gender gender = Gender.valueOf(request.getGender().toUpperCase());
        LocalDateTime dateOfBirth = LocalDateTime.parse(request.getDateOfBirth() + "T00:00:00");
        user.setFullName(request.getFullName());
        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);
        user.setPhone(request.getPhone());


        if (!request.getUsername().equals(user.getUsername())) {
            if (user.getIsSetUsername()) {
                throw new AppException(AppErrorCode.USERNAME_IS_SET);
            }
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new AppException(AppErrorCode.USERNAME_IS_USED);
            }
            user.setUsername(request.getUsername());
        }

        userRepository.save(user);
    }

    @Override
    public void updateAvatar(MultipartFile avatarFile) {
        validateFileType(avatarFile);

        User user = authService.validateAndGetUserByAuthenticationName();

        String newUrl = null;
        String oldUrl = user.getUrlAvt();

        try {
            newUrl = uploadService.uploadFile(avatarFile);
        } catch (Exception e) {
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        user.setUrlAvt(newUrl);
        userRepository.save(user);

        if (oldUrl != null) {
            uploadService.deleteCloud(oldUrl);
        }
    }


    private void validateFileType(MultipartFile file) {
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

        // Kiểm tra xem phần mở rộng có hợp lệ không (jpg, png, jpeg)
        if (!fileExtension.equals("jpg") && !fileExtension.equals("png") && !fileExtension.equals("jpeg")) {
            throw new AppException(AppErrorCode.INVALID_FILE_CONTENT);
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new AppException(AppErrorCode.INVALID_FILE_CONTENT);
        }
    }



    //help function

    public User onboardUserIfNotExist(OutboundUserResponse userInfo , OutBoundLoginRequest request ) {
        User user =  userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            String randomUsername = generateUniqueRandomUsername();
            User newUser = User.builder()
                    .username(randomUsername)
                    .isSetUsername(false)
                    .isSetPassword(false)
                    .failedAttempts(0)
                    .firstName(userInfo.getGivenName())
                    .lastName(userInfo.getFamilyName())
                    .email(userInfo.getEmail())
                    .fullName(userInfo.getName())
                    .urlAvt(userInfo.getPicture())
                    .locale(userInfo.getLocale())
                    .password(null)
                    .tokenVersion(UUID.randomUUID().toString())
                    .tokenDevice(request.getTokenDevice())
                    .build();

            newUser = userRepository.save(newUser);
            log.info("New user created: {}", newUser);

            Role roleUser = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new IllegalStateException("Role USER not found"));

            UserHasRole userHasRole = UserHasRole.builder()
                    .user(newUser)
                    .role(roleUser)
                    .build();

            userHasRoleRepository.save(userHasRole);
            return newUser;
        });
        user.setTokenDevice(request.getTokenDevice());
        return  userRepository.save(user);
    }

    private String generateUniqueRandomUsername() {
        String randomUsername;
        do {
            randomUsername = generateRandomString();
        } while (userRepository.findByUsername(randomUsername).isPresent());
        return randomUsername;
    }

    private String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(RANDOM_PART_LENGTH);

        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

}
