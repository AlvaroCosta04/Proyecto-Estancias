package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Notification;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.services.NotificationService;
import com.estancias.Estancias.services.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService usersService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping()
    public String getProfile(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");

        model.put("profile", user);
        model.put("userId", user.getId());

        model.put("profile.image", user.getImage());
        model.put("profile.description", user.getDescription());
        model.put("profile.name", user.getName());
        model.put("profile.last_name", user.getLast_name());
        model.put("profile.user_name", "UserName: " + user.getUser_name());

        return "verPerfil.html";
    }

    @GetMapping("/{id}")
    public String getProfile(HttpSession session, @PathVariable("id") Integer id, ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");
        if (user != null && Objects.equals(user.getId(), id)) {
            return "redirect:/user";
        }

        try {
            // model.put("userId", user.getId());
            Users users = usersService.findById(id);
            List<Notification> notifications = user.getNotificationsReceived();

            String image = users.getImage();
            model.put("profile", user);
            model.put("profile", users);
            model.put("image", image);
            model.put("notifications", notifications);
        } catch (ServicesException se) {
            model.put("error", se.getMessage());
            return "index";
        }
        return "verPerfil";
    }

    @GetMapping("/notifications")
    public String Notifications(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");

        model.put("profile", user);
        List<Notification> notifications = user.getNotificationsReceived();
        Collections.reverse(notifications);
        model.put("notifications", notifications);

        for (Notification notification : user.getNotificationsReceived()) {
            System.out.println(notification.getTitle());
            System.out.println(notification.getMessage());
            System.out.println("- - -");
        }

        return "notifications.html";
    }

    @GetMapping("/notification-{id}")
    public String Notification(@PathVariable("id") Integer id,
            HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");

        try {
            model.put("profile", user);
            model.put("notification", notificationService.findById(id));
        } catch (ServicesException se) {

        }

        return "rentalRequest.html";
    }
}
