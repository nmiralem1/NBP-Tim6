package ba.unsa.etf.nbp_tim6.service.abstraction;

import ba.unsa.etf.nbp_tim6.model.City;
import java.util.List;

public interface CityService {
    List<City> getAllCities();

    City getCityById(Integer id);

    List<City> getCitiesByCountry(Integer countryId);

    void createCity(City city);

    void updateCity(City city);

    void deleteCity(Integer id);
}
