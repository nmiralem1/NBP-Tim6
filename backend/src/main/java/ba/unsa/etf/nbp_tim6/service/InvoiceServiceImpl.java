package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.model.Invoice;
import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.InvoiceRepositoryImpl;
import ba.unsa.etf.nbp_tim6.service.abstraction.InvoiceService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepositoryImpl invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepositoryImpl invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice generateAndSave(Payment payment, Booking booking, User user) {
        String invoiceNumber = generateInvoiceNumber(booking.getId());

        byte[] pdfBytes = buildPdf(invoiceNumber, payment, booking, user);

        Invoice invoice = new Invoice();
        invoice.setPaymentId(payment.getId());
        invoice.setBookingId(booking.getId());
        invoice.setUserId(user.getId());
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setTotalAmount(payment.getAmount());
        invoice.setPdfData(pdfBytes);

        Integer savedId = invoiceRepository.save(invoice);
        invoice.setId(savedId);
        invoice.setIssuedAt(LocalDateTime.now());
        return invoice;
    }

    @Override
    public byte[] getPdfByBookingId(Integer bookingId) {
        return invoiceRepository.findPdfByBookingId(bookingId);
    }

    @Override
    public byte[] getPdfById(Integer invoiceId) {
        return invoiceRepository.findPdfById(invoiceId);
    }

    @Override
    public Invoice getByBookingId(Integer bookingId) {
        return invoiceRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Invoice> getByUserId(Integer userId) {
        return invoiceRepository.findByUserId(userId);
    }

    private String generateInvoiceNumber(Integer bookingId) {
        int year = LocalDateTime.now().getYear();
        return String.format("INV-%d-%06d", year, bookingId);
    }

    private byte[] buildPdf(String invoiceNumber, Payment payment, Booking booking, User user) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(doc, out);
            doc.open();

            // ── Fonts ────────────────────────────────────────────────────────
            Font brandFont   = new Font(Font.HELVETICA, 22, Font.BOLD,   new Color(255, 255, 255));
            Font titleFont   = new Font(Font.HELVETICA, 26, Font.BOLD,   new Color(30, 58, 138));
            Font headingFont = new Font(Font.HELVETICA, 11, Font.BOLD,   new Color(30, 58, 138));
            Font labelFont   = new Font(Font.HELVETICA, 10, Font.BOLD,   new Color(75, 85, 99));
            Font valueFont   = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(17, 24, 39));
            Font smallGray   = new Font(Font.HELVETICA,  9, Font.NORMAL, new Color(107, 114, 128));
            Font totalFont   = new Font(Font.HELVETICA, 13, Font.BOLD,   new Color(255, 255, 255));

            Color primaryBlue  = new Color(30,  58, 138);
            Color lightBlue    = new Color(239, 246, 255);
            Color accentGreen  = new Color(5,  150,  105);
            Color borderGray   = new Color(209, 213, 219);
            Color rowAlt       = new Color(249, 250, 251);

            // ── Header bar ───────────────────────────────────────────────────
            PdfContentByte canvas = writer.getDirectContent();
            canvas.setColorFill(primaryBlue);
            canvas.rectangle(50, doc.top() - 70, doc.right() - doc.left(), 70);
            canvas.fill();

            // Brand name in header
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 1});
            headerTable.setSpacingBefore(0);

            PdfPCell brandCell = new PdfPCell(new Phrase("✈  TravelEase", brandFont));
            brandCell.setBorder(Rectangle.NO_BORDER);
            brandCell.setBackgroundColor(primaryBlue);
            brandCell.setPaddingTop(18);
            brandCell.setPaddingBottom(18);
            brandCell.setPaddingLeft(10);
            headerTable.addCell(brandCell);

            Font invoiceTagFont = new Font(Font.HELVETICA, 11, Font.NORMAL, new Color(147, 197, 253));
            PdfPCell tagCell = new PdfPCell(new Phrase("PAYMENT INVOICE", invoiceTagFont));
            tagCell.setBorder(Rectangle.NO_BORDER);
            tagCell.setBackgroundColor(primaryBlue);
            tagCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tagCell.setPaddingTop(22);
            tagCell.setPaddingBottom(18);
            tagCell.setPaddingRight(10);
            headerTable.addCell(tagCell);

            doc.add(headerTable);
            doc.add(Chunk.NEWLINE);

            // ── Invoice title + meta ─────────────────────────────────────────
            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);
            metaTable.setWidths(new float[]{1.4f, 1});
            metaTable.setSpacingBefore(10);

            // Left: invoice number + date
            PdfPCell leftMeta = new PdfPCell();
            leftMeta.setBorder(Rectangle.NO_BORDER);
            leftMeta.setPaddingBottom(6);

            Paragraph invTitle = new Paragraph("Invoice", titleFont);
            invTitle.setSpacingAfter(4);
            leftMeta.addElement(invTitle);

            Paragraph invNum = new Paragraph();
            invNum.add(new Chunk("Invoice No:  ", labelFont));
            invNum.add(new Chunk(invoiceNumber, new Font(Font.HELVETICA, 10, Font.BOLD, primaryBlue)));
            leftMeta.addElement(invNum);

            Paragraph invDate = new Paragraph();
            invDate.add(new Chunk("Issue Date:  ", labelFont));
            invDate.add(new Chunk(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")), valueFont));
            leftMeta.addElement(invDate);

            Paragraph bkRef = new Paragraph();
            bkRef.add(new Chunk("Booking Ref: ", labelFont));
            bkRef.add(new Chunk(booking.getBookingReference() != null ? booking.getBookingReference() : "—", valueFont));
            leftMeta.addElement(bkRef);

            metaTable.addCell(leftMeta);

            // Right: status badge
            PdfPCell rightMeta = new PdfPCell();
            rightMeta.setBorder(Rectangle.NO_BORDER);
            rightMeta.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPTable badgeTable = new PdfPTable(1);
            badgeTable.setWidthPercentage(60);
            badgeTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell badge = new PdfPCell(new Phrase("PAID", new Font(Font.HELVETICA, 13, Font.BOLD, Color.WHITE)));
            badge.setBackgroundColor(accentGreen);
            badge.setHorizontalAlignment(Element.ALIGN_CENTER);
            badge.setPadding(8);
            badge.setBorder(Rectangle.NO_BORDER);
            badgeTable.addCell(badge);

            rightMeta.addElement(badgeTable);
            metaTable.addCell(rightMeta);

            doc.add(metaTable);

            // ── Section: Bill To ─────────────────────────────────────────────
            addSectionHeader(doc, "Bill To", headingFont, primaryBlue, lightBlue);

            PdfPTable billTable = new PdfPTable(2);
            billTable.setWidthPercentage(100);
            billTable.setSpacingBefore(6);
            billTable.setSpacingAfter(10);

            String fullName = buildFullName(user);
            addInfoRow(billTable, "Name",     fullName,              labelFont, valueFont, borderGray, Color.WHITE, rowAlt, true);
            addInfoRow(billTable, "Username", user.getUsername(),    labelFont, valueFont, borderGray, rowAlt, Color.WHITE, false);
            addInfoRow(billTable, "Email",    user.getEmail() != null ? user.getEmail() : "—", labelFont, valueFont, borderGray, Color.WHITE, rowAlt, true);
            if (user.getPhone() != null && !user.getPhone().isBlank()) {
                addInfoRow(billTable, "Phone", user.getPhone(), labelFont, valueFont, borderGray, rowAlt, Color.WHITE, false);
            }
            doc.add(billTable);

            // ── Section: Booking Details ─────────────────────────────────────
            addSectionHeader(doc, "Booking Details", headingFont, primaryBlue, lightBlue);

            PdfPTable bookingTable = new PdfPTable(2);
            bookingTable.setWidthPercentage(100);
            bookingTable.setSpacingBefore(6);
            bookingTable.setSpacingAfter(10);

            String bookingType = (booking.getAccommodationId() != null) ? "Accommodation" : "Transport";
            addInfoRow(bookingTable, "Type",          bookingType,                         labelFont, valueFont, borderGray, Color.WHITE, rowAlt, true);
            addInfoRow(bookingTable, "Check-in",      formatDate(booking.getCheckIn()),    labelFont, valueFont, borderGray, rowAlt, Color.WHITE, false);
            addInfoRow(bookingTable, "Check-out",     formatDate(booking.getCheckOut()),   labelFont, valueFont, borderGray, Color.WHITE, rowAlt, true);
            addInfoRow(bookingTable, "Guests",        String.valueOf(booking.getGuestsCount()), labelFont, valueFont, borderGray, rowAlt, Color.WHITE, false);
            addInfoRow(bookingTable, "Booking Status","CONFIRMED",                         labelFont, valueFont, borderGray, Color.WHITE, rowAlt, true);
            doc.add(bookingTable);

            // ── Section: Payment Summary ─────────────────────────────────────
            addSectionHeader(doc, "Payment Summary", headingFont, primaryBlue, lightBlue);

            PdfPTable payTable = new PdfPTable(2);
            payTable.setWidthPercentage(100);
            payTable.setSpacingBefore(6);
            payTable.setSpacingAfter(10);

            String payDateStr = payment.getPaymentDate() != null
                    ? payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
                    : LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));

            addInfoRow(payTable, "Payment Date",   payDateStr,              labelFont, valueFont, borderGray, Color.WHITE, rowAlt, true);
            addInfoRow(payTable, "Payment Status", "COMPLETED",             labelFont, valueFont, borderGray, rowAlt, Color.WHITE, false);
            doc.add(payTable);

            // ── Total amount box ─────────────────────────────────────────────
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setSpacingBefore(16);
            totalTable.setSpacingAfter(20);

            PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL AMOUNT", totalFont));
            totalLabelCell.setBackgroundColor(primaryBlue);
            totalLabelCell.setBorder(Rectangle.NO_BORDER);
            totalLabelCell.setPadding(14);
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalTable.addCell(totalLabelCell);

            BigDecimal amount = payment.getAmount() != null ? payment.getAmount() : booking.getTotalPrice();
            PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("%.2f KM", amount), totalFont));
            totalValueCell.setBackgroundColor(primaryBlue);
            totalValueCell.setBorder(Rectangle.NO_BORDER);
            totalValueCell.setPadding(14);
            totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalValueCell);

            doc.add(totalTable);

            // ── Footer ───────────────────────────────────────────────────────
            canvas.setColorFill(new Color(243, 244, 246));
            canvas.rectangle(50, doc.bottom(), doc.right() - doc.left(), 38);
            canvas.fill();

            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            footerTable.setSpacingBefore(4);

            PdfPCell footerCell = new PdfPCell();
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerCell.setBackgroundColor(new Color(243, 244, 246));
            footerCell.setPadding(10);

            Paragraph footerText = new Paragraph("Thank you for choosing TravelEase! This is an automatically generated invoice.", smallGray);
            footerText.setAlignment(Element.ALIGN_CENTER);
            footerCell.addElement(footerText);

            Paragraph footerContact = new Paragraph("support@travelease.ba  |  travelease.ba", smallGray);
            footerContact.setAlignment(Element.ALIGN_CENTER);
            footerCell.addElement(footerContact);

            footerTable.addCell(footerCell);
            doc.add(footerTable);

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    private void addSectionHeader(Document doc, String title, Font font, Color bgColor, Color lightBg) throws DocumentException {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(14);
        PdfPCell cell = new PdfPCell(new Phrase(title, font));
        cell.setBackgroundColor(lightBg);
        cell.setPadding(8);
        cell.setPaddingLeft(12);
        cell.setBorderColor(bgColor);
        cell.setBorderWidthLeft(4);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        t.addCell(cell);
        doc.add(t);
    }

    private void addInfoRow(PdfPTable table, String label, String value,
                            Font labelFont, Font valueFont, Color border,
                            Color bg1, Color bg2, boolean isOdd) {
        PdfPCell lCell = new PdfPCell(new Phrase(label, labelFont));
        lCell.setBackgroundColor(isOdd ? bg1 : bg2);
        lCell.setPadding(8);
        lCell.setPaddingLeft(12);
        lCell.setBorderColor(border);
        table.addCell(lCell);

        PdfPCell vCell = new PdfPCell(new Phrase(value != null ? value : "—", valueFont));
        vCell.setBackgroundColor(isOdd ? bg1 : bg2);
        vCell.setPadding(8);
        vCell.setPaddingLeft(12);
        vCell.setBorderColor(border);
        table.addCell(vCell);
    }

    private String buildFullName(User user) {
        String first = user.getFirstName() != null ? user.getFirstName() : "";
        String last  = user.getLastName()  != null ? user.getLastName()  : "";
        String full  = (first + " " + last).trim();
        return full.isBlank() ? user.getUsername() : full;
    }

    private String formatDate(Object date) {
        if (date == null) return "—";
        return date.toString();
    }
}
