package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Acceptance;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Integer> {

    @Query("SELECT r FROM Reserve r WHERE r.id = :id")
    Optional<Reserve> findById(@Param("id") Integer id);

    @Query("SELECT r FROM Reserve r WHERE r.id = :id")
    Reserve findReserveById(@Param("id") Integer id);

    @Query("SELECT r FROM Reserve r WHERE r.acceptance != 'FINALIZADO'")
    List<Reserve> findAllReservesExceptFinalizado();

    @Query("SELECT r FROM Reserve r WHERE r.house.country.name = :countryName")
    List<Reserve> findAllReservesByCountryName(@Param("countryName") String countryName);

    @Query("SELECT r FROM Reserve r WHERE r.house.province.name = :provinceName")
    List<Reserve> findAllReservesByProvinceName(@Param("provinceName") String provinceName);

    @Query("SELECT r FROM Reserve r WHERE r.house.houseType = :houseType")
    List<Reserve> findAllReservesByTypeOfHome(@Param("houseType") String houseType);

    @Query("SELECT r FROM Reserve r WHERE r.owner = :owner")
    Optional<Reserve> findByOwner(@Param("owner") Users owner);

    @Query("SELECT r FROM Reserve r WHERE r.price BETWEEN :price1 AND :price2")
    List<Reserve> filterByPrice(@Param("price1") Double price1, @Param("price2") Double price2);

    @Query("SELECT r FROM Reserve r WHERE r.minDays = :minDays AND r.maxDays = :maxDays")
    List<Reserve> findByStay(@Param("minDays") Integer minDays, @Param("maxDays") Integer maxDays);

    @Query("SELECT r FROM Reserve r JOIN r.bookings b WHERE b.tenant = :tenant AND b.available = true")
    List<Reserve> findReservesByTenant(@Param("tenant") Users tenant);

    @Query("SELECT r FROM Reserve r WHERE r.owner = :owner")
    List<Reserve> findReservesByOwner(@Param("owner") Users owner);

    @Query("SELECT DISTINCT r FROM Reserve r "
            + "WHERE r.acceptance != 'FINALIZADO' "
            + "AND r.house.province.name = :provinceName "
            + "AND r.house.houseType = :houseType "
            + "AND r.price BETWEEN :precioMin AND :precioMax "
            + "AND NOT EXISTS ("
            + "    SELECT 1 FROM Reserve r2 "
            + "    JOIN r2.occupiedDates d "
            + "    WHERE r2 = r "
            + "    AND d IN :occupiedDates"
            + ")"
    )
    List<Reserve> findAllReservesByHomeAndPriceRangeAndDates(
            @Param("provinceName") String provinceName,
            @Param("houseType") String houseType,
            @Param("precioMin") double precioMin,
            @Param("precioMax") double precioMax,
            @Param("occupiedDates") List<Date> occupiedDates);


    /*@Query("SELECT r FROM Reserve r "
            + "WHERE r.acceptance = 'PUBLICADO' "
            + "AND r.house.province.name = :provinceName "
            + "AND r.house.houseType = :houseType "
            + "AND r.price BETWEEN :precioMin AND :precioMax ")
    List<Reserve> findAllReservesByProvinceHouseTypePriceRange(
            @Param("provinceName") String provinceName,
            @Param("houseType") String houseType,
            @Param("precioMin") double precioMin,
            @Param("precioMax") double precioMax);*/
}
