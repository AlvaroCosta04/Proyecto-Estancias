package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.id = :id")
    Optional<Review> findById(@Param("id") Integer id);

    @Query("SELECT r FROM Review r WHERE r.content = :content")
    Optional<Review> findByContent(@Param("content") String content);

    @Query("SELECT r FROM Review r ORDER BY r.score DESC")
    List<Review> orderByScore();

    @Query("SELECT COALESCE(AVG(r.score), 0) FROM Review r WHERE r.reserve.id = :reserveId")
    Double getGeneralScore(@Param("reserveId") Integer reserveId);
}
