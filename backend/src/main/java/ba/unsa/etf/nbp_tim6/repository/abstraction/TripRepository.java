package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Trip;

import java.util.List;

public interface TripRepository {

    int save(Trip trip);

    List<Trip> findByUserId(Integer userId);
}
