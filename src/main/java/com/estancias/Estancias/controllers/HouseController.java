package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Image;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.services.CountryService;
import com.estancias.Estancias.services.HouseService;
import com.estancias.Estancias.services.ImageService;
import com.estancias.Estancias.services.ProvinceService;
import com.estancias.Estancias.services.ReserveService;
import com.estancias.Estancias.services.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/dwelling")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public String crearHogarFormulario(ModelMap model) {
        model.addAttribute("house", new House() {
        });
        //model.put("countries", countryService.getAllCountriesOrderedByName());
        model.put("provinces", provinceService.findAllByOrderByNameAsc());
        return "crearVivienda.html";
    }

    /*@GetMapping("/create")
    public String crearHogarPrimeraVezFormulario(ModelMap model) {
        model.addAttribute("house", new House() {
        });
        return "crearVivienda.html";
    }*/
    @PostMapping("/post")
    public String guardarHogar(
            @ModelAttribute("house") House house,
            HttpSession session,
            @RequestParam("images[]") MultipartFile[] images,
            ModelMap model) {

        house.setCountry(countryService.findByName("Argentina"));

        Users user = (Users) session.getAttribute("userSession");

        int i = 1;
        if (user.getHouse() == null) {
            i = 0;
        }

        ArrayList<Integer> imageIds = new ArrayList<>();

        try {
            for (MultipartFile picture : images) {
                byte[] imageBytes = picture.getBytes();
                String ImageString = Base64.getEncoder().encodeToString(imageBytes);
                Image image = new Image();
                image.setImage(ImageString);
                imageService.save(image);
                //imageIds.add(image.getId().toString());
                imageIds.add(image.getId());
            }
            house.setImage_id(imageIds);
            houseService.create(house);

            user.setHouse(house);
            userService.update(user);

        } catch (IOException ioe) {
            model.put("error", "No se ha podido cargar la imagen, por favor ingrese otra");
            return "crearVivienda.html";
        }

        if (i == 1) {
            return "redirect:/home";
        } else {
            model.addAttribute("reserve", new Reserve());
            model.addAttribute("exito",
                    "Se ha añadido un hogar a su usuario exitosamente. "
                    + "Ahora solo queda completar los ultimos datos de tu Estancia");
            return "crearEstancia.html";
        }
    }

    @GetMapping("/modify")
    public String modificarHogarFormulario(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
         House house = user.getHouse();
        List<String> images = new ArrayList();
        try {
            for (Integer imageId : house.getImage_id()) {

                images.add(imageService.findById(imageId));
            }
        } catch (ServicesException se) {

        }

        model.addAttribute("house", house);
        model.addAttribute("images", images);

        model.put("provinces", provinceService.findAllByOrderByNameAsc());
        return "modifyHouse.html";
    }

    @PostMapping("/modify-post")
    public String modificarHogar(
            @ModelAttribute("house") House house,
            HttpSession session,
            @RequestParam("images[]") MultipartFile[] images,
            ModelMap model) throws ServicesException {

        Users user = (Users) session.getAttribute("userSession");

        House currentHouse = user.getHouse();

        currentHouse.setStreet(house.getStreet());
        currentHouse.setNumber(house.getNumber());
        currentHouse.setPostalCode(house.getPostalCode());
        currentHouse.setCity(house.getCity());
        currentHouse.setProvince(house.getProvince());
        currentHouse.setCountry(house.getCountry());
        currentHouse.setHouseType(house.getHouseType());

        // Eliminar las imágenes antiguas asociadas a la casa
        for (Integer imageId : currentHouse.getImage_id()) {
            try {
                imageService.deleteById(imageId);
            } catch (ServicesException ex) {
                // Manejar excepción (opcional)
            }
        }

        // Agregar las nuevas imágenes a la casa
        ArrayList<Integer> newImageIds = new ArrayList<>();
        try {
            for (MultipartFile picture : images) {
                byte[] imageBytes = picture.getBytes();
                String imageString = Base64.getEncoder().encodeToString(imageBytes);
                Image image = new Image();
                image.setImage(imageString);
                imageService.save(image);
                newImageIds.add(image.getId());
            }
        } catch (IOException io) {
            model.addAttribute("io", io);
            return "modifyHouse.html";
        }
        currentHouse.setImage_id(newImageIds);
        houseService.update(currentHouse, user.getHouse().getId());

        user.setHouse(currentHouse);
        userService.update(user);

        Reserve reserve = reserveService.findByOwner(user);
        reserve.setHouse(currentHouse);
        reserveService.update(reserve, reserve.getId());

        return "redirect:/home";
    }

}
