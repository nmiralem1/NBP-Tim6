package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Booking;

public interface BookingRepository {

    int save(Booking booking);
}