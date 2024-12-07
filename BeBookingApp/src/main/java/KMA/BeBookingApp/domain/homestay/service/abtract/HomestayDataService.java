package KMA.BeBookingApp.domain.homestay.service.abtract;

import KMA.BeBookingApp.domain.common.dto.PageResponse;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayCardType;
import KMA.BeBookingApp.domain.homestay.dto.response.*;

import java.util.List;
import java.util.Map;

public interface HomestayDataService {
    Map<String, List<HomestayCardResponse>> getAllHomestayOfAUser(String status);

    Map<String, List<HomestayStyleResponse>>  getAllHomestayStyle(Long homestayId);

    Map<String, List<SpaceResponse>> getAllSpace(Long homestayId);

    Map<String , List<AmenityResponse>> getAllAmenity(Long homestayId);

    Map<String, List<HomestayMediaResponse>> getAllMedia(Long homestayId);

    HomestayLocationResponse getHomestayLocation(Long homestayId);

    HomestayInformationResponse getHomestayInformation(Long homestayId);

    HomestayConfirmationResponse getHomestayConfirmation(Long homestayId);
}
