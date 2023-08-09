package com.estancias.Estancias.services;

import com.estancias.Estancias.entities.Continent;
import com.estancias.Estancias.repositories.ContinentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContinentService {

    @Autowired
    private ContinentRepository continentRepository;

    @Transactional(readOnly = true)
    public List<Continent> getAllContinents() {
        return continentRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Continent getContinentByName(String name) {
        return continentRepository.findByName(name);
    }
    
    @Transactional(readOnly = true)
    public Continent getContinentById(Integer id) {
        return continentRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Continent getRandomContinent() {
        return continentRepository.findRandomContinent();
    }

    @Transactional
    public void saveContinent(Continent continent) {
        continentRepository.save(continent);
    }

    @Transactional
    public void deleteContinentById(int id) {
        continentRepository.deleteById(id);
    }

}
