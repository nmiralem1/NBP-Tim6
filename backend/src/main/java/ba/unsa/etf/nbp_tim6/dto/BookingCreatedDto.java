package ba.unsa.etf.nbp_tim6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BookingCreatedDto {
    private Integer bookingId;
    private BigDecimal totalPrice;
    private String bookingReference;
}
