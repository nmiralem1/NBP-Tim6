package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class City {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private Integer countryId;
    private String name;
    private String postalCode;
    private String description;
    private String imageUrl;
    private String countryName;
    private String continent;
}