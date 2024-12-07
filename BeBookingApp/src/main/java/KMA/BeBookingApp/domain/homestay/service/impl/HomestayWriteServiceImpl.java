package KMA.BeBookingApp.domain.homestay.service.impl;
import KMA.BeBookingApp.domain.booking.service.abtract.HomestayAvailabilityService;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.MediaType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.common.service.UploadService;
import KMA.BeBookingApp.domain.homestay.dto.request.*;
import KMA.BeBookingApp.domain.homestay.dto.response.ChangeProcessHomestayResponse;
import KMA.BeBookingApp.domain.homestay.entity.*;
import KMA.BeBookingApp.domain.homestay.repository.*;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayWriteService;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class HomestayWriteServiceImpl implements HomestayWriteService {
    HomestayRepository homestayRepository;
    AuthService authService;
    HomestayStyleRepository homestayStyleRepository;
    HomestaySpaceRepository homestaySpaceRepository;
    SpaceRepository spaceRepository;
    AmenityRepository amenityRepository;
    HomestayAmenityRepository homestayAmenityRepository;
    UploadService uploadService;
    MediaRepository mediaRepository;
    HomestayAvailabilityService homestayAvailabilityService;




    @Override
    public ChangeProcessHomestayResponse createHomestay(CreateHomestayRequest request) {
        User user = authService.validateAndGetUserByAuthenticationName();
        Homestay homestay = Homestay.builder()
                .hostId(user.getId())
                .homestayStatus(HomestayStatus.CREATING)
                .homestayStepStatus(HomestayStepStatus.CREATED)
                .build();
        Homestay homestaySaved = homestayRepository.save(homestay);
        log.info("Homestay với ID {} đã được tạo thành công.", homestaySaved.getId());
        return buildResponse(homestay , HomestayStepStatus.CREATED);
    }

    @Override
    public ChangeProcessHomestayResponse updateHomestayStyle(Long homestayId, UpdateHomestayStyleRequest request) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);

        validateStepHomestay(homestay, HomestayStepStatus.STYLE);
        setHomestayStepIfItIsPeak(homestay , HomestayStepStatus.STYLE);

        HomestayStyle homestayStyle = homestayStyleRepository.findById(request.getHomestayStyleId())
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        homestay.setHomestayStyle(homestayStyle);
        Homestay homestaySaved = homestayRepository.save(homestay);

        log.info("Homestay với ID {} đã được cập nhật style thành công.", homestayId);
        return buildResponse(homestay , HomestayStepStatus.STYLE);

    }

    @Override
    public ChangeProcessHomestayResponse updateHomestaySpace(Long homestayId, UpdateHomestaySpaceRequest request) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);

        validateStepHomestay(homestay, HomestayStepStatus.SPACE);
        setHomestayStepIfItIsPeak(homestay , HomestayStepStatus.SPACE);

        List<Long> requestedIds = request.getSpaceIds();

        //validate id request
        List<Space> spaces = spaceRepository.findAllById(requestedIds);
        if (spaces.size() != requestedIds.size()) {
            throw new AppException(AppErrorCode.RESOURCE_NOT_FOUND);
        }
        List<Long> currentSpaceId = homestaySpaceRepository.getCurrentSpaceOfHomestay(homestayId);

        List<Long> removedSpaceId = new ArrayList<>(currentSpaceId);
        removedSpaceId.removeAll(requestedIds);

        List<Long> addedSpaceId = new ArrayList<>(requestedIds);
        addedSpaceId.removeAll(currentSpaceId);


        if (!removedSpaceId.isEmpty()) {
            homestaySpaceRepository.removeSpacesFromHomestay(homestayId, removedSpaceId);
        }

        if (!addedSpaceId.isEmpty()) {
            List<Space> addedSpaces = spaceRepository.findAllById(addedSpaceId);
            List<HomestaySpace> homestaySpaces = addedSpaces.stream()
                    .map(space -> HomestaySpace.builder()
                            .space(space)
                            .homestay(homestay)
                            .build())
                    .collect(Collectors.toList());
            homestaySpaceRepository.saveAll(homestaySpaces);
        }



        return buildResponse(homestay , HomestayStepStatus.SPACE);


    }


    @Override
    public ChangeProcessHomestayResponse updateHomestayAmenity(Long homestayId, UpdateHomestayAmenityRequest request) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);

        validateStepHomestay(homestay, HomestayStepStatus.AMENITY);
        setHomestayStepIfItIsPeak(homestay , HomestayStepStatus.AMENITY);

        List<Long> requestedIds = request.getAmenityIds();
        List<Amenity> amenities = amenityRepository.findAllById(requestedIds);
        if (amenities.size() != requestedIds.size()) {
            throw new AppException(AppErrorCode.RESOURCE_NOT_FOUND);
        }

        List<Long> currentAmenityId = homestayAmenityRepository.getCurrentAmenityOfHomestay(homestayId);

        List<Long> removedAmenityId = new ArrayList<>(currentAmenityId);
        removedAmenityId.removeAll(requestedIds);

        List<Long> addedAmenityId = new ArrayList<>(requestedIds);
        addedAmenityId.removeAll(currentAmenityId);

        if (!removedAmenityId.isEmpty()) {
            homestayAmenityRepository.removeAmenitiesFromHomestay(homestayId, removedAmenityId);
        }

        // Xử lý tiện nghi mới
        if (!addedAmenityId.isEmpty()) {
            List<Amenity> addedAmenities = amenityRepository.findAllById(addedAmenityId);
            List<HomestayAmenity> homestayAmenitiesToAdd = addedAmenities.stream()
                    .map(amenity -> HomestayAmenity.builder()
                            .amenity(amenity)
                            .homestay(homestay)
                            .build())
                    .collect(Collectors.toList());
            homestayAmenityRepository.saveAll(homestayAmenitiesToAdd);
        }

        Homestay homestaySaved = homestayRepository.save(homestay);

        log.info("Homestay với ID {} đã được cập nhật tiện nghi thành công.", homestayId);
        return buildResponse(homestay , HomestayStepStatus.AMENITY);

    }



    @Override
    public ChangeProcessHomestayResponse updateHomestayMedia(Long homestayId,
                                                             UpdateHomestayImageRequest request,
                                                             MultipartFile primaryFile,
                                                             List<MultipartFile> relatedFiles) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay, HomestayStepStatus.MEDIA);
        setHomestayStepIfItIsPeak(homestay, HomestayStepStatus.MEDIA);

        List<MultipartFile> relatedFileList = Optional.ofNullable(relatedFiles).orElse(Collections.emptyList());
        validateMultipartFileImgOrVideo(relatedFileList);

        List<Media> medias = new ArrayList<>();
        List<String> uploadedImageUrls = new ArrayList<>();
        List<String> removedUrls = new ArrayList<>();
        String primaryFileUrl = null;


        Media currentPrimaryMedia = mediaRepository.findPrimaryMediaOfHomestay(homestayId).orElse(null);

        if (primaryFile != null && !primaryFile.isEmpty()) {
            validateFileType(primaryFile, "image");

            if (currentPrimaryMedia != null) {
                mediaRepository.delete(currentPrimaryMedia);
                removedUrls.add(currentPrimaryMedia.getUrl());
            }

            try {
                Media media = createMedia(primaryFile, request.getContent(), homestay, true);
                primaryFileUrl = media.getUrl();
                medias.add(media);
            } catch (Exception e) {
                throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
            }
        } else if (currentPrimaryMedia == null) {
            throw new AppException(AppErrorCode.PRIMARY_MEDIA_MISSING);
        }

        List<Long> removedMediaIds = Optional.ofNullable(request.getMediaRemovedIds()).orElse(Collections.emptyList());
        if (!removedMediaIds.isEmpty()) {
            List<Media> mediaToRemove = mediaRepository.findAllByIdAndHomestayId(removedMediaIds, homestayId);
            if (mediaToRemove.size() != removedMediaIds.size()) {
                if(primaryFileUrl != null){
                    uploadService.deleteCloud(primaryFileUrl);
                }
                throw new AppException(AppErrorCode.UNAUTHORIZED);
            }
            mediaToRemove.forEach(media -> {
                removedUrls.add(media.getUrl());
            });
            mediaRepository.removeInRemovedIds(homestayId, removedMediaIds);
        }

        long currentRelatedMediaCount = mediaRepository.countRelatedMediaOfHomestay(homestayId);
        if (currentRelatedMediaCount + relatedFileList.size() < 2) {
            throw new AppException(AppErrorCode.INVALID_RELATED_MEDIA_COUNT);
        }

            for (MultipartFile file : relatedFileList) {
                try {
                    Media relatedMedia = createMedia(file ,request.getContent(), homestay, false );
                    String relatedImageUrl = relatedMedia.getUrl();
                    uploadedImageUrls.add(relatedImageUrl);
                    medias.add(relatedMedia);
                } catch (Exception e) {
                    rollbackUploadedImages(uploadedImageUrls); //xoá danh sách những cái đã up trên clould
                    throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
                }
            }

            // Lưu các media vào DB
            mediaRepository.saveAll(medias);
            Homestay homestaySaved = homestayRepository.save(homestay);
            rollbackUploadedImages(removedUrls); // tất cả các query đã thành công => thực sự xoá trên cloud
        log.info("Homestay với ID {} đã được cập nhật phương tiện thành công.", homestayId);
        return buildResponse(homestay , HomestayStepStatus.MEDIA);


    }

    private void rollbackUploadedImages(List<String> uploadedImageUrls) {
        Optional.ofNullable(uploadedImageUrls)
                .ifPresent(urls -> urls.forEach(imageUrl -> {
                    try {
                        uploadService.deleteCloud(imageUrl);
                    } catch (Exception e) {
                        log.error("Không thể xóa ảnh trên Cloudinary: {}", e.getMessage());
                    }
                }));
    }


    private void validateMultipartFileImgOrVideo(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return;
        }
        multipartFiles.forEach(file -> {
            String contentType = file.getContentType();
            if (contentType == null || !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
                throw new AppException(AppErrorCode.INVALID_FILE_CONTENT);
            }
        });
    }

    private void validateFileType(MultipartFile file, String expectedType) {
        if (file == null || file.isEmpty()) {
            throw new AppException(AppErrorCode.INVALID_FILE_CONTENT);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith(expectedType)) {
            throw new AppException(AppErrorCode.INVALID_FILE_CONTENT);
        }
    }


    private Media createMedia(MultipartFile file, String content, Homestay homestay, boolean isPrimary) throws IOException {
        String url = uploadService.uploadFile(file);
        MediaType mediaType = (file.getContentType().startsWith("image/")) ? MediaType.IMAGE : MediaType.VIDEO;
        return Media.builder()
                .mediaType(mediaType)
                .url(url)
                .description(content)
                .homestay(homestay)
                .isPrimary(isPrimary)
                .build();
    }



