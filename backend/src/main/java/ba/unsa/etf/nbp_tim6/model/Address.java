package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Address {

    private Integer id;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private LocalDateTime createdAt;
}