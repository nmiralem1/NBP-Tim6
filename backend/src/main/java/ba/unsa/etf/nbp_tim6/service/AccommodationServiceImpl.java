package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Accommodation;
import ba.unsa.etf.nbp_tim6.repository.abstraction.AccommodationRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.AccommodationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public AccommodationServiceImpl(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }

    @Override
    public List<Accommodation> getAccommodationsByCityId(Integer cityId) {
        return accommodationRepository.findByCityId(cityId);
    }

    @Override
    public Accommodation getAccommodationById(Integer id) {
        Accommodation acc = accommodationRepository.findById(id);
        if (acc == null) {
            throw new RuntimeException("Accommodation not found!");
        }
        return acc;
    }

    @Override
    public void createAccommodation(Accommodation accommodation) {
        accommodationRepository.save(accommodation);
    }

    @Override
    public void updateAccommodation(Accommodation accommodation) {
        accommodationRepository.update(accommodation);
    }

    @Override
    public void deleteAccommodation(Integer id) {
        accommodationRepository.delete(id);
    }
}