//    @Override
//    public ChangeProcessHomestayResponse updateHomestayLocation(Long homestayId, UpdateHomestayLocationRequest request) {
//        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
//        validateStepHomestay(homestay, HomestayStepStatus.LOCATION);
//        setHomestayStepIfItIsPeak(homestay, HomestayStepStatus.LOCATION);
//
//        homestay.setLatitude(request.getLatitude());
//        homestay.setLongitude(request.getLongitude());
//        homestay.setAddress(request.getAddress());
//        homestay.setAddressDetail(request.getAddressDetail());
//
//        Point point = new GeometryFactory().createPoint(new org.locationtech.jts.geom.Coordinate(request.getLongitude(), request.getLatitude()));
//        homestay.setGeom(point);
//
//        Homestay homestaySaved = homestayRepository.save(homestay);
//
//        log.info("Homestay với ID {} đã được cập nhật vị trí thành công.", homestayId);
//
//        // Trả về phản hồi sau khi cập nhật thành công
//        return buildResponse(homestay , HomestayStepStatus.LOCATION);
//
//    }

    @Override
    @Transactional
    public ChangeProcessHomestayResponse updateHomestayLocation(Long homestayId, UpdateHomestayLocationRequest request) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay, HomestayStepStatus.LOCATION);
        setHomestayStepIfItIsPeak(homestay, HomestayStepStatus.LOCATION);

        homestay.setLatitude(request.getLatitude());
        homestay.setLongitude(request.getLongitude());
        homestay.setAddress(request.getAddress());
        homestay.setAddressDetail(request.getAddressDetail());

