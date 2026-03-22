package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;

@Data
public class City {

    private Integer id;
    private Integer countryId;
    private String name;
    private String postalCode;
    private String description;
}