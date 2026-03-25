package ba.unsa.etf.nbp_tim6.service;

import ba.unsa.etf.nbp_tim6.model.City;
import ba.unsa.etf.nbp_tim6.repository.abstraction.CityRepository;
import ba.unsa.etf.nbp_tim6.service.abstraction.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCityById(Integer id) {
        City city = cityRepository.findById(id);
        if (city == null) {
            throw new RuntimeException("City not found!");
        }
        return city;
    }

    @Override
    public List<City> getCitiesByCountry(Integer countryId) {
        return cityRepository.findByCountryId(countryId);
    }

    @Override
    public void createCity(City city) {
        cityRepository.save(city);
    }

    @Override
    public void updateCity(City city) {
        cityRepository.update(city);
    }

    @Override
    public void deleteCity(Integer id) {
        cityRepository.delete(id);
    }
}
