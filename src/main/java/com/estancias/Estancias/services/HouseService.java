package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Image;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.repositories.HouseRepository;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ImageService imageService;

    /* @Transactional
    public void create(House house) {
        houseRepository.save(house);
    }

    @Transactional
    public void update(House house, Integer id) {
        house.setId(id);
        houseRepository.save(house);

    }

    @Transactional
    public void delete(Integer id) throws ServicesException {
        House house = findById(id);
        houseRepository.delete(house);
    }

    //read
    @Transactional(readOnly = true)
    public House findById(@NotNull Integer id) throws ServicesException {
        return getFromOptional(houseRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public String findImageById(@NotNull Integer id) throws ServicesException {
        return imageService.findById(id);
    }

    @Transactional(readOnly = true)
    public House findAddress(@NotNull String street, int number, String city) throws ServicesException {
        return getFromOptional(houseRepository.findAddress(street, number, city));
    }

    @Transactional(readOnly = true)
    public List<House> findByCity(@NotNull String country, String city) throws ServicesException {
        return houseRepository.findByCity(country, city);
    }

    @Transactional(readOnly = true)
    public List<House> findByCountry(@NotNull String country) throws ServicesException {
        return houseRepository.findByCountry(country);
    }

    @Transactional(readOnly = true)
    public List<House> findByHouseType(@NotNull String houseType) throws ServicesException {
        return houseRepository.findByHouseType(houseType);
    }

    //optional
    private House getFromOptional(Optional<House> houseOpt) throws ServicesException {
        if (houseOpt.isPresent()) {
            return houseOpt.get();
        }
        throw new ServicesException("No se ha encontrado una casa");
    }*/
    @Transactional
    public void create(House house) {
        houseRepository.save(house);
    }

    @Transactional
    public void update(House house, Integer id) throws ServicesException {
        house.setId(id);
        houseRepository.save(house);
    }

    @Transactional
    public void delete(Integer id) throws ServicesException {
        House house = findById(id);
        houseRepository.delete(house);
    }

    @Transactional(readOnly = true)
    public House findById(@NotNull Integer id) throws ServicesException {
        return houseRepository.findById(id).orElseThrow(() -> new ServicesException("House not found with id " + id));
    }

    @Transactional(readOnly = true)
    public String findImageById(@NotNull Integer id) throws ServicesException {
        return imageService.findById(id);
    }

    @Transactional(readOnly = true)
    public House findAddress(@NotNull String street, int number, String city) throws ServicesException {
        return houseRepository.findAddress(street, number, city).orElseThrow(() -> new ServicesException("House not found with address " + street + " " + number + ", " + city));
    }

    @Transactional(readOnly = true)
    public List<House> find5RandomHouses() throws ServicesException {
        return houseRepository.find5RandomHouses();
    }

    @Transactional(readOnly = true)
    public List<House> findByCity(@NotNull String country, String city) throws ServicesException {
        return houseRepository.findByCity(country, city);
    }

    @Transactional(readOnly = true)
    public List<House> findByCountry(@NotNull String country) throws ServicesException {
        return houseRepository.findByCountry(country);
    }

    @Transactional(readOnly = true)
    public List<House> findAll() throws ServicesException {
        return houseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<House> findByHouseType(@NotNull String houseType) throws ServicesException {
        return houseRepository.findByHouseType(houseType);
    }

}
