package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Trip;
import ba.unsa.etf.nbp_tim6.repository.abstraction.TripRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.TripService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void createTrip(Trip trip) {

        // poslovna logika
        if (trip.getStartDate().isAfter(trip.getEndDate())) {
            throw new RuntimeException("Start date mora biti prije end date!");
        }

        tripRepository.save(trip);
    }

    public List<Trip> getTripsByUser(Integer userId) {
        return tripRepository.findByUserId(userId);
    }
}