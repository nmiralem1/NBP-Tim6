package ba.unsa.etf.nbp_tim6.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Invoice generated after a successful payment")
public class Invoice {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the invoice", example = "1")
    private Integer id;

    @Schema(description = "ID of the related payment", example = "5")
    private Integer paymentId;

    @Schema(description = "ID of the related booking", example = "12")
    private Integer bookingId;

    @Schema(description = "ID of the user this invoice belongs to", example = "7")
    private Integer userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique invoice number", example = "INV-2026-000001")
    private String invoiceNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "When the invoice was issued", example = "2026-05-12T14:30:00")
    private LocalDateTime issuedAt;

    @Schema(description = "Total amount on the invoice", example = "480.00")
    private BigDecimal totalAmount;

    @JsonIgnore
    private byte[] pdfData;
}
