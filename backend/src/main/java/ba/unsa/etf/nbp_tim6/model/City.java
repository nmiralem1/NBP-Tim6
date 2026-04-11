package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Represents a city available for travel, accommodations, and activities")
public class City {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the city", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "Country ID is required")
    @Schema(description = "ID of the country this city belongs to", example = "2")
    private Integer countryId;

    @NotBlank(message = "City name is required")
    @Size(max = 100)
    @Schema(description = "Name of the city", example = "Sarajevo")
    private String name;

    @Size(max = 20)
    @Schema(description = "Postal code of the city", example = "71000")
    private String postalCode;

    @Size(max = 1000)
    @Schema(description = "Description of the city", example = "Capital city of Bosnia and Herzegovina, known for its rich history and culture")
    private String description;

    @Schema(description = "Image URL representing the city", example = "https://example.com/images/sarajevo.jpg")
    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Name of the country (derived field)", example = "Bosnia and Herzegovina", accessMode = Schema.AccessMode.READ_ONLY)
    private String countryName;

    @Schema(description = "Continent where the city is located", example = "Europe")
    private String continent;
}