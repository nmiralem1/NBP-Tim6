package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.dto.StripePaymentRequest;
import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.PaymentRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeServiceImpl implements StripeService {

    private final String webhookSecret;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public StripeServiceImpl(
            @Value("${stripe.secret.key}") String secretKey,
            @Value("${stripe.webhook.secret:}") String webhookSecret,
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository) {
        Stripe.apiKey = secretKey;
        this.webhookSecret = webhookSecret;
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public PaymentIntent createPaymentIntent(StripePaymentRequest request) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency().toLowerCase())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .putMetadata("bookingId", request.getBookingId() == null ? "" : request.getBookingId().toString())
                .putMetadata("tripId", request.getTripId() == null ? "" : request.getTripId().toString())
                .putMetadata("userId", request.getUserId() == null ? "" : request.getUserId().toString())
                .putMetadata("paymentMethodId", request.getPaymentMethodId() == null ? "" : request.getPaymentMethodId().toString())
                .build();

        return PaymentIntent.create(params);
    }

    @Override
    public void handleWebhookEvent(String payload, String sigHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        if ("payment_intent.succeeded".equals(event.getType())) {
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            if (deserializer.getObject().isPresent()) {
                PaymentIntent intent = (PaymentIntent) deserializer.getObject().get();
                processSuccessfulPayment(intent);
            }
        }
    }

    private void processSuccessfulPayment(PaymentIntent intent) {
        String bookingIdStr = intent.getMetadata().getOrDefault("bookingId", "");
        String tripIdStr = intent.getMetadata().getOrDefault("tripId", "");
        String userIdStr = intent.getMetadata().getOrDefault("userId", "");
        String paymentMethodIdStr = intent.getMetadata().getOrDefault("paymentMethodId", "");

        if (bookingIdStr.isEmpty()) return;

        Integer bookingId = Integer.parseInt(bookingIdStr);
        Integer tripId = tripIdStr.isEmpty() ? null : Integer.parseInt(tripIdStr);
        Integer userId = userIdStr.isEmpty() ? null : Integer.parseInt(userIdStr);
        Integer paymentMethodId = paymentMethodIdStr.isEmpty() ? null : Integer.parseInt(paymentMethodIdStr);

        bookingRepository.updateStatus(bookingId, "confirmed");

        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setTripId(tripId);
        payment.setUserId(userId);
        payment.setPaymentMethodId(paymentMethodId);
        payment.setAmount(BigDecimal.valueOf(intent.getAmount()).divide(BigDecimal.valueOf(100)));
        payment.setPaymentStatus("COMPLETED");

        paymentRepository.save(payment);
    }
}
