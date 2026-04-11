package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
        import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "Represents an activity that can be planned or booked during a trip")
public class Activity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the activity", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "Trip ID is required")
    @Schema(description = "ID of the trip this activity belongs to", example = "5")
    private Integer tripId;

    @NotNull(message = "City ID is required")
    @Schema(description = "ID of the city where the activity takes place", example = "3")
    private Integer cityId;

    @NotNull(message = "Activity type ID is required")
    @Schema(description = "ID of the activity type", example = "2")
    private Integer activityTypeId;

    @NotBlank(message = "Activity name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Schema(description = "Name of the activity", example = "Walking Tour of Old Town")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Detailed description of the activity", example = "A guided walking tour through the historic city center")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    @Schema(description = "Location where the activity takes place", example = "Bascarsija, Sarajevo")
    private String location;

    @NotNull(message = "Activity date is required")
    @Schema(description = "Date of the activity", example = "2026-05-20")
    private LocalDate activityDate;

    @NotNull(message = "Start time is required")
    @Schema(description = "Start time of the activity", example = "10:00:00")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Schema(description = "End time of the activity", example = "12:30:00")
    private LocalTime endTime;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Schema(description = "Price of the activity in BAM", example = "25.00")
    private BigDecimal price;

    @Schema(description = "URL of the activity image", example = "https://example.com/images/activity.jpg")
    private String imageUrl;
}