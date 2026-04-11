package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Represents a type of accommodation, such as hotel, apartment, hostel, or villa")
public class AccommodationType {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the accommodation type", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "Accommodation type name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    @Schema(description = "Name of the accommodation type", example = "Hotel")
    private String name;
}