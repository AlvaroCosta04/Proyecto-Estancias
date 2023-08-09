package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Booking;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.repositories.BookingRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Create operation
    @Transactional
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    // Update operation
    @Transactional
    public Booking updateBooking(Integer id, Booking booking) throws ServicesException {
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(() -> new ServicesException("Booking not found with id " + id));
        existingBooking.setStartDate(booking.getStartDate());
        existingBooking.setEndDate(booking.getEndDate());
        existingBooking.setTenant(booking.getTenant());
        return bookingRepository.save(existingBooking);
    }

    // Delete operation
    @Transactional
    public void deleteBooking(Integer id) throws ServicesException {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }

    // Read operation
    public Booking getBookingById(Integer id) throws ServicesException {
        return bookingRepository.findById(id).orElseThrow(() -> new ServicesException("Booking not found with id " + id));
    }

    // Custom methods using repository
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByTenant(Users tenant) {
        return bookingRepository.findByTenant(tenant);
    }

    public List<Booking> getAvailableBookingsBetweenDates(Date startDate, Date endDate) {
        return bookingRepository.findByStartDateBeforeAndEndDateAfter(startDate, endDate);
    }

    public List<Date> getUnavailableDates(Reserve reserve) {
        List<Booking> bookings = reserve.getBookings();
        List<Date> daysNotAvailable = new ArrayList<>();

        for (Booking booking : bookings) {
            Date startDate = booking.getStartDate();
            Date endDate = booking.getEndDate();

            System.out.println(startDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (!calendar.getTime().after(endDate)) {
                daysNotAvailable.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
        }

        return daysNotAvailable;
    }

    public Booking findBookingByTenant(Reserve reserve, Users user) {
        List<Booking> bookingList = bookingRepository.findBookingByTenant(user, reserve);
        if (!bookingList.isEmpty()) {
            return bookingList.get(0);
        } else {
            return null;
        }
    }

    public List<String> getDatesBetweenyyyyMMdd(Reserve reserve) {
        List<Booking> bookings = bookingRepository.findAllByReserveAndAvailableIsTrue(reserve);
        List<String> dates = new ArrayList<>();

        for (Booking booking : bookings) {
            String startDate = booking.getStartDate().toString();
            String endDate = booking.getEndDate().toString();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);

                long interval = 24 * 1000 * 60 * 60;

                long endTime = end.getTime();
                long currentTime = start.getTime();

                while (currentTime <= endTime) {
                    Date currentDate = new Date(currentTime);
                    dates.add(dateFormat.format(currentDate));
                    currentTime += interval;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return dates;
    }

}
