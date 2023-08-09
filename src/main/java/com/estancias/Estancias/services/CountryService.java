package com.estancias.Estancias.services;

import com.estancias.Estancias.entities.Country;
import com.estancias.Estancias.repositories.CountryRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Country getCountryById(int id) {
        return countryRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Country findByName(String name) {
        return countryRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Country> searchCountryByContinent(String continent) {
        return countryRepository.findByContinent(continent);
    }

    @Transactional(readOnly = true)
    public List<Country> getAllCountriesOrderedByName() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<String> getAllCountriesNameOrderAsc() {
        List<Country> countries = countryRepository.findAllByOrderByNameAsc();
        List<String> countriesName = new ArrayList<>();
        for (Country country : countries) {
            countriesName.add(country.getName());
        }
        return countriesName;
    }

    @Transactional(readOnly = true)
    public Country getRandomCountry() {
        return countryRepository.findRandomCountry();
    }

    @Transactional(readOnly = true)
    public List<Country> find5RandomsCountries() {
        return countryRepository.find5RandomsCountries();
    }

    @Transactional
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Transactional
    public void deleteCountryById(int id) {
        countryRepository.deleteById(id);
    }

}
