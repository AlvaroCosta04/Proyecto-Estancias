package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Acceptance;
import com.estancias.Estancias.repositories.ReserveRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;

    @Transactional
    public void create(Reserve reserve) {
        reserveRepository.save(reserve);
    }

    @Transactional
    public void update(Reserve reserve, Integer id) {
        reserve.setId(id);
        reserveRepository.save(reserve);
    }

    @Transactional
    public void delete(@NotNull Integer id) throws ServicesException {
        Reserve reserve = findById(id);
        reserveRepository.delete(reserve);
    }

    //read
    @Transactional(readOnly = true)
    public List<Reserve> getAll() {
        return reserveRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reserve> findAllReservesExceptFinalizado() {
        return reserveRepository.findAllReservesExceptFinalizado();
    }

    @Transactional(readOnly = true)
    public List<Reserve> findAllReservesByCountryName(String country) {
        return reserveRepository.findAllReservesByCountryName(country);
    }

    @Transactional(readOnly = true)
    public List<Reserve> findAllReservesByTypeOfHome(String houseType) {
        return reserveRepository.findAllReservesByTypeOfHome(houseType);
    }

    @Transactional(readOnly = true)
    public List<Reserve> findAllReservesByProvinceName(String province) {
        return reserveRepository.findAllReservesByProvinceName(province);
    }

    @Transactional(readOnly = true)
    public List<Reserve> filterByPrice(Double priceMin, Double priceMax) {
        return reserveRepository.filterByPrice(priceMin, priceMax);
    }

    @Transactional(readOnly = true)
    public List<Reserve> findByStay(Integer minDays, Integer maxDays) {
        return reserveRepository.findByStay(minDays, maxDays);
    }

    @Transactional(readOnly = true)
    public Reserve findByOwner(Users owner) throws ServicesException {
        return reserveRepository.findByOwner(owner).orElseThrow(() -> new ServicesException("No se ha encontrado una reserva"));
    }

    @Transactional(readOnly = true)
    public Reserve findById(@NotNull Integer id) throws ServicesException {
        return reserveRepository.findById(id).orElseThrow(() -> new ServicesException("No se ha encontrado una reserva"));
    }

    @Transactional(readOnly = true)
    public Reserve findReserveById(@NotNull Integer id) throws ServicesException {
        return reserveRepository.findReserveById(id);
    }

    @Transactional(readOnly = true)
    public List<Reserve> findReservesByCriteriaAndDates(
            String provinceName, String houseType, double precioMin,
            double precioMax, Date startDate, Date endDate) {

         List<Date> occupiedDates = getDatesBetween(startDate, endDate);
        
        return reserveRepository.findAllReservesByHomeAndPriceRangeAndDates(
                provinceName, houseType, precioMin, precioMax, occupiedDates);
    }

    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            datesInRange.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }

    @Transactional(readOnly = true)
    public List<Reserve> findReservesByTenant(Users tenant) {
        return reserveRepository.findReservesByTenant(tenant);
    }

    @Transactional(readOnly = true)
    public List<Reserve> findReservesByOwner(Users owner) {
        return reserveRepository.findReservesByOwner(owner);
    }
}
