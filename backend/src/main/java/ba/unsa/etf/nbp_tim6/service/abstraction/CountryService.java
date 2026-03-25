package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();

    Country getCountryById(Integer id);

    void createCountry(Country country);

    void updateCountry(Country country);

    void deleteCountry(Integer id);
}