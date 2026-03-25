package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Trip;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrip_InvalidDates_ThrowsException() {
        Trip trip = new Trip();
        trip.setStartDate(LocalDate.of(2023, 10, 10));
        trip.setEndDate(LocalDate.of(2023, 10, 5));

        assertThrows(RuntimeException.class, () -> tripService.createTrip(trip));
        verify(tripRepository, never()).save(any());
    }

    @Test
    void createTrip_ValidDates_SavesTrip() {
        Trip trip = new Trip();
        trip.setStartDate(LocalDate.of(2023, 10, 5));
        trip.setEndDate(LocalDate.of(2023, 10, 10));

        tripService.createTrip(trip);
        verify(tripRepository, times(1)).save(trip);
    }
}
