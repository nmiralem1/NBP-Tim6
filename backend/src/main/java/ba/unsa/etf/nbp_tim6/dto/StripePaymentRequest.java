package ba.unsa.etf.nbp_tim6.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StripePaymentRequest {

    @NotNull
    @Min(1)
    private Long amount;

    @NotBlank
    private String currency;

    private Integer bookingId;
    private Integer tripId;
    private Integer userId;
}