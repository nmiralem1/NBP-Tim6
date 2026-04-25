package ba.unsa.etf.nbp_tim6.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User entity representing a travel planner user")
public class User {

    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;
    @Schema(description = "User's first name", example = "John")
    @com.fasterxml.jackson.annotation.JsonProperty("firstName")
    private String firstName;
    @Schema(description = "User's last name", example = "Doe")
    @com.fasterxml.jackson.annotation.JsonProperty("lastName")
    private String lastName;
    @Schema(description = "Unique username for the user", example = "johndoe")
    @com.fasterxml.jackson.annotation.JsonProperty("username")
    private String username;
    @Schema(description = "User's email address", example = "john.doe@example.com")
    @com.fasterxml.jackson.annotation.JsonProperty("email")
    private String email;
    @Schema(description = "Hashed password of the user", accessMode = Schema.AccessMode.WRITE_ONLY)
    @com.fasterxml.jackson.annotation.JsonProperty("passwordHash")
    private String passwordHash;
    @Schema(description = "User's phone number", example = "+387 61 123 456")
    @com.fasterxml.jackson.annotation.JsonProperty("phone")
    private String phone;
    @Schema(description = "User's role in the application", example = "ROLE_USER")
    @com.fasterxml.jackson.annotation.JsonProperty("role")
    private String role; // e.g., "ROLE_USER"
}