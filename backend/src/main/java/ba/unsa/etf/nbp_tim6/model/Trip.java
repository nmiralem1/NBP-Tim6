package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Represents a travel plan created by a user")
public class Trip {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the trip", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who created the trip", example = "7")
    private Integer userId;

    @NotBlank(message = "Trip title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @Schema(description = "Title of the trip", example = "Summer Vacation in Paris")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Detailed description of the trip", example = "A five-day city break including sightseeing, accommodation, and activities")
    private String description;

    @NotNull(message = "Start date is required")
    @Schema(description = "Start date of the trip", example = "2026-07-10")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Schema(description = "End date of the trip", example = "2026-07-15")
    private LocalDate endDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
    @Schema(description = "Planned budget for the trip", example = "1500.00")
    private BigDecimal budget;

    @Schema(description = "Status of the trip (e.g., PLANNED, ACTIVE, COMPLETED, CANCELLED)", example = "PLANNED", accessMode = Schema.AccessMode.READ_ONLY)
    private String status;

    @Schema(description = "Image URL representing the trip", example = "https://example.com/images/paris-trip.jpg")
    private String imageUrl;

    @Schema(description = "Timestamp when the trip was created", example = "2026-05-01T09:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}