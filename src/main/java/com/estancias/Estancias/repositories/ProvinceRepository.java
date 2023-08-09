package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Province;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    List<Province> findAll(); //obtener todos los países

    @Query("SELECT p FROM Province p WHERE p.name = :name")
    Province findByName(@Param("name") String name);

    @Query("SELECT p FROM Province p WHERE p.name LIKE %:name%")
    List<Province> findByNameContaining(@Param("name") String name);

    List<Province> findAllByOrderByNameAsc();

    @Query(value = "SELECT * FROM province ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Province findRandomProvince();

    @Query(value = "SELECT * FROM province ORDER BY RAND() LIMIT 5;", nativeQuery = true)
    List<Province> find5RandomsProvinces();
}
