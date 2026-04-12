package ba.unsa.etf.nbp_tim6.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
}