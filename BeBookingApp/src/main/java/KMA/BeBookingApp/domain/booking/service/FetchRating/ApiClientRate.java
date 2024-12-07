package KMA.BeBookingApp.domain.booking.service.FetchRating;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ApiClientRate {

    private final RestTemplate restTemplate;

    public ApiClientRate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResponse fetchData(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                builder.build().toUri(),
                HttpMethod.GET,
                null,
                ApiResponse.class
        );
        return responseEntity.getBody();
    }
}
