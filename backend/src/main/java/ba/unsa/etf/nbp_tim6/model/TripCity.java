package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Represents a city included in a trip itinerary with arrival and departure dates")
public class TripCity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the trip-city relation", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "Trip ID is required")
    @Schema(description = "ID of the trip", example = "5")
    private Integer tripId;

    @NotNull(message = "City ID is required")
    @Schema(description = "ID of the city included in the trip", example = "3")
    private Integer cityId;

    @NotNull(message = "Arrival date is required")
    @Schema(description = "Date when the user arrives in the city", example = "2026-07-10")
    private LocalDate arrivalDate;

    @NotNull(message = "Departure date is required")
    @Schema(description = "Date when the user leaves the city", example = "2026-07-12")
    private LocalDate departureDate;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Schema(description = "Optional notes for the stay in this city", example = "Visit museums and try local food")
    private String notes;
}