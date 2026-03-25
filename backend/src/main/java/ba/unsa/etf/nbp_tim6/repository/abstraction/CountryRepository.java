package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> findAll();

    Country findById(Integer id);

    int save(Country country);

    int update(Country country);

    int delete(Integer id);
}