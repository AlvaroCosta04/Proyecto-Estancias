package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Booking;
import com.estancias.Estancias.entities.Notification;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.repositories.BookingRepository;
import com.estancias.Estancias.services.BookingService;
import com.estancias.Estancias.services.NotificationService;
import com.estancias.Estancias.services.ReserveService;
import com.estancias.Estancias.services.UserService;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/booking-reserve")
public class BookingController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService usersService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/booking-{id}")
    public String bookReserve(@PathVariable Integer id, RedirectAttributes redirectAttributes, ModelMap model, @RequestParam(required = false, name = "error") String error) {

        try {
            Reserve reserve = reserveService.findById(id);
            List<Date> unavailableDates = bookingService.getUnavailableDates(reserve);

            model.put("idReserve", id);
            model.put("unavailableDatesView", sortDates(unavailableDates));
            model.put("unavailableDates", unavailableDates);
            model.put("unavailableDates", sortDates(unavailableDates));
            model.put("unavailableDates", bookingService.getDatesBetweenyyyyMMdd(reserve));
            model.put("minDays", reserve.getMinDays());
            model.put("maxDays", reserve.getMaxDays());
            model.put("price", reserve.getPrice());

            System.out.println(bookingService.getDatesBetweenyyyyMMdd(reserve));
            if (error != null) {
                model.addAttribute("error", error);
            }

        } catch (ServicesException ex) {
        }
        return "bookReserve.html";
    }

    public List<String> sortDates(List<Date> dates) {
        dates.sort(Comparator.naturalOrder());
        //SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        List<String> formattedDates = new ArrayList<>();
        for (Date date : dates) {
            formattedDates.add(dateFormatter.format(date));
        }
        return formattedDates;
    }

    @PostMapping("/bookingpost")
    public String bookPost(
            @Valid Reserve reserve,
            @RequestParam("idReserve") Integer id,
            @RequestParam(required = false, name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false, name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            RedirectAttributes redirectAttributes,
            ModelMap model,
            BindingResult result,
            HttpSession session) {

        try {
            Users user = (Users) session.getAttribute("userSession");
            reserve = reserveService.findById(id);

            if (endDate == null || startDate == null) {
                redirectAttributes.addAttribute("error", "Complete los datos antes de enviar el formulario");
                return "redirect:/booking-reserve/booking-" + id;
            }

            if (endDate.before(startDate)) {
                redirectAttributes.addAttribute("error", "la fecha inicial de la reserva es posterior a la fecha final");
                return "redirect:/booking-reserve/booking-" + id;
            }

            /*List<Date> unavailableDates = bookingService.getUnavailableDates(reserve);
            List<Date> daysEntered = getDatesBetween(startDate, endDate);

            if (checkOverlapDates(daysEntered, unavailableDates)) {
                redirectAttributes.addAttribute("error", "Alguna fecha ingresada se solapa con alguna reserva ya existente, porfavor, corobore la lista de las fechas ya reservadas");
                return "redirect:/booking-reserve/booking-" + id;
            }*/
            int days = (int) ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());

            if (days < reserve.getMinDays()) {
                redirectAttributes.addAttribute("error", "No puedes hacer una reserva con una duración de días menor a la establecida");
                return "redirect:/booking-reserve/booking-" + id;
            } else if (days > reserve.getMaxDays()) {
                redirectAttributes.addAttribute("error", "No puedes hacer una reserva con una duración de días mayor a la establecida");
                return "redirect:/booking-reserve/booking-" + id;
            }
            System.out.println("startDate" + startDate);

            Booking booking = new Booking();
            booking.setTenant(user);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setAvailable(false);
            booking.setReserve(reserve);
            bookingService.createBooking(booking);

            reserve.getBookings().add(booking);
            reserveService.update(reserve, id);

            /* List<Notification> notifications = reserveService.findById(id).getOwner().getNotificationsReceived();
            Notification notification = new Notification();

            notification.setSender(user);
            notification.setTitle("Tienes una solicitud en la estancia n° " + id);
            notification.setMessage("MENSAJE" + user.getId());
            notification.setBooking(booking);
            notifications.add(notification);
            notificationService.saveNotification(notification);

            Users userRecived = usersService.findById(reserveService.findById(id).getOwner().getId());
            userRecived.setNotificationsReceived(notifications);
            usersService.update(userRecived);*/
            Users userRecived = usersService.findById(reserveService.findById(id).getOwner().getId());
            String title = "Tienes una solicitud en la estancia n° " + id;
            String message = "MENSAJE" + user.getId();
            createNotification(userRecived, user, title, message, booking);

            //redirectAttributes.addAttribute("success", "Se añadió su reserva de forma exitosa");
            redirectAttributes.addAttribute("success", "Se envió correctamente la soliciud al propietario");
            return "redirect:/home";
        } catch (ServicesException se) {
            model.addAttribute("error", "Hubo un error a la hora de agendar su reserva, por favor intentelo de nuevo");
            return "redirect:/booking-reserve/booking-" + id;
        }
    }

    @PostMapping("/accept")
    public String acceptBooking(
            @RequestParam("id") Integer id,
            @RequestParam("idNotification") Integer idNotification,
            RedirectAttributes redirectAttributes,
            ModelMap model, HttpSession session) {
        Users user = (Users) session.getAttribute("userSession");

        try {
            Booking booking = bookingService.getBookingById(id);
            booking.setAvailable(true);
            bookingRepository.save(booking);
            
            Reserve reserve = booking.getReserve();
            List<Date> occupiedDates = reserveService.getDatesBetween(booking.getStartDate(), booking.getEndDate());
            reserve.getOccupiedDates().addAll(occupiedDates);
            reserveService.update(reserve, reserve.getId());

            List<Notification> notificationsOwner = user.getNotificationsReceived();
            notificationsOwner.removeIf(notification -> notification.getId() == idNotification);

            user.setNotificationsReceived(notificationsOwner);
            usersService.update(user);

            Users userRecived = booking.getTenant();
            String title = "Respuesta sobre su solicitud de Estancia";
            String message = "Se ha aceptado su solicitud de estancia";
            createNotification(userRecived, user, title, message, null);

            notificationService.deleteNotification(idNotification);

        } catch (ServicesException ex) {
        }
        redirectAttributes.addAttribute("success", "Se aceptó la propuesta exitosamente");
        return "redirect:/home";
    }

    @PostMapping("/deny")
    public String denyBooking(
            @RequestParam("id") Integer id,
            @RequestParam("idNotification") Integer idNotification,
            RedirectAttributes redirectAttributes,
            ModelMap model, HttpSession session) {
        Users user = (Users) session.getAttribute("userSession");

        try {
            Users userRecived = bookingService.getBookingById(id).getTenant();
            String title = "Respuesta sobre su solicitud de Estancia";
            String message = "Se ha rechazado su solicitud de estancia";
            createNotification(userRecived, user, title, message, null);

            List<Notification> notifications = user.getNotificationsReceived();
            notifications.removeIf(notification -> notification.getId() == idNotification);

            user.setNotificationsReceived(notifications);
            usersService.update(user);

            notificationService.deleteNotification(idNotification);

            Booking booking = bookingService.getBookingById(id);
            Reserve reserve = booking.getReserve();
            reserve.getBookings().remove(booking);
            bookingService.deleteBooking(id);
            reserveService.create(reserve);

        } catch (ServicesException se) {
        }
        redirectAttributes.addAttribute("success", "Se rechazó la propuesta");
        //return "redirect:/user/notifications";
        return "redirect:/home";
    }

    public void createNotification(
            Users userRecived, Users userSender,
            String title, String message, Booking booking) {

        List<Notification> notificationsTenant = userRecived.getNotificationsReceived();
        Notification notification = new Notification();

        notification.setSender(userSender);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setBooking(booking);
        notificationsTenant.add(notification);
        notificationService.saveNotification(notification);

        userRecived.setNotificationsReceived(notificationsTenant);
        usersService.update(userRecived);
    }

}
