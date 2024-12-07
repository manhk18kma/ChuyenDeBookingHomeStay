package KMA.BeBookingApp.domain.homestay.service.init;

import KMA.BeBookingApp.domain.common.enumType.homestay.*;
import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.HomestayStyle;
import KMA.BeBookingApp.domain.homestay.entity.Space;
import KMA.BeBookingApp.domain.homestay.repository.AmenityRepository;
import KMA.BeBookingApp.domain.homestay.repository.HomestayStyleRepository;
import KMA.BeBookingApp.domain.homestay.repository.SpaceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class InitHomestayDomainService implements CommandLineRunner {
    AmenityRepository amenityRepository;
    SpaceRepository spaceRepository;
    HomestayStyleRepository homestayStyleRepository;

    @Override
    public void run(String... args) throws Exception {
        initAmenities();
        initSpaces();
        initHomestayStyles();
    }

    private void initAmenities() {
        int totalAmenities = AmenityName.values().length;
        long existingAmenitiesCount = amenityRepository.count();

        if (existingAmenitiesCount != totalAmenities) {
            log.info("Số lượng amenities trong DB không khớp. Xóa tất cả và khởi tạo lại...");

            amenityRepository.deleteAll();
            List<Amenity> amenities = Arrays.stream(AmenityName.values())
                    .map(amenityName -> Amenity.builder()
                            .amenityName(amenityName)
                            .description(amenityName.getDescription())
                            .name(amenityName.getName())
                            .icon(amenityName.getIcon())
                            .amenityType(amenityName.getAmenityType())
                            .build())
                    .collect(Collectors.toList());

            amenityRepository.saveAll(amenities);

            log.info("Khởi tạo lại amenities hoàn tất. Đã lưu {} amenities.", amenities.size());
        } else {
            log.info("Số lượng amenities trong DB đã khớp.");
        }
    }

    private void initSpaces() {
        int totalSpaces = SpaceName.values().length;
        long existingSpaces = spaceRepository.count();

        if (totalSpaces != existingSpaces) {
            log.info("Số lượng spaces trong DB không khớp. Xóa tất cả và khởi tạo lại...");

            spaceRepository.deleteAll();

            List<Space> spaces = Arrays.stream(SpaceName.values())
                    .map(spaceName -> {
                        String description = spaceName.getDescription();
                        return Space.builder()
                                .spaceName(spaceName)
                                .description(spaceName.getDescription())
                                .name(spaceName.getName())
                                .icon(spaceName.getIcon())
                                .spaceType(spaceName.getSpaceType())
                                .build();
                    })
                    .collect(Collectors.toList());

            spaceRepository.saveAll(spaces);

            log.info("Khởi tạo lại spaces hoàn tất. Đã lưu {} spaces.", spaces.size());
        } else {
            log.info("Số lượng spaces trong DB đã khớp.");
        }
    }

    private void initHomestayStyles() {
        int totalHomestayStyles = HomestayStyleName.values().length;
        long existingHomestayStyles = homestayStyleRepository.count();

        if (totalHomestayStyles != existingHomestayStyles) {
            log.info("Số lượng homestay styles trong DB không khớp. Xóa tất cả và khởi tạo lại...");

            homestayStyleRepository.deleteAll();

            List<HomestayStyle> homestayStyles = Arrays.stream(HomestayStyleName.values())
                    .map(homestayStyleName -> {

                        return HomestayStyle.builder()
                                .homestayStyleName(homestayStyleName)
                                .description(homestayStyleName.getDescription())
                                .name( homestayStyleName.getName())
                                .icon(homestayStyleName.getIcon())
                                .homestayStyleType(homestayStyleName.getHomestayStyleType())
                                .build();
                    })
                    .collect(Collectors.toList());

            homestayStyleRepository.saveAll(homestayStyles);

            log.info("Khởi tạo lại homestay styles hoàn tất. Đã lưu {} homestay styles.", homestayStyles.size());
        } else {
            log.info("Số lượng homestay styles trong DB đã khớp.");
        }
    }

}
