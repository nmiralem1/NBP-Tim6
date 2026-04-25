package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.dto.BookingCreatedDto;
import ba.unsa.etf.nbp_tim6.model.Booking;

public interface BookingService {

    BookingCreatedDto createBooking(Booking booking);

    Booking getBookingById(Integer id);

    java.util.List<Booking> getBookingsByUserId(Integer userId);

    void updateBooking(Booking booking);

    void deleteBooking(Integer id);

    java.util.List<Booking> getBookingsForAuthenticatedUser(String username);

    void updateBookingStatus(Integer bookingId, String status);

    java.util.List<Booking> getBookingsByTripId(Integer tripId);
}
