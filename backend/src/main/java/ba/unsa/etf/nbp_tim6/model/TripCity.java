package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TripCity {

    private Integer id;
    private Integer tripId;
    private Integer cityId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private String notes;
}