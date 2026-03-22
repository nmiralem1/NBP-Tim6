package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Review {

    private Integer id;
    private Integer userId;
    private Integer accommodationId;
    private Integer activityId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}