package ba.unsa.etf.nbp_tim6.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Response containing details of a newly created booking")
public class BookingCreatedDto {
    @Schema(description = "Unique identifier of the created booking", example = "42")
    private Integer bookingId;

    @Schema(description = "Total price of the booking", example = "480.00")
    private BigDecimal totalPrice;

    @Schema(description = "Unique booking reference code for customer reference", example = "BK-2026-000123")
    private String bookingReference;
}
