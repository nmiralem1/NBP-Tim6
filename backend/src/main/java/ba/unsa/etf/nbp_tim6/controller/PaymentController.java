package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(
        name = "Payment",
        description = "Endpoints for processing and managing payments"
)
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all payments",
            description = "Returns a list of all payments"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping
    public List<Payment> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get payment by ID",
            description = "Returns a payment based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    public Payment getById(
            @Parameter(description = "ID of the payment", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Get payments by trip ID",
            description = "Returns all payments associated with a specific trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @GetMapping("/trip/{tripId}")
    public List<Payment> getByTrip(
            @Parameter(description = "ID of the trip", example = "5")
            @PathVariable Integer tripId) {
        return service.getByTrip(tripId);
    }

    @Operation(
            summary = "Get payments by user ID",
            description = "Returns all payments made by a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public List<Payment> getByUser(
            @Parameter(description = "ID of the user", example = "7")
            @PathVariable Integer userId) {
        return service.getByUser(userId);
    }

    @Operation(
            summary = "Create payment",
            description = "Records a new payment for a booking or trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment data")
    })
    @PostMapping
    public String create(@Valid @RequestBody Payment payment) {
        service.create(payment);
        return "Payment successfully recorded!";
    }

    @Operation(
            summary = "Update payment",
            description = "Updates an existing payment by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment data"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the payment", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Payment payment) {
        payment.setId(id);
        service.update(payment);
        return "Payment updated!";
    }

    @Operation(
            summary = "Delete payment",
            description = "Deletes a payment by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the payment", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Payment deleted!";
    }
}