package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Review {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private Integer userId;
    private Integer accommodationId;
    private Integer activityId;
    private Integer rating;
    private String note;
    private LocalDateTime createdAt;
    private String userName;
    private String userLocation;
}