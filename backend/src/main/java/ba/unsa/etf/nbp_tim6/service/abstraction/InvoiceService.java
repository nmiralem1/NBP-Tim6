package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.model.Invoice;
import ba.unsa.etf.nbp_tim6.model.Payment;
import ba.unsa.etf.nbp_tim6.model.User;

import java.util.List;

public interface InvoiceService {
    Invoice generateAndSave(Payment payment, Booking booking, User user);
    byte[] getPdfByBookingId(Integer bookingId);
    byte[] getPdfById(Integer invoiceId);
    Invoice getByBookingId(Integer bookingId);
    List<Invoice> getByUserId(Integer userId);
}
