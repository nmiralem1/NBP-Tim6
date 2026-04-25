package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Represents a booking made by a user for an accommodation")
public class Booking {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the booking", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "ID of the trip associated with this booking", example = "5")
    private Integer tripId;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who made the booking", example = "10")
    private Integer userId;

    @NotNull(message = "Accommodation ID is required")
    @Schema(description = "ID of the booked accommodation", example = "3")
    private Integer accommodationId;

    @NotNull(message = "Check-in date is required")
    @Schema(description = "Check-in date", example = "2026-06-01")
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @Schema(description = "Check-out date", example = "2026-06-05")
    private LocalDate checkOut;

    @NotNull(message = "Guests count is required")
    @Min(value = 1, message = "At least one guest is required")
    @Schema(description = "Number of guests", example = "2")
    private Integer guestsCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Total price of the booking", example = "480.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Status of the booking (e.g., PENDING, CONFIRMED, CANCELLED)", example = "CONFIRMED", accessMode = Schema.AccessMode.READ_ONLY)
    private String bookingStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique booking reference code", example = "BK-2026-000123", accessMode = Schema.AccessMode.READ_ONLY)
    private String bookingReference;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when the booking was created", example = "2026-05-01T12:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}