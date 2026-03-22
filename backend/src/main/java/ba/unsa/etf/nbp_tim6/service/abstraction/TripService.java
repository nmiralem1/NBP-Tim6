package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Trip;

import java.util.List;

public interface TripService {

    void createTrip(Trip trip);

    List<Trip> getTripsByUser(Integer userId);
}
