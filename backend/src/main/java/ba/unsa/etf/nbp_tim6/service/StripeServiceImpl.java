package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.dto.StripePaymentRequest;
import ba.unsa.etf.nbp_tim6.service.abstraction.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {

    public StripeServiceImpl(@Value("${stripe.secret.key}") String secretKey) {
        Stripe.apiKey = secretKey;
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
                .build();

        return PaymentIntent.create(params);
    }
}