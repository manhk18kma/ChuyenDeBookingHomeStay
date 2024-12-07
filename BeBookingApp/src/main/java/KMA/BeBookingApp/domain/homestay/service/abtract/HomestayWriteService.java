package KMA.BeBookingApp.domain.homestay.service.abtract;

import KMA.BeBookingApp.domain.homestay.dto.request.*;
import KMA.BeBookingApp.domain.homestay.dto.response.ChangeProcessHomestayResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomestayWriteService {
    ChangeProcessHomestayResponse createHomestay(CreateHomestayRequest request);

    ChangeProcessHomestayResponse updateHomestayStyle(Long homestayId , UpdateHomestayStyleRequest request);

    ChangeProcessHomestayResponse updateHomestaySpace(Long homestayId, UpdateHomestaySpaceRequest request);

    ChangeProcessHomestayResponse updateHomestayAmenity(Long homestayId, UpdateHomestayAmenityRequest request);

    ChangeProcessHomestayResponse updateHomestayMedia(Long homestayId,
                                                      UpdateHomestayImageRequest request ,
                                                      MultipartFile primaryFile,
                                                      List<MultipartFile> relatedFiles);

    ChangeProcessHomestayResponse updateHomestayLocation(Long homestayId, UpdateHomestayLocationRequest request);

    ChangeProcessHomestayResponse updateHomestayInformation(Long homestayId, UpdateHomestayInformationRequest request);

    ChangeProcessHomestayResponse confirmHomestayProcess(Long homestayId , ConfirmHomestayProcessRequest request);

    void deleteHomestay(Long homestayId);
}
