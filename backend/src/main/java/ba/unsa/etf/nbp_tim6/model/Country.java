package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;

@Data
public class Country {

    private Integer id;
    private String name;
    private String code;
    private String currency;
    private String language;
}