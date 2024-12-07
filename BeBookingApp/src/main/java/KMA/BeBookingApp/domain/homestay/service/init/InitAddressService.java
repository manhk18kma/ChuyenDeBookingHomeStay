package KMA.BeBookingApp.domain.homestay.service.init;

import KMA.BeBookingApp.domain.homestay.entity.District;
import KMA.BeBookingApp.domain.homestay.entity.Province;
import KMA.BeBookingApp.domain.homestay.entity.Ward;
import KMA.BeBookingApp.domain.homestay.repository.DistrictRepository;
import KMA.BeBookingApp.domain.homestay.repository.ProvinceRepository;
import KMA.BeBookingApp.domain.homestay.repository.WardRepository;
import KMA.BeBookingApp.domain.homestay.service.abtract.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InitAddressService
        implements CommandLineRunner {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private ApiClient apiClient;



    @Override
    public void run(String... args) throws Exception {

        long totalProvince = provinceRepository.count();
        long totalDistrict = districtRepository.count();
        long totalWard = wardRepository.count();
        if (totalProvince == 0 && totalWard == 0 && totalDistrict == 0) {
            return;
        } else {
            wardRepository.deleteAll();
            districtRepository.deleteAll();
            provinceRepository.deleteAll();
            ApiResponse<Province> provinceResponse = apiClient.fetchData(
                    "https://esgoo.net/api-tinhthanh/1/0.htm",
                    new ParameterizedTypeReference<ApiResponse<Province>>() {
                    }
            );
            List<Province> provinces = provinceResponse.getData();

            for (Province province : provinces) {
                provinceRepository.save(province);

                ApiResponse<District> districtResponse = apiClient.fetchData(
                        "https://esgoo.net/api-tinhthanh/2/" + province.getId() + ".htm",
                        new ParameterizedTypeReference<ApiResponse<District>>() {
                        }
                );
                List<District> districts = districtResponse.getData();

                for (District district : districts) {
                    district.setProvince(province);
                    districtRepository.save(district);

                    ApiResponse<Ward> wardResponse = apiClient.fetchData(
                            "https://esgoo.net/api-tinhthanh/3/" + district.getId() + ".htm",
                            new ParameterizedTypeReference<ApiResponse<Ward>>() {
                            }
                    );
                    List<Ward> wards = wardResponse.getData();

                    for (Ward ward : wards) {
                        ward.setDistrict(district);
                        wardRepository.save(ward);
                    }
                }
            }
        }
    }
}