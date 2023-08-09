package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.PasswordToken;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.services.MailService;
import com.estancias.Estancias.services.PasswordTokenService;
import com.estancias.Estancias.services.UserService;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resettingPassword")
public class RessetPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private MailService mailService;

    @GetMapping("/step1")
    public String getFormStep1(@RequestParam(required = false, name = "error") String error, ModelMap model) {

        if (error != null) {
            model.addAttribute("error", error);
        }

        return "verifyingUserEmail.html";
    }

    @PostMapping("/step1-post")
    public String postStep1(
            @RequestParam(value = "email") String userMail,
            RedirectAttributes redirectAttributes,
            ModelMap model) {

        try {
            if (userService.findByEmail(userMail) == null) {
                redirectAttributes.addAttribute("error", "No se ha encontrado un usuario con el correo ingresado");
                return "redirect:/resettingPassword/step1";
            } else {
                mailService.sendEmail(
                        userMail,
                        "Cambio de contraseña",
                        "El código para restablecer su contraseña es: " + passwordTokenService.generateCode(userMail)
                        + "\nEste código tiene 45 minutos de utilidad antes de que caduque.");
            }

        } catch (ServicesException ex) {
        }
        redirectAttributes.addAttribute("userMail", userMail);
        return "redirect:/resettingPassword/step2";
    }

    @GetMapping("/step2")
    public String getFormStep2(@RequestParam(name = "userMail") String userMail,
            @RequestParam(required = false, name = "error") String error,
            @RequestParam(required = false, name = "mail") String mail, ModelMap model) {

        if (error != null) {
            model.addAttribute("error", error);
        }

        if (mail != null) {
            model.addAttribute("userMail", mail);
        }

        System.out.println("step2 " + userMail);
        model.addAttribute("userMail", userMail);
        return "verificationCode.html";
    }

    @PostMapping("/step2-post")
    public String postStep2(
            @RequestParam(value = "userMail") String userMail,
            @RequestParam(name = "verificationCode") String verificationCode,
            RedirectAttributes redirectAttributes,
            ModelMap model) {

        PasswordToken token = passwordTokenService.findByCode(verificationCode);

        if (token == null || !token.getUserMail().equals(userMail)) {
            redirectAttributes.addAttribute("error", "El código ingresado es incorrecto o no está asociado a este correo");
            redirectAttributes.addAttribute("userMail", userMail);
            return "redirect:/resettingPassword/step2";
        }

        redirectAttributes.addAttribute("userMail", userMail);
        return "redirect:/resettingPassword/step3";
    }

    @GetMapping("/step3")
    public String getFormStep3(
            @RequestParam(name = "userMail") String userMail,
            @RequestParam(required = false, name = "error") String error, ModelMap model) {

        if (error != null) {
            model.addAttribute("error", error);
        }

        model.addAttribute("userMail", userMail);
        return "resettingPassword.html";
    }

    @PostMapping("/step3-post")
    public String postStep3(
            @RequestParam(name = "userMail") String userMail,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirm_password,
            RedirectAttributes redirectAttributes,
            ModelMap model) {

        if (!password.equals(confirm_password)) {
            model.addAttribute("userMail", userMail);
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "resettingPassword.html";
        }

        try {
            Users user = userService.findByEmail(userMail);

            user.setPassword(password);
            userService.updatePassword(user);

            passwordTokenService.delete(passwordTokenService.findByMail(userMail).getId());

        } catch (ServicesException ex) {
            redirectAttributes.addAttribute("error", "No se pudo actualizar la contraseña: " + ex.getMessage());
            return "redirect:/resettingPassword/step3";
        }

        redirectAttributes.addAttribute("success", "La contraseña fue actualizada correctamente");
        redirectAttributes.addAttribute("email", userMail);
        return "redirect:/login";
    }
}
