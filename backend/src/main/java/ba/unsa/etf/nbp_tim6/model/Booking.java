package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Booking {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private Integer tripId;
    private Integer userId;
    private Integer accommodationId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer guestsCount;
    private BigDecimal totalPrice;
    private String bookingStatus;
    private String bookingReference;
    private LocalDateTime createdAt;
}