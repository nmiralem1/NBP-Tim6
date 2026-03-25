package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.Country;
import ba.unsa.etf.nbp_tim6.repository.abstraction.CountryRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountryById(Integer id) {
        Country country = countryRepository.findById(id);
        if (country == null) {
            throw new RuntimeException("Country not found!");
        }
        return country;
    }

    @Override
    public void createCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public void updateCountry(Country country) {
        countryRepository.update(country);
    }

    @Override
    public void deleteCountry(Integer id) {
        countryRepository.delete(id);
    }
}