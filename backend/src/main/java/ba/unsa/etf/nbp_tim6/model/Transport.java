package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transport {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private Integer tripId;
    private Integer fromCityId;
    private Integer toCityId;
    private Integer transportTypeId;
    private String companyName;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal ticketPrice;
    private String seatNumber;
    private String bookingReference;
}
