package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Represents an accommodation entity")
public class Accommodation {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the accommodation", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "City ID is required")
    @Schema(description = "ID of the city where the accommodation is located", example = "3")
    private Integer cityId;

    @NotNull(message = "Accommodation type ID is required")
    @Schema(description = "ID of the accommodation type (e.g., hotel, apartment, hostel)", example = "2")
    private Integer accommodationTypeId;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @Schema(description = "Name of the accommodation", example = "Hotel Europe")
    private String name;

    @Size(max = 200)
    @Schema(description = "Address of the accommodation", example = "Zmaja od Bosne 12, Sarajevo")
    private String address;

    @Size(max = 1000)
    @Schema(description = "Detailed description of the accommodation", example = "Modern hotel located in the city center with free Wi-Fi and breakfast included")
    private String description;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Price per night in BAM", example = "120.50")
    private BigDecimal pricePerNight;

    @NotNull(message = "Max guests is required")
    @Min(value = 1, message = "At least 1 guest is required")
    @Schema(description = "Maximum number of guests allowed", example = "4")
    private Integer maxGuests;

    @Min(value = 1, message = "Stars must be at least 1")
    @Max(value = 5, message = "Stars cannot exceed 5")
    @Schema(description = "Star rating of the accommodation (1-5)", example = "4")
    private Integer stars;

    @Schema(description = "Contact phone number", example = "+38761123456")
    private String phone;

    @Email(message = "Invalid email format")
    @Schema(description = "Contact email address", example = "info@hoteleurope.ba")
    private String email;

    @Schema(description = "URL of the accommodation image", example = "https://example.com/images/hotel.jpg")
    private String imageUrl;
}