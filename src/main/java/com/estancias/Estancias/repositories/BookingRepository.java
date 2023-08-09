package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Booking;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findById(Integer id);

    List<Booking> findByTenant(Users tenant);

    //Busca todas las reservas que tienen una fecha de inicio entre un rango determinado.
    List<Booking> findByStartDateBetween(Date start, Date end);

    // Busca todas las reservas que tienen una fecha de fin entre un rango determinado.
    List<Booking> findByEndDateBetween(Date start, Date end);

    List<Booking> findByStartDateBeforeAndEndDateAfter(Date start, Date end);

    @Query("SELECT b FROM Booking b WHERE b.reserve = :reserve AND b.available = true")
    List<Booking> findAllByReserveAndAvailableIsTrue(@Param("reserve") Reserve reserve);

    @Query("SELECT b FROM Booking b WHERE b.tenant = :tenant AND b.startDate <= :end AND b.endDate >= :start")
    List<Booking> findOverlappingBookings(@Param("tenant") Users tenant, @Param("start") Date start, @Param("end") Date end);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.reserve = :reserve "
            + "AND b.tenant = :tenant "
            + "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    List<Booking> findBookingByTenant(
            @Param("tenant") Users tenant,
            @Param("reserve") Reserve reserve);

}
