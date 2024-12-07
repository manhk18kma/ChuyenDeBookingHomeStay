package KMA.BeBookingApp.domain.payment.service.abtract;

import KMA.BeBookingApp.domain.payment.dto.response.IpnResponse;

import java.util.Map;

public interface IpnService {
    IpnResponse handlePaymentGatewayCallback(Map<String, String> params);

}
