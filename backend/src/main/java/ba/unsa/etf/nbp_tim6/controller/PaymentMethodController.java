package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.PaymentMethod;
import ba.unsa.etf.nbp_tim6.service.abstraction.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@Tag(
        name = "Payment Method",
        description = "Endpoints for managing available payment methods"
)
public class PaymentMethodController {

    private final PaymentMethodService service;

    public PaymentMethodController(PaymentMethodService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all payment methods",
            description = "Returns a list of all payment methods"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment methods retrieved successfully")
    })
    @GetMapping
    public List<PaymentMethod> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get payment method by ID",
            description = "Returns a payment method based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @GetMapping("/{id}")
    public PaymentMethod getById(
            @Parameter(description = "ID of the payment method", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Create payment method",
            description = "Creates a new payment method"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment method data")
    })
    @PostMapping
    public String create(@Valid @RequestBody PaymentMethod method) {
        service.create(method);
        return "Payment method created!";
    }

    @Operation(
            summary = "Update payment method",
            description = "Updates an existing payment method by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment method data"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the payment method", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody PaymentMethod method) {
        method.setId(id);
        service.update(method);
        return "Payment method updated!";
    }

    @Operation(
            summary = "Delete payment method",
            description = "Deletes a payment method by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment method not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the payment method", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Payment method deleted!";
    }
}