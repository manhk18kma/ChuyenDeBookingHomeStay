package KMA.BeBookingApp;

import KMA.BeBookingApp.domain.booking.repository.HomestayAvailabilityRepository;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRate;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRateService;
import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.common.service.UploadService;
import KMA.BeBookingApp.domain.homestay.entity.Space;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
@EnableFeignClients(basePackages = "KMA.BeBookingApp.domain.user.repository.httpclient")
@RequiredArgsConstructor
@EnableScheduling
public class BeBookingAppApplication implements CommandLineRunner {

	@NonFinal
	@Value("${recaptcha.secret-key}")
	private String secretKey;
	@NonFinal
	@Value("${recaptcha.verify-url}")
	private  String VERIFY_URL;

	private final ExchangeRateService exchangeRateService;
	private final HomestayAvailabilityRepository homestayAvailabilityRepository;
	public static void main(String[] args) {
		SpringApplication.run(BeBookingAppApplication.class, args);


	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
//		ExchangeRate exchangeRate = exchangeRateService.getExchangeRates();
//		homestayAvailabilityRepository.updateRateEveryDay(BigDecimal.valueOf(6789.48), LocalDate.now() , HomestayAvailabilityStatus.BOOKED);

	}

	public boolean submitCaptcha(String captchaToken) {
		try {
			String url = String.format("%s?secret=%s&response=%s", VERIFY_URL, secretKey, captchaToken);

			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			String responseBody = response.body();
			System.out.println(responseBody);

			return responseBody != null && responseBody.contains("\"success\": true");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
