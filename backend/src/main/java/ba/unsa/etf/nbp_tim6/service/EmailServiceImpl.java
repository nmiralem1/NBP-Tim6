package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.service.abstraction.EmailService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    public void sendBookingConfirmation(String toEmail, String fullName, String bookingId, String amount) {
        try {
            Email from = new Email(fromEmail, fromName);
            String subject = "Booking confirmation";

            Email to = new Email(toEmail, fullName);

            String htmlContent =
                    "<h2>Booking Confirmation</h2>" +
                            "<p>Dear " + fullName + ",</p>" +
                            "<p>We are pleased to inform you that your payment has been successfully processed and your booking has been confirmed.</p>" +
                            "<p><strong>Booking ID:</strong> " + bookingId + "</p>" +
                            "<p><strong>Amount Paid:</strong> " + amount + "</p>" +
                            "<p>If you have any questions regarding your reservation, please feel free to contact us.</p>" +
                            "<p>Thank you for choosing our service.</p>" +
                            "<p>Kind regards,<br>Travel App Team</p>";

            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid error: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}