package ba.unsa.etf.nbp_tim6.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login credentials")
public class LoginRequest {

    @Schema(description = "Username or email of the user", example = "johndoe")
    private String username;

    @Schema(description = "Password of the user", example = "password123")
    private String password;
}
