package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private Integer tripId;
    private Integer bookingId;
    private Integer userId;
    private Integer paymentMethodId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
}