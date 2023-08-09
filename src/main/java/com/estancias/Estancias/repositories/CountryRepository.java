package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Country;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findAll(); //obtener todos los países

    List<Country> findByContinent(String continent); //buscar países por continente

    @Query(value = "SELECT c FROM Country c WHERE c.name = :name")
    Country findByName(String name);

    List<Country> findAllByOrderByNameAsc();

    @Query(value = "SELECT * FROM country ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Country findRandomCountry();

    @Query(value = "SELECT * FROM country ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    List<Country> find5RandomsCountries();

    /*@Query(value = "SELECT DISTINCT co  FROM Country co  JOIN co.house h ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Country> find5RandomCountriesWithHouses();*/
}
