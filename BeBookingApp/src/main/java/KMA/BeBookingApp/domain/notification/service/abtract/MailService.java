package KMA.BeBookingApp.domain.notification.service.abtract;

public interface MailService {

    void sendResetPasswordEmail(String token, String recipientEmail);

}
