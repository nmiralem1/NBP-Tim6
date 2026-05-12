package ba.unsa.etf.nbp_tim6.repository;

import ba.unsa.etf.nbp_tim6.model.Invoice;
import ba.unsa.etf.nbp_tim6.repository.abstraction.InvoiceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public InvoiceRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Invoice> rowMapper = (rs, rowNum) -> {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getObject("ID", Integer.class));
        invoice.setPaymentId(rs.getObject("PAYMENT_ID", Integer.class));
        invoice.setBookingId(rs.getObject("BOOKING_ID", Integer.class));
        invoice.setUserId(rs.getObject("USER_ID", Integer.class));
        invoice.setInvoiceNumber(rs.getString("INVOICE_NUMBER"));
        invoice.setIssuedAt(rs.getTimestamp("ISSUED_AT") == null ? null : rs.getTimestamp("ISSUED_AT").toLocalDateTime());
        invoice.setTotalAmount(rs.getBigDecimal("TOTAL_AMOUNT"));
        return invoice;
    };

    @Override
    public List<Invoice> findAll() {
        return jdbcTemplate.query(
                "SELECT ID, PAYMENT_ID, BOOKING_ID, USER_ID, INVOICE_NUMBER, ISSUED_AT, TOTAL_AMOUNT FROM NBPT6.INVOICES",
                rowMapper);
    }

    @Override
    public Invoice findById(Integer id) {
        List<Invoice> results = jdbcTemplate.query(
                "SELECT ID, PAYMENT_ID, BOOKING_ID, USER_ID, INVOICE_NUMBER, ISSUED_AT, TOTAL_AMOUNT FROM NBPT6.INVOICES WHERE ID = ?",
                rowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Invoice findByBookingId(Integer bookingId) {
        List<Invoice> results = jdbcTemplate.query(
                "SELECT ID, PAYMENT_ID, BOOKING_ID, USER_ID, INVOICE_NUMBER, ISSUED_AT, TOTAL_AMOUNT FROM NBPT6.INVOICES WHERE BOOKING_ID = ?",
                rowMapper, bookingId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Invoice findByPaymentId(Integer paymentId) {
        List<Invoice> results = jdbcTemplate.query(
                "SELECT ID, PAYMENT_ID, BOOKING_ID, USER_ID, INVOICE_NUMBER, ISSUED_AT, TOTAL_AMOUNT FROM NBPT6.INVOICES WHERE PAYMENT_ID = ?",
                rowMapper, paymentId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Invoice> findByUserId(Integer userId) {
        return jdbcTemplate.query(
                "SELECT ID, PAYMENT_ID, BOOKING_ID, USER_ID, INVOICE_NUMBER, ISSUED_AT, TOTAL_AMOUNT FROM NBPT6.INVOICES WHERE USER_ID = ? ORDER BY ISSUED_AT DESC",
                rowMapper, userId);
    }

    @Override
    public Integer save(Invoice invoice) {
        jdbcTemplate.update(
                "INSERT INTO NBPT6.INVOICES (PAYMENT_ID, BOOKING_ID, USER_ID, INVOICE_NUMBER, ISSUED_AT, TOTAL_AMOUNT, PDF_DATA) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                invoice.getPaymentId(),
                invoice.getBookingId(),
                invoice.getUserId(),
                invoice.getInvoiceNumber(),
                invoice.getTotalAmount(),
                invoice.getPdfData());

        return jdbcTemplate.queryForObject(
                "SELECT ID FROM NBPT6.INVOICES WHERE INVOICE_NUMBER = ?",
                Integer.class,
                invoice.getInvoiceNumber());
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM NBPT6.INVOICES WHERE ID = ?", id);
    }

    public byte[] findPdfById(Integer id) {
        List<byte[]> results = jdbcTemplate.query(
                "SELECT PDF_DATA FROM NBPT6.INVOICES WHERE ID = ?",
                (rs, rowNum) -> rs.getBytes("PDF_DATA"),
                id);
        return results.isEmpty() ? null : results.get(0);
    }

    public byte[] findPdfByBookingId(Integer bookingId) {
        List<byte[]> results = jdbcTemplate.query(
                "SELECT PDF_DATA FROM NBPT6.INVOICES WHERE BOOKING_ID = ?",
                (rs, rowNum) -> rs.getBytes("PDF_DATA"),
                bookingId);
        return results.isEmpty() ? null : results.get(0);
    }
}
