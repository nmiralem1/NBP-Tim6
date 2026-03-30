package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class Accommodation {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private Integer cityId;
    private Integer accommodationTypeId;
    private String name;
    private String address;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxGuests;
    private Integer stars;
    private String phone;
    private String email;
    private String imageUrl;
}