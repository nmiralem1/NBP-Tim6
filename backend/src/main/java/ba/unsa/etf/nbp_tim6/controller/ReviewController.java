package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Review;
import ba.unsa.etf.nbp_tim6.service.abstraction.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(
        name = "Review",
        description = "Endpoints for managing user reviews and ratings for accommodations and services"
)
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all reviews",
            description = "Returns a list of all reviews"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    })
    @GetMapping
    public List<Review> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get review by ID",
            description = "Returns a review based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public Review getById(
            @Parameter(description = "ID of the review", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Get reviews by accommodation ID",
            description = "Returns all reviews for a specific accommodation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Accommodation not found")
    })
    @GetMapping("/accommodation/{accommodationId}")
    public List<Review> getByAccommodation(
            @Parameter(description = "ID of the accommodation", example = "3")
            @PathVariable Integer accommodationId) {
        return service.getByAccommodation(accommodationId);
    }

    @Operation(
            summary = "Get reviews by activity ID",
            description = "Returns all reviews for a specific activity"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @GetMapping("/activity/{activityId}")
    public List<Review> getByActivity(
            @Parameter(description = "ID of the activity", example = "7")
            @PathVariable Integer activityId) {
        return service.getByActivity(activityId);
    }

    @Operation(
            summary = "Create review",
            description = "Creates a new review for an accommodation or activity"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data")
    })
    @PostMapping
    public String create(@Valid @RequestBody Review review) {
        service.create(review);
        return "Review created!";
    }

    @Operation(
            summary = "Update review",
            description = "Updates an existing review by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the review", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Review review) {
        review.setId(id);
        service.update(review);
        return "Review updated!";
    }

    @Operation(
            summary = "Delete review",
            description = "Deletes a review by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the review", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Review deleted!";
    }
}