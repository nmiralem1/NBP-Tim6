package ba.unsa.etf.nbp_tim6.repository.abstraction;

import ba.unsa.etf.nbp_tim6.model.City;
import java.util.List;

public interface CityRepository {
    List<City> findAll();

    City findById(Integer id);

    List<City> findByCountryId(Integer countryId);

    int save(City city);

    int update(City city);

    int delete(Integer id);
}
