package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Represents a transportation booking such as a flight, bus, or train between two cities")
public class Transport {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the transport record", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "Trip ID is required")
    @Schema(description = "ID of the trip associated with this transport", example = "5")
    private Integer tripId;

    @NotNull(message = "Departure city ID is required")
    @Schema(description = "ID of the departure city", example = "3")
    private Integer fromCityId;

    @NotNull(message = "Destination city ID is required")
    @Schema(description = "ID of the destination city", example = "4")
    private Integer toCityId;

    @NotNull(message = "Transport type ID is required")
    @Schema(description = "ID of the transport type (e.g., bus, flight, train)", example = "2")
    private Integer transportTypeId;

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    @Schema(description = "Name of the transport company", example = "FlixBus")
    private String companyName;

    @NotNull(message = "Departure time is required")
    @Schema(description = "Departure date and time", example = "2026-06-01T08:30:00")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @Schema(description = "Arrival date and time", example = "2026-06-01T12:00:00")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Ticket price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Ticket price must be greater than 0")
    @Schema(description = "Price of the transport ticket", example = "45.00")
    private BigDecimal ticketPrice;

    @Schema(description = "Seat number assigned to the user", example = "12A", accessMode = Schema.AccessMode.READ_ONLY)
    private String seatNumber;

    @Schema(description = "Unique booking reference for the transport", example = "TR-2026-000321", accessMode = Schema.AccessMode.READ_ONLY)
    private String bookingReference;
}