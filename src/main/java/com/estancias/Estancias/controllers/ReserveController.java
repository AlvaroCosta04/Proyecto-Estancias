package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Booking;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Acceptance;
import com.estancias.Estancias.services.BookingService;
import com.estancias.Estancias.services.ReserveService;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String getFormCreateReserve(ModelMap model) {
        model.addAttribute("reserve", new Reserve());
        return "crearEstancia.html";
    }

    @PostMapping("/post")
    public String createReserve(
            @Valid Reserve reserve,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");

        if (result.hasErrors()) {
            model.put("reserve", reserve);
            model.put("errors", result.getAllErrors());
            return "crearEstancia.html";
        }

        reserve.setOwner(user);
        if (user.getHouse() != null) {
            reserve.setHouse(user.getHouse());
        }
        reserveService.create(reserve);
        redirectAttributes.addAttribute("success", "Se ha publicado la reserva de manera exitosa");
        return "redirect:/home";
    }

    @GetMapping("/modify")
    public String getFormModifyReserve(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        try {
            Reserve reserve = reserveService.findByOwner(user);
            model.addAttribute("ID", reserveService.findByOwner(user).getId());
            model.addAttribute("reserve", reserve);
        } catch (ServicesException ex) {
            return "redirect:/home";
        }
        return "modifyReserve.html";
    }

    @PostMapping("/modify-post")
    public String modifyReserve(
            @Valid Reserve reserve,
            @RequestParam(value = "ID") Integer id,
            BindingResult result,
            HttpSession session,
            ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");

        reserve.getId();

        if (result.hasErrors()) {
            model.put("reserve", reserve);
            model.put("errors", result.getAllErrors());
            return "modifyReserve.html";
        }

        reserve.setOwner(user);
        if (user.getHouse() != null) {
            reserve.setHouse(user.getHouse());
        }
        model.addAttribute("reserve", reserve);
        reserveService.update(reserve, id);

        return "redirect:/home";
    }

    public int coroborarFechas(Date startDate, Date endDate, int minDays, int maxDays) {
        List<Date> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            days.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        if (days.size() < minDays) {
            return 1;
        } else if (days.size() > maxDays) {

            return 2;
        }

        return 0;
    }

}
