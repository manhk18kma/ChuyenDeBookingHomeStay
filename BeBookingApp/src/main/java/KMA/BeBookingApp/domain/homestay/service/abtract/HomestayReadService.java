package KMA.BeBookingApp.domain.homestay.service.abtract;

import KMA.BeBookingApp.domain.homestay.dto.request.SearchHomestayRequest;
import KMA.BeBookingApp.domain.homestay.dto.response.*;


import java.util.List;
import java.util.Map;

public interface HomestayReadService {

    HomestayDetailOwnerResponse getHomestayDetailOwner(Long homestayId);

    SearchFilterResponse getAvailableFilters();

    List<HomestayResponse> searchHomestays(SearchHomestayRequest request);
}
