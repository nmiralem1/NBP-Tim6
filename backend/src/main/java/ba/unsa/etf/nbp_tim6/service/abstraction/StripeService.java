package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.dto.StripePaymentRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {
    PaymentIntent createPaymentIntent(StripePaymentRequest request) throws StripeException;
    void handleWebhookEvent(String payload, String sigHeader) throws SignatureVerificationException;
}