package KMA.BeBookingApp.domain.common.enumType.payment;

public enum PaymentMethod {
    PAYPAL("USD"),
    VNPAY("VND");

    // Thêm trường currency
    private final String currency;

    // Constructor của enum để gán giá trị cho trường currency
    PaymentMethod(String currency) {
        this.currency = currency;
    }

    // Phương thức getter để lấy giá trị của currency
    public String getCurrency() {
        return currency;
    }
}
