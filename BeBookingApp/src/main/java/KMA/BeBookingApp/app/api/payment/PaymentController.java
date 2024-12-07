package KMA.BeBookingApp.app.api.payment;

import KMA.BeBookingApp.domain.payment.service.abtract.IpnService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    IpnService ipnService;

    @GetMapping("/vnpay_ipn")
    public void handleCallback(
            @Parameter(description = "Callback data from VNPay", required = true)
            @RequestParam Map<String, String> params) {
        log.info("[VNPay Ipn] Params: {}", params);
        ipnService.handlePaymentGatewayCallback(params);
    }
}
