package ba.unsa.etf.nbp_tim6.model;

import lombok.Data;

@Data
public class User {

    private Integer id;
    @com.fasterxml.jackson.annotation.JsonProperty("firstName")
    private String firstName;
    @com.fasterxml.jackson.annotation.JsonProperty("lastName")
    private String lastName;
    @com.fasterxml.jackson.annotation.JsonProperty("username")
    private String username;
    @com.fasterxml.jackson.annotation.JsonProperty("email")
    private String email;
    @com.fasterxml.jackson.annotation.JsonProperty("passwordHash")
    private String passwordHash;
    @com.fasterxml.jackson.annotation.JsonProperty("phone")
    private String phone;
    @com.fasterxml.jackson.annotation.JsonProperty("role")
    private String role; // e.g., "ROLE_USER"
}