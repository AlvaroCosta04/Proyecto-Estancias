package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Continent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinentRepository extends JpaRepository<Continent, Integer> {

    List<Continent> findAllByOrderByNameAsc();

    Continent findByName(String name);

    @Query(value = "SELECT * FROM continent ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Continent findRandomContinent();

}
