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

        if (trip.getStartDate().isAfter(trip.getEndDate())) {
            throw new RuntimeException("Start date must be before end date!");
        }

        tripRepository.save(trip);
    }

    public List<Trip> getTripsByUser(Integer userId) {
        return tripRepository.findByUserId(userId);
    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Integer id) {
        Trip trip = tripRepository.findById(id);
        if (trip == null) {
            throw new RuntimeException("Trip not found with id: " + id);
        }
        return trip;
    }

    public void updateTrip(Trip trip) {
        if (trip.getStartDate().isAfter(trip.getEndDate())) {
            throw new RuntimeException("Start date must be before end date!");
        }
        tripRepository.update(trip);
    }

    public void deleteTrip(Integer id) {
        tripRepository.delete(id);
    }
}