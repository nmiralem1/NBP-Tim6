package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Trip {

    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String status;
    private LocalDateTime createdAt;
}