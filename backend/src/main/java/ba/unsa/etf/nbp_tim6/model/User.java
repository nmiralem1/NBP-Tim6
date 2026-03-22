package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String passwordHash;
    private String phone;
    private LocalDateTime createdAt;
}