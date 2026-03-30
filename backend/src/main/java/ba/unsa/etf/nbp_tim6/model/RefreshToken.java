package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;
import java.time.Instant;

@Data
public class RefreshToken {
    private Integer id;
    private Integer userId;
    private String token;
    private Instant expiryDate;
}
