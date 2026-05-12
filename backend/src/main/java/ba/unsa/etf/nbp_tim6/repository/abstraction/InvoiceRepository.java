package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Invoice;

import java.util.List;

public interface InvoiceRepository {
    List<Invoice> findAll();
    Invoice findById(Integer id);
    Invoice findByBookingId(Integer bookingId);
    Invoice findByPaymentId(Integer paymentId);
    List<Invoice> findByUserId(Integer userId);
    Integer save(Invoice invoice);
    int delete(Integer id);
}
