package ba.unsa.etf.nbp_tim6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripePaymentResponse {
    private String paymentIntentId;
    private String clientSecret;
    private String status;
}