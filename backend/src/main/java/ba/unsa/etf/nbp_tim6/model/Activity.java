package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Activity {

    private Integer id;
    private Integer tripId;
    private Integer cityId;
    private Integer activityTypeId;
    private String name;
    private String description;
    private String location;
    private LocalDate activityDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
}