package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Represents a country available in the travel system")
public class Country {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the country", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Schema(description = "Name of the country", example = "Bosnia and Herzegovina")
    private String name;

    @Size(max = 10, message = "Code cannot exceed 10 characters")
    @Schema(description = "Country code", example = "BA")
    private String code;

    @Size(max = 20, message = "Currency cannot exceed 20 characters")
    @Schema(description = "Official currency used in the country", example = "BAM")
    private String currency;

    @Size(max = 50, message = "Language cannot exceed 50 characters")
    @Schema(description = "Primary language spoken in the country", example = "Bosnian")
    private String language;

    @Size(max = 50, message = "Continent cannot exceed 50 characters")
    @Schema(description = "Continent where the country is located", example = "Europe")
    private String continent;
}