package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Booking;

public interface BookingRepository {

    int save(Booking booking);

    Integer saveAndReturnId(Booking booking);

    int updateStatus(Integer id, String status);

    Booking findById(Integer id);

    java.util.List<Booking> findByUserId(Integer userId);

    java.util.List<Booking> findByTripId(Integer tripId);

    int update(Booking booking);

    int delete(Integer id);
}