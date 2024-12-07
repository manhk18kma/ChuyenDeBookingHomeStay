package KMA.BeBookingApp.domain.homestay.dto.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ConfirmHomestayProcessRequest implements Serializable {
    private Long defaultPricePerNight;
}
