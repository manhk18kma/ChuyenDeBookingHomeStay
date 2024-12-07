package KMA.BeBookingApp.domain.booking.service.FetchRating;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ApiClientRate apiClient;


    public ExchangeRate getExchangeRates() {
        String url = "https://api.exchangerate-api.com/v4/latest/USD";

        ApiResponse response = apiClient.fetchData(url);

        // Extract rates
        BigDecimal usdToEur = (BigDecimal) response.getRates().get("EUR");
        BigDecimal usdToVnd = (BigDecimal) response.getRates().get("VND");

        return new ExchangeRate(usdToEur, usdToVnd);
    }





}
