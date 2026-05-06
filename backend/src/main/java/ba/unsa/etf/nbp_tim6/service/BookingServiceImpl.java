package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.dto.BookingCreatedDto;
import ba.unsa.etf.nbp_tim6.model.Accommodation;
import ba.unsa.etf.nbp_tim6.model.Booking;
import ba.unsa.etf.nbp_tim6.model.Transport;
import ba.unsa.etf.nbp_tim6.model.TripCity;
import ba.unsa.etf.nbp_tim6.model.User;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.BookingRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TransportRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripCityRepository;
import ba.unsa.etf.nbp_tim6.repository.abstraction.UserRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final TransportRepository transportRepository;
    private final UserRepository userRepository;
    private final TripCityRepository tripCityRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            AccommodationRepository accommodationRepository,
            TransportRepository transportRepository,
            UserRepository userRepository,
            TripCityRepository tripCityRepository) {
        this.bookingRepository = bookingRepository;
        this.accommodationRepository = accommodationRepository;
        this.transportRepository = transportRepository;
        this.userRepository = userRepository;
        this.tripCityRepository = tripCityRepository;
    }

    @Transactional
    public BookingCreatedDto createBooking(Booking booking) {
        boolean accommodationBooking = booking.getAccommodationId() != null;
        boolean transportBooking = booking.getTransportId() != null;

        if (accommodationBooking == transportBooking) {
            throw new RuntimeException("Booking must contain exactly one accommodation or transport!");
        }

        BigDecimal total;

        if (accommodationBooking) {
            long days = ChronoUnit.DAYS.between(
                    booking.getCheckIn(),
                    booking.getCheckOut());

            if (days <= 0) {
                throw new RuntimeException("Check-out must be after check-in!");
            }

            BigDecimal pricePerNight = accommodationRepository.getPricePerNight(booking.getAccommodationId());
            total = pricePerNight.multiply(BigDecimal.valueOf(days));
        } else {
            if (booking.getGuestsCount() == null || booking.getGuestsCount() < 1) {
                throw new RuntimeException("At least one traveler is required!");
            }

            Transport transport = transportRepository.findById(booking.getTransportId());
            if (transport == null) {
                throw new RuntimeException("Transport not found!");
            }

            total = transport.getTicketPrice().multiply(BigDecimal.valueOf(booking.getGuestsCount()));
            if (booking.getTripId() == null) {
                booking.setTripId(transport.getTripId());
            }
        }

        booking.setTotalPrice(total);

        // Set default status
        booking.setBookingStatus("pending");

        // Generate booking reference
        String ref = UUID.randomUUID().toString();
        booking.setBookingReference(ref);

        // Save and return generated ID
        Integer bookingId = bookingRepository.saveAndReturnId(booking);
        if (accommodationBooking) {
            addAccommodationCityToTrip(booking);
        }
        return new BookingCreatedDto(bookingId, total, ref);
    }

    private void addAccommodationCityToTrip(Booking booking) {
        if (booking.getTripId() == null) {
            return;
        }

        Accommodation accommodation = accommodationRepository.findById(booking.getAccommodationId());
        if (accommodation == null || accommodation.getCityId() == null) {
            return;
        }

        boolean alreadyInTrip = tripCityRepository.findByTripId(booking.getTripId()).stream()
                .anyMatch(tripCity -> accommodation.getCityId().equals(tripCity.getCityId()));

        if (alreadyInTrip) {
            return;
        }

        TripCity tripCity = new TripCity();
        tripCity.setTripId(booking.getTripId());
        tripCity.setCityId(accommodation.getCityId());
        tripCity.setArrivalDate(booking.getCheckIn());
        tripCity.setDepartureDate(booking.getCheckOut());
        tripCity.setNotes("Stay added from hotel booking");

        tripCityRepository.save(tripCity);
    }

    @Override
    public Booking getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }
        return booking;
    }

    @Override
    public java.util.List<Booking> getBookingsByUserId(Integer userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public void updateBooking(Booking booking) {
        bookingRepository.update(booking);
    }

    @Override
    public void deleteBooking(Integer id) {
        bookingRepository.delete(id);
    }

    @Override
    public List<Booking> getBookingsForAuthenticatedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found!"));
        return bookingRepository.findByUserId(user.getId());
    }

    @Override
    public void updateBookingStatus(Integer bookingId, String status) {
        bookingRepository.updateStatus(bookingId, status);
    }

    @Override
    public java.util.List<Booking> getBookingsByTripId(Integer tripId) {
        return bookingRepository.findByTripId(tripId);
    }
}
