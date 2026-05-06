package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.dto.UpdateProfileRequest;
import ba.unsa.etf.nbp_tim6.model.ProfileImage;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for user profile management")
public class UserProfileController {

    private final UserRepository userRepository;

    public UserProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Get my profile",
            description = "Returns the currently authenticated user's profile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        user.setPasswordHash(null);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Update my profile",
            description = "Updates first name, last name, username, email and phone for the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(
            Authentication authentication,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User profile update data",
                    required = true
            )
            @RequestBody UpdateProfileRequest request
    ) {
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        User updatedUser = userRepository.updateProfile(
                currentUser.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getEmail(),
                request.getPhone()
        );

        updatedUser.setPasswordHash(null);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Upload my profile image",
            description = "Uploads profile image for the currently authenticated user and stores it as BLOB in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image file"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMyProfileImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");

        if (file.getContentType() == null || !allowedTypes.contains(file.getContentType())) {
            return ResponseEntity.badRequest().body("Only JPG, PNG and WEBP images are allowed.");
        }

        long maxSize = 2 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            return ResponseEntity.badRequest().body("Image must be smaller than 2MB.");
        }

        userRepository.saveProfileImage(
                currentUser.getId(),
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        );

        return ResponseEntity.ok("Profile image uploaded successfully.");
    }

    @Operation(
            summary = "Get my profile image",
            description = "Returns profile image BLOB for the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile image retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile image not found"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/me/profile-image")
    public ResponseEntity<byte[]> getMyProfileImage(Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return userRepository.findProfileImageByUserId(currentUser.getId())
                .map(this::buildProfileImageResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<byte[]> buildProfileImageResponse(ProfileImage profileImage) {
        String contentType = profileImage.getContentType() != null
                ? profileImage.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        String fileName = profileImage.getFileName() != null
                ? profileImage.getFileName()
                : "profile-image";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(profileImage.getData());
    }
}