package KMA.BeBookingApp.domain.homestay.service.impl;

import KMA.BeBookingApp.domain.common.dto.PageResponse;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayCardType;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.homestay.dto.response.*;
import KMA.BeBookingApp.domain.homestay.entity.*;
import KMA.BeBookingApp.domain.homestay.repository.*;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayDataService;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class HomestayDataServiceImpl implements HomestayDataService {
    HomestayStyleRepository homestayStyleRepository;
    SpaceRepository spaceRepository;
    AmenityRepository amenityRepository;
    HomestayRepository homestayRepository;
    HomestaySpaceRepository homestaySpaceRepository;
    MediaRepository mediaRepository;
    AuthService authService;

    @Override
    public Map<String, List<HomestayCardResponse>> getAllHomestayOfAUser(String status) {
        HomestayStatus homestayStatus = HomestayStatus.valueOf(status);

        User user = authService.validateAndGetUserByAuthenticationName();
        List<Homestay> homestays = homestayRepository.getAllHomestayByHostId(
                user.getId() ,
                homestayStatus == HomestayStatus.DEFAULT ? null : homestayStatus
        );

        List<Homestay> homestaysInProcess = homestays.stream()
                .filter(homestay -> homestay.getHomestayStatus().equals(HomestayStatus.CREATING))
                .collect(Collectors.toList());

        List<Homestay> homestaysCreated = homestays.stream()
                .filter(homestay -> !homestay.getHomestayStatus().equals(HomestayStatus.CREATING))
                .collect(Collectors.toList());

        Function<Homestay, HomestayCardResponse> function = buildHomestayCardResponse();


        List<HomestayCardResponse> inProcess = homestaysInProcess.stream()
                .map(function)
                .collect(Collectors.toList());

        List<HomestayCardResponse> created = homestaysCreated.stream()
                .map(function)
                .collect(Collectors.toList());


        Map<String, List<HomestayCardResponse>> response = new HashMap<>();
        response.put(HomestayCardType.IN_PROCESS.getName(), inProcess);
        response.put(HomestayCardType.CREATED.getName(), created);

        return response;
    }


    @Override
    public Map<String, List<HomestayStyleResponse>> getAllHomestayStyle(Long homestayId) {
        List<HomestayStyle> homestayStyles = homestayStyleRepository.getAll();

        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay , HomestayStepStatus.STYLE);

        Long idSelected = homestay.getHomestayStyle() != null ? homestay.getHomestayStyle().getId() : 0L;

        Map<String, List<HomestayStyleResponse>> groupedHomestayStyles = homestayStyles.stream()
                .map(homestayStyle -> HomestayStyleResponse.builder()
                        .id(homestayStyle.getId())
                        .name(homestayStyle.getName())
                        .description(homestayStyle.getDescription())
                        .icon(homestayStyle.getIcon())
                        .homestayStyleType(homestayStyle.getHomestayStyleType().getTypeName())
                        .isSelected(homestayStyle.getId().equals(idSelected))
                        .build())
                .collect(Collectors.groupingBy(HomestayStyleResponse::getHomestayStyleType));

        return groupedHomestayStyles;
    }


    @Override
    public Map<String, List<SpaceResponse>> getAllSpace(Long homestayId) {

        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay , HomestayStepStatus.SPACE);

        List<SpaceResponse> amenityResponses = spaceRepository.getAllCus(homestayId);
        Map<String, List<SpaceResponse>> groupedSpaces = amenityResponses.stream()
                .collect(Collectors.groupingBy(SpaceResponse::getSpaceType));
        return groupedSpaces;

    }


    @Override
    public Map<String, List<AmenityResponse>> getAllAmenity(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay , HomestayStepStatus.AMENITY);
        List<AmenityResponse> amenityResponses = amenityRepository.getAllCus(homestayId);
        Map<String, List<AmenityResponse>> groupedAmenities = amenityResponses.stream()
                .collect(Collectors.groupingBy(AmenityResponse::getAmenityType));
        return groupedAmenities;

    }

    @Override
    public Map<String, List<HomestayMediaResponse>> getAllMedia(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay , HomestayStepStatus.MEDIA);
        List<HomestayMediaResponse> homestayMediaResponses =  mediaRepository.getAllMediaOfHomestayCus(homestayId);
        return   homestayMediaResponses.stream()
                .collect(Collectors.groupingBy(HomestayMediaResponse::checkPrimary));
    }

    @Override
    public HomestayLocationResponse getHomestayLocation(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay , HomestayStepStatus.LOCATION);
        return  homestayRepository.getHomestayLocation(homestayId);
    }

    @Override
    public HomestayInformationResponse getHomestayInformation(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay , HomestayStepStatus.INFORMATION);
        return homestayRepository.getHomestayInformation(homestayId);
    }

    @Override
    public HomestayConfirmationResponse getHomestayConfirmation(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        System.out.println(homestay.getHomestayStatus());
        validateStepHomestay(homestay , HomestayStepStatus.CONFIRMED);
        if (!homestay.getHomestayStatus().equals(HomestayStatus.CREATING)) {
            throw new AppException(AppErrorCode.INVALID_HOMESTAY_STEP);
        }

        HomestayConfirmationResponse response = homestayRepository.getHomestayConfirmation(homestayId);
        HomestayStyleResponse homestayStyleResponse = homestayStyleRepository.getHomestayStyleResponseByHomestayId(homestayId);


        List<SpaceResponse> spaceResponses = spaceRepository.getAllSpaceOfHomestay(homestayId);
        List<AmenityResponse> amenityResponses = amenityRepository.getAllAmenityOfHomestay(homestayId);

        response.setHomestayStyle(homestayStyleResponse);
        response.setAmenities(amenityResponses);
        response.setSpaces(spaceResponses);
        return response;
    }


    //helper funtion
    private Homestay validateOwnerHomestayAndGet(Long homestayId){
        User user = authService.validateAndGetUserByAuthenticationName();
        Homestay homestay = homestayRepository.findByIdCus(homestayId)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        if (!homestay.getHostId().equals(user.getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
        return homestay;
    }
    Function<Homestay, HomestayCardResponse> buildHomestayCardResponse(){
        return homestay -> {
            Media primaryMedia = mediaRepository.findPrimaryMediaOfHomestay(homestay.getId()).orElse(null);
            String primaryImage = (primaryMedia != null) ? primaryMedia.getUrl() :
                    "https://tse4.mm.bing.net/th?id=OIP.nEUTJHCsxG7SGctqFqS_OQHaHa&pid=Api&P=0&h=220";


            String name = homestay.getName() != null
                    ? homestay.getName()
                    : "Homestay được cập nhật lúc " + homestay.getUpdatedAt()
                    .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));


            String description = homestay.getHomestayStatus().equals(HomestayStatus.CREATING) ?
                    HomestayCardType.IN_PROCESS.getDescription() : HomestayCardType.CREATED.getDescription();


            HomestayStepStatus next = homestay.getHomestayStepStatus().getNextStatus().orElse(null);
            String typeRedirect = HomestayStatus.CREATING.equals(homestay.getHomestayStatus())
                    ? next == null ? "DETAIL" : homestay.getHomestayStepStatus().getNextStatus().get().toString()
                    : "DETAIL";

            return HomestayCardResponse.builder()
                    .id(homestay.getId())
                    .primaryImage(primaryImage)
                    .name(name)
                    .updateAd(homestay.getUpdatedAt())
                    .descriptionProcess(description)
                    .typeRedirect(typeRedirect)
                    .build();
        };
    }

    private void validateStepHomestay(Homestay homestay, HomestayStepStatus targetStep) {
        HomestayStepStatus currentStep = homestay.getHomestayStepStatus();
        if (targetStep.ordinal()  > currentStep.ordinal() + 1) {
            throw new AppException(AppErrorCode.INVALID_HOMESTAY_STEP);
        }
    }


}
