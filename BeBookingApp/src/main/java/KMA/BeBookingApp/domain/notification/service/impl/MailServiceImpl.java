package KMA.BeBookingApp.domain.notification.service.impl;

import KMA.BeBookingApp.domain.notification.service.abtract.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${client.reset-password.url}")
    String resetPasswordUrl;

    @Override
    public void sendResetPasswordEmail(String token, String recipientEmail) {
        String resetUrl = resetPasswordUrl + token;
        System.out.println(resetUrl);
        String emailSubject = "Reset Your Password";
        String emailContent = buildResetPasswordEmailContent(resetUrl);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipientEmail);
            helper.setSubject(emailSubject);
            helper.setText(emailContent, true);

            mailSender.send(message);
            log.info("Reset password email sent to {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("Failed to send reset password email to {}: {}", recipientEmail, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

//    private String buildResetPasswordEmailContent(String resetUrl) {
//        return "<!DOCTYPE html>"
//                + "<html lang='en'>"
//                + "<head>"
//                + "    <meta charset='UTF-8'>"
//                + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
//                + "    <title>Reset Password</title>"
//                + "    <style>"
//                + "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7; }"
//                + "        .container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; }"
//                + "        .header { text-align: center; padding: 10px 0; background-color: #4CAF50; color: #ffffff; }"
//                + "        .content { padding: 20px; text-align: center; color: #333333; }"
//                + "        .button { text-align: center; margin: 20px 0; }"
//                + "        .button a { background-color: #4CAF50; color: #ffffff; padding: 15px 25px; text-decoration: none; border-radius: 5px; }"
//                + "        .footer { text-align: center; font-size: 12px; color: #aaaaaa; margin-top: 20px; }"
//                + "    </style>"
//                + "</head>"
//                + "<body>"
//                + "    <div class='container'>"
//                + "        <div class='header'><h2>KMA Booking App</h2></div>"
//                + "        <div class='content'>"
//                + "            <p>Hello,</p>"
//                + "            <p>We received a request to reset your password. Click the button below to proceed:</p>"
//                + "            <div class='button'><a href='" + resetUrl + "'>Reset Password</a></div>"
//                + "            <p>If you didn't request this, please ignore this email.</p>"
//                + "        </div>"
//                + "        <div class='footer'>"
//                + "            <p>&copy; 2023 KMA Booking App. All rights reserved.</p>"
//                + "        </div>"
//                + "    </div>"
//                + "</body>"
//                + "</html>";
//    }

    private String buildResetPasswordEmailContent(String resetUrl) {
        // In ra URL để kiểm tra trong console.
        System.out.println(resetUrl);
        String token = resetUrl.substring(resetUrl.lastIndexOf("/") + 1); // Lấy token từ URL
        return "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "    <meta charset='UTF-8'>"
                + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "    <title>Reset Password</title>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7; }"
                + "        .container { width: 100%; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; }"
                + "        .header { text-align: center; padding: 10px 0; background-color: #4CAF50; color: #ffffff; }"
                + "        .content { padding: 20px; text-align: center; color: #333333; }"
                + "        .button { text-align: center; margin: 20px 0; }"
                + "        .footer { text-align: center; font-size: 12px; color: #aaaaaa; margin-top: 20px; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'><h2>KMA Booking App</h2></div>"
                + "        <div class='content'>"
                + "            <p>Hello,</p>"
                + "            <p>We received a request to reset your password. Here is your reset token:</p>"
                + "            <div style='font-size: 18px; font-weight: bold; padding: 10px; background-color: #f1f1f1; border-radius: 5px;'>"
                + "                " + token + "</div>"
                + "            <p>You can copy this token and use it to reset your password.</p>"
                + "            <p>If you didn't request this, please ignore this email.</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>&copy; 2023 KMA Booking App. All rights reserved.</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }




}

