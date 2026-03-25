package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Accommodation;

import java.util.List;

public interface AccommodationService {
    List<Accommodation> getAllAccommodations();

    List<Accommodation> getAccommodationsByCityId(Integer cityId);

    Accommodation getAccommodationById(Integer id);

    void createAccommodation(Accommodation accommodation);

    void updateAccommodation(Accommodation accommodation);

    void deleteAccommodation(Integer id);
}