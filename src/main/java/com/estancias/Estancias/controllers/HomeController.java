package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.PasswordToken;
import com.estancias.Estancias.entities.Province;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Rol;
import com.estancias.Estancias.services.HouseService;
import com.estancias.Estancias.services.MailService;
import com.estancias.Estancias.services.PasswordTokenService;
import com.estancias.Estancias.services.ProvinceService;
import com.estancias.Estancias.services.ReserveService;
import com.estancias.Estancias.services.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private MailService mailService;

    @GetMapping
    public String getIndex(HttpSession session, ModelMap model,
            @RequestParam(required = false, name = "success") String success,
            @RequestParam(required = false, name = "error") String error,
            @RequestParam(required = false, name = "provinceName") String provinceName,
            @RequestParam(required = false, name = "houseType") String houseType,
            @RequestParam(required = false, name = "precioMin") Double precioMin,
            @RequestParam(required = false, name = "precioMax") Double precioMax,
            @RequestParam(required = false, name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false, name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Users user = (Users) session.getAttribute("userSession");
        List<Province> provinces = new ArrayList<>();
        List<String> provincesNames = new ArrayList<>();
        List<House> houses = new ArrayList<>();

        /*try {
            houses = houseService.find5RandomHouses();
            for (House house : houses) {
                provinces.add(house.getProvince());
                provincesNames.add(house.getProvince().getName());
            }
        } catch (ServicesException ex) {
        }*/
        if (success != null) {
            model.addAttribute("success", success);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        model.put("provinceName", provinceName);
        model.put("houseType", houseType);
        model.put("precioMin", precioMin);
        model.put("precioMax", precioMax);

        model.put("provinces", provinceService.findAllByOrderByNameAsc());
        model.put("randomProvinces", provinces);
        model.put("provincesNames", provincesNames);
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_FAMILY','ROLE_ADMIN')")
    @GetMapping("/home")
    public String getHome(HttpSession session, ModelMap model,
            @RequestParam(required = false, name = "success") String success,
            @RequestParam(required = false, name = "error") String error,
            @RequestParam(required = false, name = "provinceName") String provinceName,
            @RequestParam(required = false, name = "houseType") String houseType,
            @RequestParam(required = false, name = "precioMin") Double precioMin,
            @RequestParam(required = false, name = "precioMax") Double precioMax,
            @RequestParam(required = false, name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false, name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Users user = (Users) session.getAttribute("userSession");
        int rol = 0;
        if (user.getRol() == Rol.USER) {
            rol = 1;
        } else if (user.getRol() == Rol.FAMILY) {
            rol = 2;
        } else if (user.getRol() == Rol.ADMIN) {
            rol = 3;
        }

        if (user.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("user", user);
        model.addAttribute("rol", rol);

        Reserve reserve = new Reserve();
        try {
            reserve = reserveService.findByOwner(user);
        } catch (ServicesException ex) {
        }
        model.addAttribute("reserve", reserve);
        List<Province> provinces = new ArrayList<>();
        List<String> provincesNames = new ArrayList<>();
        List<House> houses = new ArrayList<>();

 /* try {
            houses = houseService.find5RandomHouses();
            for (House house : houses) {
                provinces.add(house.getProvince());
                provincesNames.add(house.getProvince().getName());
            }
        } catch (ServicesException ex) {
        }*/
        model.put("provinceName", provinceName);
        model.put("houseType", houseType);
        model.put("precioMin", precioMin);
        model.put("precioMax", precioMax);

        model.put("provinces", provinceService.findAllByOrderByNameAsc());
        /*model.put("randomProvinces", provinces);
        model.put("provincesNames", provincesNames);*/
        if (success != null) {
            model.addAttribute("success", success);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "home.html";
    }

    /*
    @GetMapping("/signup/user")
    public String getFormUser(
            @RequestParam(required = false, name = "error") String error,
            ModelMap model) {

        if (error != null) {
            model.addAttribute("error", error);
        }
        return "createAccount.html";
    }

    @PostMapping("/code-signup/user")
    public String codeSignUpUser(
            @RequestParam("name") String name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email,
            @RequestParam("user_name") String user_name,
            @RequestParam("password") String password,
            @RequestParam("confirmar_password") String confirmar_password,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes, ModelMap model) {

        try {
            Users users = userService.findByEmail(email);
            if (users != null) {
                redirectAttributes.addAttribute("error", "El correo electrónico ya está registrado.");
                return "redirect:/signup/user";
            }

            if (!password.equals(confirmar_password)) {
                redirectAttributes.addAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/signup/user";
            }
        } catch (ServicesException ex) {
        }

        try {
            mailService.sendEmail(
                    email,
                    "Estancias Argentinas",
                    "El código para crear su cuenta es: " + passwordTokenService.generateCode(email));

            model.addAttribute("name", name);
            model.addAttribute("last_name", last_name);
            model.addAttribute("email", email);
            model.addAttribute("user_name", user_name);
            model.addAttribute("password", password);
            model.addAttribute("description", description);
            model.addAttribute("stepCode", "stepCode");
            return "createAccount.html";
        } catch (ServicesException ex) {
            redirectAttributes.addAttribute("error", "Hubo un error a la hora de crear el usuario");
            return "redirect:/signup/user";
        }

    }

    @GetMapping("/image/user")
    public String imageUser(
            @RequestParam("name") String name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email,
            @RequestParam("user_name") String user_name,
            @RequestParam("password") String password,
            @RequestParam("description") String description,
            @RequestParam(name = "verificationCode") String verificationCode,
            RedirectAttributes redirectAttributes, ModelMap model) {

        PasswordToken token = passwordTokenService.findByCode(verificationCode);

        if (token == null || !token.getUserMail().equals(email)) {

            model.addAttribute("error", "El código ingresado es incorrecto");
            model.addAttribute("name", name);
            model.addAttribute("last_name", last_name);
            model.addAttribute("email", email);
            model.addAttribute("user_name", user_name);
            model.addAttribute("password", password);
            model.addAttribute("description", description);
            model.addAttribute("image", "image");
            return "redirect:/signup/user";
        }

        model.addAttribute("name", name);
        model.addAttribute("last_name", last_name);
        model.addAttribute("email", email);
        model.addAttribute("user_name", user_name);
        model.addAttribute("password", password);
        model.addAttribute("description", description);
        model.addAttribute("image", "image");
        return "createAccount.html";
    }*/
    @GetMapping("/signup/user")
    public String getFormUser(
            @RequestParam(required = false, name = "error") String error,
            ModelMap model,
            HttpSession session) {

        if (error != null) {
            model.addAttribute("error", error);
        }
        return "createAccount.html";
    }

    @PostMapping("/singup/user-post")
    public String postFormUser(
            @RequestParam("name") String name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email,
            @RequestParam("user_name") String user_name,
            @RequestParam("password") String password,
            @RequestParam("confirmar_password") String confirmar_password,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            Users users = userService.findByEmail(email);
            if (users != null) {
                redirectAttributes.addAttribute("error", "El correo electrónico ya está registrado.");
                return "redirect:/signup/user";
            }

            if (!password.equals(confirmar_password)) {
                redirectAttributes.addAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/signup/user";
            }

            mailService.sendEmail(
                    email,
                    "Estancias Argentinas",
                    "El código para crear su cuenta es: " + passwordTokenService.generateCode(email));

            session.setAttribute("name", name);
            session.setAttribute("last_name", last_name);
            session.setAttribute("email", email);
            session.setAttribute("user_name", user_name);
            session.setAttribute("password", password);
            session.setAttribute("description", description);
            session.setAttribute("stepCode", "stepCode");

        } catch (ServicesException ex) {
        }

        return "redirect:/code-signup/user";
    }

    @GetMapping("/code-signup/user")
    public String codeSignUpUser(
            @RequestParam(required = false, name = "error") String error,
            ModelMap model,
            HttpSession session) {

        String email = (String) session.getAttribute("email");

        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("email", email);
        return "code-singup.html";
    }

    @GetMapping("/image/user")
    public String imageUser(
            @RequestParam(name = "verificationCode") String verificationCode,
            HttpSession session, RedirectAttributes redirectAttributes) {

        String name = (String) session.getAttribute("name");
        String last_name = (String) session.getAttribute("last_name");
        String email = (String) session.getAttribute("email");
        String user_name = (String) session.getAttribute("user_name");
        String password = (String) session.getAttribute("password");
        String description = (String) session.getAttribute("description");

        System.out.println("codigo introducido: " + verificationCode);
        PasswordToken token = passwordTokenService.findByCode(verificationCode);

        if (token == null || !token.getUserMail().equals(email)) {

            session.setAttribute("name", name);
            session.setAttribute("last_name", last_name);
            session.setAttribute("email", email);
            session.setAttribute("user_name", user_name);
            session.setAttribute("password", password);
            session.setAttribute("description", description);
            session.setAttribute("image", "image");
            redirectAttributes.addAttribute("error", "El código ingresado es incorrecto");
            return "redirect:/code-signup/user";
        }

        session.setAttribute("name", name);
        session.setAttribute("last_name", last_name);
        session.setAttribute("email", email);
        session.setAttribute("user_name", user_name);
        session.setAttribute("password", password);
        session.setAttribute("description", description);
        session.setAttribute("image", "image");
        return "image-user.html";
    }

    @PostMapping("/signup-post/user")
    public String signUpUser(
            @RequestParam(value = "imageFile", required = false) MultipartFile image,
            HttpSession session,
            RedirectAttributes redirectAttributes, ModelMap model) {

        String name = (String) session.getAttribute("name");
        String last_name = (String) session.getAttribute("last_name");
        String email = (String) session.getAttribute("email");
        String user_name = (String) session.getAttribute("user_name");
        String password = (String) session.getAttribute("password");
        String description = (String) session.getAttribute("description");

        Users user = new Users();
        user.setName(name);
        user.setLast_name(last_name);
        user.setEmail(email);
        user.setUser_name(user_name);
        user.setPassword(password);
        user.setDescription(description);

        try {
            setImage(user, image);
        } catch (IOException ioe) {
            redirectAttributes.addAttribute("error", "No se ha podido cargar la imagen, por favor ingrese otra");
            return "redirect:/image/user";
        }

        try {
            passwordTokenService.delete(passwordTokenService.findByMail(user.getEmail()).getId());
        } catch (ServicesException ex) {
        }

        return createUser(user, redirectAttributes, model);
    }

    @GetMapping("/update-user")
    public String getFormUpdateUser(
            @RequestParam(required = false, name = "error") String error,
            HttpSession session, ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");
        model.put("id", user.getId());
        model.put("name", user.getName());
        model.put("last_name", user.getLast_name());
        model.put("email", user.getEmail());
        model.put("user_name", user.getUser_name());
        model.put("description", user.getDescription());
        model.put("image", user.getImage());
        if (error != null) {
            model.put("error", error);
        }
        return "updateUser.html";
    }

    @PostMapping("/modify/user")
    public String modifyUser(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "last_name") String last_name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "user_name") String user_name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes, ModelMap model) throws ServicesException {

        Users user = userService.findById(id);
        user.setName(name);
        user.setLast_name(last_name);
        user.setEmail(email);
        user.setUser_name(user_name);
        user.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                setImage(user, image);
            } catch (IOException ioe) {
                redirectAttributes.addAttribute("error", "No se ha podido cargar la imagen, por favor ingrese otra");
                return "redirect:/modify/user";
            }
        }
        return modifyUser(user, redirectAttributes);
    }

    @GetMapping("/login")
    public String getLoginForm(@RequestParam(required = false) String error,
            @RequestParam(required = false, name = "email") String email,
            @RequestParam(required = false, name = "success") String success,
            ModelMap model) {

        if (error != null) {
            model.put("error", "Usuario o contraseña invalida");
        }

        if (success != null) {
            model.addAttribute("success", success);
        }

        if (email != null) {
            model.addAttribute("email", email);
        }

        return "iniciarSesion.html";
    }

    @GetMapping("/start-reserve")
    public String getPreviewReserve(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");

        return "previoEstancia.html";
    }

    private String createUser(Users user, RedirectAttributes redirectAttributes, ModelMap model) {
        user.setRol(Rol.USER);
        userService.createUser(user);

        redirectAttributes.addAttribute("success", "¡Su cuenta ha sido creada exitosamente!");
        redirectAttributes.addAttribute("email", user.getEmail());
        return "redirect:/login";
    }

    private String modifyUser(Users user, RedirectAttributes redirectAttributes) {
        try {
            Users userMail = userService.findByEmail(user.getEmail());
            Users userUserName = userService.findByUsername(user.getUser_name());

            if (userMail != null && userMail.getId() != user.getId()) {
                redirectAttributes.addAttribute("error", "El correo ya está asociado a otro usuario");
                return "redirect:/update-user";
            }
            /*if (userUserName != null) {
                redirectAttributes.addAttribute("error", "El userName ya está asociado a otro usuario");
                return "redirect:/update-user";
            }*/

            user.setRol(Rol.USER);
            userService.update(user);

        } catch (ServicesException se) {
            redirectAttributes.addAttribute("users", user);
            redirectAttributes.addAttribute("error", se.getMessage());
            return "redirect:/update-user";
        }

        redirectAttributes.addAttribute("success", "¡Su cuenta ha sido modificada exitosamente!");
        redirectAttributes.addAttribute("email", user.getEmail());
        return "redirect:/login";
    }

    private void setImage(Users user, MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        String Image = Base64.getEncoder().encodeToString(imageBytes);
        user.setImage(Image);
    }
}
