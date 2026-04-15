package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.dto.LoginRequest;
import ba.unsa.etf.nbp_tim6.model.RefreshToken;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import ba.unsa.etf.nbp_tim6.security.JwtUtils;
import ba.unsa.etf.nbp_tim6.security.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and session management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username, email, or password")
    })
    @Operation(summary = "Login user", description = "Authenticates a user and returns JWT tokens in cookies.", security = @SecurityRequirement(name = ""))
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest,
                                              HttpServletResponse response) {
        try {
            String identifier = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(identifier)
                    .or(() -> userRepository.findByEmail(identifier))
                    .orElseThrow();
            String jwt = jwtUtils.generateToken(user.getUsername());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

            addTokenCookies(response, jwt, refreshToken.getToken());

            return ResponseEntity.ok(Map.of(
                    "message", "User logged in successfully!",
                    "accessToken", jwt,
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "role", user.getRole(),
                    "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                    "lastName", user.getLastName() != null ? user.getLastName() : "",
                    "phone", user.getPhone() != null ? user.getPhone() : ""));
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    @Operation(summary = "Register user", description = "Creates a new user account.", security = @SecurityRequirement(name = ""))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Username or email is already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already in use!"));
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole("REGISTERED_USER");
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    @Operation(summary = "Refresh token", description = "Rotates the refresh token and returns a new access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "403", description = "Refresh token is missing, invalid, or expired")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenString = parseRefreshToken(request);

        if (refreshTokenString != null) {
            return refreshTokenService.findByToken(refreshTokenString)
                    .map(refreshTokenService::verifyExpiration)
                    .map(token -> {
                        User user = userRepository.findById(token.getUserId()).orElseThrow();
                        String jwt = jwtUtils.generateToken(user.getUsername());

                        refreshTokenService.deleteByToken(token.getToken());
                        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

                        addTokenCookies(response, jwt, newRefreshToken.getToken());
                        return ResponseEntity.ok(Map.of("message", "Token refreshed successfully!"));
                    })
                    .orElse(ResponseEntity.status(403).body(Map.of("message", "Refresh token is not in database!")));
        }

        return ResponseEntity.status(403).body(Map.of("message", "Refresh Token is null!"));
    }

    @Operation(summary = "Logout user", description = "Invalidates the user's session and clears cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log out successful")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenString = parseRefreshToken(request);
        if (refreshTokenString != null) {
            refreshTokenService.deleteByToken(refreshTokenString);
        }

        ResponseCookie deleteAccessToken = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie deleteRefreshToken = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshToken.toString());

        return ResponseEntity.ok(Map.of("message", "Log out successful!"));
    }

    private void addTokenCookies(HttpServletResponse response, String jwt, String refreshToken) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwt)
                .httpOnly(true).secure(false).path("/").maxAge(24 * 60 * 60).sameSite("Lax").build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true).secure(false).path("/").maxAge(7 * 24 * 60 * 60).sameSite("Lax").build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private String parseRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}