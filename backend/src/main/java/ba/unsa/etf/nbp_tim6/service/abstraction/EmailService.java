package ba.unsa.etf.nbp_tim6.service.abstraction;

public interface EmailService {
    public void sendBookingConfirmation(String toEmail, String fullName, String bookingId, String amount);
    }
