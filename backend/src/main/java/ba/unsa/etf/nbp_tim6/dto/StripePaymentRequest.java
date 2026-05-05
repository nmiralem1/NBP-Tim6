package ba.unsa.etf.nbp_tim6.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request for creating a Stripe payment")
public class StripePaymentRequest {

    @NotNull
    @Min(1)
    @Schema(description = "Payment amount in the smallest currency unit (e.g., cents for USD)", example = "5000")
    private Long amount;

    @NotBlank
    @Schema(description = "Currency code for the payment", example = "usd")
    private String currency;

    @Schema(description = "ID of the booking associated with this payment", example = "15")
    private Integer bookingId;

    @Schema(description = "ID of the trip associated with this payment", example = "5")
    private Integer tripId;

    @Schema(description = "ID of the user making the payment", example = "10")
    private Integer userId;

    @Schema(description = "ID of the payment method to use", example = "1")
    private Integer paymentMethodId;
}