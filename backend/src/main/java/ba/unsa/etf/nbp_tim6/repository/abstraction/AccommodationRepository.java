package ba.unsa.etf.nbp_tim6.repository.abstraction;

import java.math.BigDecimal;

public interface AccommodationRepository {

    BigDecimal getPricePerNight(Integer accommodationId);
}
