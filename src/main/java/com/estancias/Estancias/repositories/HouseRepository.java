package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.House;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {

    @Query("SELECT h FROM House h WHERE h.id = :id")
    Optional<House> findById(@Param("id") Integer id);

    /* @Modifying
    @Query("UPDATE House h SET h.street = :street, h.number = :number, h.postalCode = :postalCode, h.city = :city, h.country = :country, h.houseType = :houseType WHERE h.id = :id")
    void updateHouse(@Param("id") Integer id, @Param("street") String street, @Param("number") int number, @Param("postalCode") String postalCode, @Param("city") String city, @Param("country") String country, @Param("houseType") String houseType);
     */
    @Query("SELECT h FROM House h WHERE h.street = :street AND h.number = :number AND h.city = :city")
    Optional<House> findAddress(@Param("street") String street, @Param("number") int number, @Param("city") String city);

    @Query("SELECT h FROM House h WHERE h.country = :country AND h.city = :city")
    List<House> findByCity(@Param("country") String country, @Param("city") String city);

    @Query("SELECT h FROM House h WHERE h.country = :country")
    List<House> findByCountry(@Param("country") String country);

    @Query("SELECT h FROM House h WHERE h.houseType = :houseType")
    List<House> findByHouseType(@Param("houseType") String houseType);

    @Query(value = "SELECT * FROM house ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<House> find5RandomHouses();
}
