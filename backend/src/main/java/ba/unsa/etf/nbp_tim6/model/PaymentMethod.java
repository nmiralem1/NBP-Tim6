package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Represents a payment method such as credit card, PayPal, or bank transfer")
public class PaymentMethod {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the payment method", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "Payment method name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    @Schema(description = "Name of the payment method", example = "Credit Card")
    private String name;
}