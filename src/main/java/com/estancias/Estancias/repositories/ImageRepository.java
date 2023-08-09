package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT i FROM Image i WHERE i.id = :id")
    Optional<Image> findById(@Param("id") Integer id);
}
