package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Transport;
import ba.unsa.etf.nbp_tim6.service.abstraction.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
@Tag(
        name = "Transport",
        description = "Endpoints for managing transportation options (bus, flight, train, etc.)"
)
public class TransportController {

    private final TransportService service;

    public TransportController(TransportService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all transport options",
            description = "Returns a list of all transport records"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport records retrieved successfully")
    })
    @GetMapping
    public List<Transport> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get transport by ID",
            description = "Returns a transport record based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transport not found")
    })
    @GetMapping("/{id}")
    public Transport getById(
            @Parameter(description = "ID of the transport", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Get transport by trip ID",
            description = "Returns all transport records associated with a specific trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @GetMapping("/trip/{tripId}")
    public List<Transport> getByTripId(
            @Parameter(description = "ID of the trip", example = "5")
            @PathVariable Integer tripId) {
        return service.getByTripId(tripId);
    }

    @Operation(
            summary = "Create transport",
            description = "Creates a new transport record for a trip"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transport data")
    })
    @PostMapping
    public String create(@Valid @RequestBody Transport transport) {
        service.create(transport);
        return "Transport successfully created!";
    }

    @Operation(
            summary = "Update transport",
            description = "Updates an existing transport record by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transport data"),
            @ApiResponse(responseCode = "404", description = "Transport not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the transport", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody Transport transport) {
        transport.setId(id);
        service.update(transport);
        return "Transport updated!";
    }

    @Operation(
            summary = "Delete transport",
            description = "Deletes a transport record by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transport not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the transport", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Transport deleted!";
    }
}