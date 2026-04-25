package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TripActivity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    private Integer tripId;
    private Integer activityId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime addedAt;
}
