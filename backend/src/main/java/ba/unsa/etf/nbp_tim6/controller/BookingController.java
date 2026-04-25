package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.dto.BookingCreatedDto;
import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.service.abstraction.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(
        name = "Booking",
        description = "Endpoints for creating and managing bookings for accommodations and travel services"
)
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Create booking",
            description = "Creates a new booking for an accommodation"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid booking data")
    })
    @PostMapping
    public ResponseEntity<BookingCreatedDto> createBooking(@Valid @RequestBody Booking booking) {
        BookingCreatedDto result = bookingService.createBooking(booking);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get booking by ID",
            description = "Returns a booking based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public Booking getBookingById(
            @Parameter(description = "ID of the booking", example = "1")
            @PathVariable Integer id) {
        return bookingService.getBookingById(id);
    }

    @Operation(
            summary = "Get bookings by user ID",
            description = "Returns all bookings associated with a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public java.util.List<Booking> getBookingsByUserId(
            @Parameter(description = "ID of the user", example = "5")
            @PathVariable Integer userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @Operation(
            summary = "Update booking",
            description = "Updates an existing booking by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid booking data"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PutMapping("/{id}")
    public String updateBooking(
            @Parameter(description = "ID of the booking", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Booking booking) {
        booking.setId(id);
        bookingService.updateBooking(booking);
        return "Booking updated!";
    }

    @Operation(
            summary = "Delete booking",
            description = "Deletes a booking by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    public String deleteBooking(
            @Parameter(description = "ID of the booking", example = "1")
            @PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return "Booking deleted!";
    }

    @Operation(
            summary = "Get my bookings",
            description = "Returns all bookings for the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/me")
    public List<Booking> getMyBookings(Authentication authentication) {
        return bookingService.getBookingsForAuthenticatedUser(authentication.getName());
    }

    @Operation(summary = "Get bookings by trip ID", description = "Returns all bookings for a specific trip")
    @GetMapping("/trip/{tripId}")
    public List<Booking> getBookingsByTripId(@PathVariable Integer tripId) {
        return bookingService.getBookingsByTripId(tripId);
    }
}