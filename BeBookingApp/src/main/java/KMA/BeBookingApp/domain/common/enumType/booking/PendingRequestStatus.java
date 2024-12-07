package KMA.BeBookingApp.domain.common.enumType.booking;

public enum PendingRequestStatus {
    PENDING,  // Yêu cầu đang chờ xử lý
    EXPIRED,  // Yêu cầu đã hết hạn
    BOOKED    // Yêu cầu đã được xác nhận và đặt thành công
}
