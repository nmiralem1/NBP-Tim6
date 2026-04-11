package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Represents a user review for an accommodation or activity")
public class Review {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the review", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who wrote the review", example = "5")
    private Integer userId;

    @Schema(description = "ID of the accommodation being reviewed (optional if reviewing an activity)", example = "3")
    private Integer accommodationId;

    @Schema(description = "ID of the activity being reviewed (optional if reviewing an accommodation)", example = "7")
    private Integer activityId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating cannot exceed 10")
    @Schema(description = "Rating given by the user (1 to 10)", example = "5")
    private Integer rating;

    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    @Schema(description = "Optional review comment", example = "Amazing experience, highly recommended!")
    private String note;

    @Schema(description = "Date and time when the review was created", example = "2026-05-10T15:20:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Name of the user who wrote the review", example = "Enna H.", accessMode = Schema.AccessMode.READ_ONLY)
    private String userName;

    @Schema(description = "Location of the user", example = "Sarajevo, Bosnia and Herzegovina", accessMode = Schema.AccessMode.READ_ONLY)
    private String userLocation;
}