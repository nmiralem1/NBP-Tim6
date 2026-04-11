package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.TransportType;
import ba.unsa.etf.nbp_tim6.service.abstraction.TransportTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport-types")
@Tag(
        name = "Transport Type",
        description = "Endpoints for managing different types of transportation"
)
public class TransportTypeController {

    private final TransportTypeService service;

    public TransportTypeController(TransportTypeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all transport types",
            description = "Returns a list of all transport types"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport types retrieved successfully")
    })
    @GetMapping
    public List<TransportType> getAll() {
        return service.getAll();
    }

    @Operation(
            summary = "Get transport type by ID",
            description = "Returns a transport type based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport type retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transport type not found")
    })
    @GetMapping("/{id}")
    public TransportType getById(
            @Parameter(description = "ID of the transport type", example = "1")
            @PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(
            summary = "Create transport type",
            description = "Creates a new transport type"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transport type data")
    })
    @PostMapping
    public String create(@Valid @RequestBody TransportType type) {
        service.create(type);
        return "Transport type created!";
    }

    @Operation(
            summary = "Update transport type",
            description = "Updates an existing transport type by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport type updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transport type data"),
            @ApiResponse(responseCode = "404", description = "Transport type not found")
    })
    @PutMapping("/{id}")
    public String update(
            @Parameter(description = "ID of the transport type", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody TransportType type) {
        type.setId(id);
        service.update(type);
        return "Transport type updated!";
    }

    @Operation(
            summary = "Delete transport type",
            description = "Deletes a transport type by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transport type not found")
    })
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "ID of the transport type", example = "1")
            @PathVariable Integer id) {
        service.delete(id);
        return "Transport type deleted!";
    }
}