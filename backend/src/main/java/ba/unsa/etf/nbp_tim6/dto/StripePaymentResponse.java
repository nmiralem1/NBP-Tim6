package ba.unsa.etf.nbp_tim6.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response containing Stripe PaymentIntent details")
public class StripePaymentResponse {
    @Schema(description = "Unique identifier for the Stripe PaymentIntent", example = "pi_1234567890abcdef")
    private String paymentIntentId;

    @Schema(description = "Client secret used to complete the payment on the frontend", example = "pi_1234567890abcdef_secret_9876543210")
    private String clientSecret;

    @Schema(description = "Current status of the PaymentIntent", example = "requires_payment_method")
    private String status;
}