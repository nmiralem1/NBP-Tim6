package ba.unsa.etf.nbp_tim6.controller;

import ba.unsa.etf.nbp_tim6.model.Invoice;
import ba.unsa.etf.nbp_tim6.service.abstraction.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoice", description = "Endpoints for retrieving and downloading booking invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "Get invoices for a user", description = "Returns all invoice metadata for the given user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoices retrieved"),
            @ApiResponse(responseCode = "404", description = "No invoices found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getByUser(
            @Parameter(description = "ID of the user") @PathVariable Integer userId) {
        List<Invoice> invoices = invoiceService.getByUserId(userId);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoice by booking ID", description = "Returns invoice metadata for the given booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice retrieved"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Invoice> getByBooking(
            @Parameter(description = "ID of the booking") @PathVariable Integer bookingId) {
        Invoice invoice = invoiceService.getByBookingId(bookingId);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Download invoice PDF by booking ID", description = "Downloads the PDF invoice for the given booking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF returned"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/booking/{bookingId}/pdf")
    public ResponseEntity<byte[]> downloadByBooking(
            @Parameter(description = "ID of the booking") @PathVariable Integer bookingId) {
        byte[] pdf = invoiceService.getPdfByBookingId(bookingId);
        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        Invoice invoice = invoiceService.getByBookingId(bookingId);
        String filename = invoice != null ? invoice.getInvoiceNumber() + ".pdf" : "invoice.pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(pdf.length);

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @Operation(summary = "Download invoice PDF by invoice ID", description = "Downloads the PDF invoice by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF returned"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadById(
            @Parameter(description = "ID of the invoice") @PathVariable Integer id) {
        byte[] pdf = invoiceService.getPdfById(id);
        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice-" + id + ".pdf");
        headers.setContentLength(pdf.length);

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