//        // Cập nhật Point với SRID 3857
//        GeometryFactory geometryFactory = new GeometryFactory();
//        Coordinate coordinate = new Coordinate(request.getLongitude(), request.getLatitude());
//        Point point = geometryFactory.createPoint(coordinate);
//        point.setSRID(3857);  // Đặt SRID là 3857
//        homestay.setGeom(point);

        Homestay homestaySaved = homestayRepository.save(homestay);
        homestayRepository.saveLocation(homestayId , request.getLongitude() , request.getLatitude());

        log.info("Homestay với ID {} đã được cập nhật vị trí thành công.", homestayId);

        // Trả về phản hồi sau khi cập nhật thành công
        return buildResponse(homestay , HomestayStepStatus.LOCATION);
    }



    @Override
    public ChangeProcessHomestayResponse updateHomestayInformation(Long homestayId, UpdateHomestayInformationRequest request) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);

        validateStepHomestay(homestay, HomestayStepStatus.INFORMATION);
        setHomestayStepIfItIsPeak(homestay , HomestayStepStatus.INFORMATION);

        homestay.setName(request.getName());
        homestay.setDescription(request.getDescription());
        homestay.setMaxGuests(request.getMaxGuests());

        Homestay homestaySaved = homestayRepository.save(homestay);
        log.info("Homestay với ID {} đã được cập nhật thông tin thành công.", homestayId);

        return buildResponse(homestay , HomestayStepStatus.INFORMATION);

    }

    @Override
    public ChangeProcessHomestayResponse confirmHomestayProcess(Long homestayId , ConfirmHomestayProcessRequest request) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        if (!homestay.getHomestayStatus().equals(HomestayStatus.CREATING)) {
            throw new AppException(AppErrorCode.INVALID_HOMESTAY_STEP);
        }
        setHomestayStepIfItIsPeak(homestay , HomestayStepStatus.CONFIRMED);


        homestay.setHomestayStatus(HomestayStatus.AVAILABLE);
        Homestay homestaySaved = homestayRepository.save(homestay);

        homestayAvailabilityService.generateAvailabilityForNextYear(homestayId , request.getDefaultPricePerNight());
        log.info("Homestay với ID {} và tên '{}' đã được cập nhật thành công, chuyển sang bước CONFIRMATION.",
                homestaySaved.getId(), homestaySaved.getName());

        // Trả về kết quả quy trình
        return buildResponse(homestay , HomestayStepStatus.CONFIRMED);

    }

    @Override
    public void deleteHomestay(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        homestay.setHomestayStatus(HomestayStatus.REMOVED);
        homestayRepository.save(homestay);
    }


    //helper function





    private Homestay validateOwnerHomestayAndGet(Long homestayId){
        User user = authService.validateAndGetUserByAuthenticationName();
        Homestay homestay = homestayRepository.findByIdCus(homestayId)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        if (!homestay.getHostId().equals(user.getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
        return homestay;
    }

    private void validateStepHomestay(Homestay homestay, HomestayStepStatus targetStep) {
        HomestayStepStatus currentStep = homestay.getHomestayStepStatus();
        if (targetStep.ordinal() > currentStep.ordinal() + 1) {
            throw new AppException(AppErrorCode.INVALID_HOMESTAY_STEP);
        }
    }

    private void setHomestayStepIfItIsPeak(Homestay homestay, HomestayStepStatus targetStep) {
        if (targetStep.ordinal() > homestay.getHomestayStepStatus().ordinal()) {
            homestay.setHomestayStepStatus(targetStep);
        }
    }

    private ChangeProcessHomestayResponse buildResponse(Homestay homestay,HomestayStepStatus functionStepStatus ){
        HomestayStepStatus nextStatus = functionStepStatus.getNextStatus().orElse(null);
        String nextStatusString = nextStatus == null ? "DETAIL" : nextStatus.toString();
        return ChangeProcessHomestayResponse.builder()
                .homestayId(homestay.getId())
                .currentStep(functionStepStatus.toString())
                .nextStep(nextStatusString)
                .peakStep(homestay.getHomestayStepStatus().toString())
                .build();
    }


}
