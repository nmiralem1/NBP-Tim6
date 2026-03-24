package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Accommodation;

import java.math.BigDecimal;
import java.util.List;

public interface AccommodationRepository {

    BigDecimal getPricePerNight(Integer accommodationId);
    List<Accommodation> findAll();
    List<Accommodation> findByCityId(Integer cityId);
}
