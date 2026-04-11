package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Represents a payment made by a user for a booking or trip")
public class Payment {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the payment", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "Trip ID is required")
    @Schema(description = "ID of the related trip", example = "5")
    private Integer tripId;

    @NotNull(message = "Booking ID is required")
    @Schema(description = "ID of the related booking", example = "12")
    private Integer bookingId;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who made the payment", example = "7")
    private Integer userId;

    @NotNull(message = "Payment method ID is required")
    @Schema(description = "ID of the payment method used", example = "2")
    private Integer paymentMethodId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Schema(description = "Amount paid", example = "250.00")
    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Date and time when the payment was made", example = "2026-05-10T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime paymentDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Status of the payment (e.g., PENDING, COMPLETED, FAILED, REFUNDED)", example = "COMPLETED", accessMode = Schema.AccessMode.READ_ONLY)
    private String paymentStatus;
}