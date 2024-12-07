package KMA.BeBookingApp.domain.common.enumType.booking;

public enum HomestayAvailabilityStatus {

    AVAILABLE("Available"),
    BOOKED("Booked"),
    PENDING("Waiting for payment"),

    UNAVAILABLE("Unavailable");

    private final String status;

    HomestayAvailabilityStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
