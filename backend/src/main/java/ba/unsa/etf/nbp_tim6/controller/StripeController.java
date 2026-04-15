package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.dto.StripePaymentRequest;
import ba.unsa.etf.nbp_tim6.dto.StripePaymentResponse;
import ba.unsa.etf.nbp_tim6.service.abstraction.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@Tag(
        name = "Stripe Payment",
        description = "Endpoints for Stripe test payments"
)
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Operation(
            summary = "Create Stripe PaymentIntent",
            description = "Creates a Stripe PaymentIntent and returns the client secret"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PaymentIntent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment data"),
            @ApiResponse(responseCode = "500", description = "Stripe error")
    })
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@Valid @RequestBody StripePaymentRequest request) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(request);

            return ResponseEntity.ok(
                    new StripePaymentResponse(
                            paymentIntent.getId(),
                            paymentIntent.getClientSecret(),
                            paymentIntent.getStatus()
                    )
            );
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Stripe error: " + e.getMessage()
            ));
        }
    }

    @Operation(
            summary = "Stripe Webhook",
            description = "Handles Stripe webhook events to confirm payments and update booking status"
    )
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody byte[] payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        try {
            String payloadStr = new String(payload, StandardCharsets.UTF_8);
            stripeService.handleWebhookEvent(payloadStr, sigHeader);
            return ResponseEntity.ok("Webhook processed");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Webhook error: " + e.getMessage());
        }
    }
}
