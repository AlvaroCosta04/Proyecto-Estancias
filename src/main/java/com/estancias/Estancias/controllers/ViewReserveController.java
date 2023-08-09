package com.estancias.Estancias.controllers;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Booking;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Province;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.services.BookingService;
import com.estancias.Estancias.services.CountryService;
import com.estancias.Estancias.services.ImageService;
import com.estancias.Estancias.services.ProvinceService;
import com.estancias.Estancias.services.ReserveService;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view-reserve")
public class ViewReserveController {
//menuverreserva

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private ImageService imageService;

    /*@GetMapping("/all")
    public String getAllReserves(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        List<Reserve> reserves = reserveService.findAllReservesExceptFinalizado();
        System.out.println("La lista de reserva tiene la siguiente cantidad de reservas: " + reserves.size());
        List<String> images = new ArrayList<>();
        for (Reserve reserve : reserves) {
            List<Integer> imageIds = reserve.getHouse().getImage_id();
            if (imageIds != null && !imageIds.isEmpty()) {
                Integer imageId = imageIds.get(0);
                try {
                    String base64Image = imageService.findById(imageId);
                    images.add(base64Image);
                } catch (Exception e) {
                    images.add(null);
                }
            } else {
                images.add(null);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("reserves", reserves);
        model.addAttribute("images", images);
        return "viewAllReserves.html";
    }

    @GetMapping("/redirect-province")
    public String redirectProvince(@RequestParam(name = "provinceInput") String province) {
        String provinceWithoutSpecialCharacters
                = Normalizer.normalize(province, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return "redirect:/view-reserve/province-" + provinceWithoutSpecialCharacters;
    }

    @GetMapping("/province-{province}")
    public String getReservesByProvince(@PathVariable("province") String province,
            HttpSession session, RedirectAttributes redirectAttributes, ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");
        List<Province> provinces = provinceService.findByNameContaining(province);

        if (provinces == null || provinces.isEmpty()) {
            model.put("provinceNull", province);
            return "viewAllReserves.html";

        } else if (provinces.size() == 1) {
            List<Reserve> reserves = reserveService.findAllReservesByProvinceName(provinces.get(0).getName());
            List<String> images = new ArrayList<>();

            for (Reserve reserve : reserves) {
                List<Integer> imageIds = reserve.getHouse().getImage_id();

                if (imageIds != null && !imageIds.isEmpty()) {
                    Integer imageId = imageIds.get(0);

                    try {
                        String base64Image = imageService.findById(imageId);
                        images.add(base64Image);
                    } catch (Exception e) {
                        images.add(null);
                    }
                } else {
                    images.add(null);
                }
            }
            model.addAttribute("province", provinces.get(0).getName());
            model.addAttribute("reserves", reserves);
            model.addAttribute("user", user);
            model.addAttribute("images", images);
            return "viewAllReserves.html";

        } else if (provinces.size() >= 2) {
            List<String> provincesName = new ArrayList<>();

            for (Province provinceSelect : provinces) {
                provincesName.add(provinceSelect.getName());
            }
            model.put("provincesName", provincesName);
            return "viewAllReserves.html";
        } else {
            redirectAttributes.addAttribute("error", "Ocurrió un error a la hora de buscar la provincia");
            return "redirect:/home";
        }
    }

    @GetMapping("/houseType-{houseType}")
    public String getReservesyTypeOfHome(@PathVariable("houseType") String houseType,
            HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        List<Reserve> reserves = reserveService.findAllReservesByTypeOfHome(houseType);
        List<String> images = new ArrayList<>();

        for (Reserve reserve : reserves) {
            List<Integer> imageIds = reserve.getHouse().getImage_id();

            if (imageIds != null && !imageIds.isEmpty()) {
                Integer imageId = imageIds.get(0);
                try {
                    String base64Image = imageService.findById(imageId);
                    images.add(base64Image);
                } catch (Exception e) {
                    images.add(null);
                }
            } else {
                images.add(null);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("reserves", reserves);
        model.addAttribute("images", images);
        return "viewAllReserves.html";
    }*/
    @GetMapping("/myReserves")
    public String myReserves(HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        List<Reserve> reserves = reserveService.findReservesByTenant(user);
        List<String> images = new ArrayList<>();

        for (Reserve reserve : reserves) {
            List<Integer> imageIds = reserve.getHouse().getImage_id();

            if (imageIds != null && !imageIds.isEmpty()) {
                Integer imageId = imageIds.get(0);
                try {
                    String base64Image = imageService.findById(imageId);
                    images.add(base64Image);
                } catch (Exception e) {
                    images.add(null);
                }
            } else {
                images.add(null);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("infoReserve", null);
        model.addAttribute("reserves", reserves);
        model.addAttribute("images", images);
        return "viewAllReserves.html";
    }

    @GetMapping("/myReserves-{type}")
    public String myReserves(@PathVariable("type") String type, HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        List<Reserve> reserves;

        if (type.equals("owner")) {
            reserves = reserveService.findReservesByOwner(user);
        } else {
            reserves = reserveService.findReservesByTenant(user);
            model.addAttribute("infoReserve", 1);
        }

        List<String> images = new ArrayList<>();

        for (Reserve reserve : reserves) {
            List<Integer> imageIds = reserve.getHouse().getImage_id();

            if (imageIds != null && !imageIds.isEmpty()) {
                Integer imageId = imageIds.get(0);
                try {
                    String base64Image = imageService.findById(imageId);
                    images.add(base64Image);
                } catch (Exception e) {
                    images.add(null);
                }
            } else {
                images.add(null);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("reserves", reserves);
        model.addAttribute("images", images);
        return "viewAllReserves.html";
    }

    @GetMapping("/custom-properties")
    public String getReservesByCustomProperties(
            @RequestParam("provinceName") String provinceName,
            @RequestParam("houseType") String houseType,
            @RequestParam("precioMin") double precioMin,
            @RequestParam("precioMax") double precioMax,
            @RequestParam(required = false, name = "redirectPage") String redirectPage,
            @RequestParam(required = false, name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false, name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpSession session, RedirectAttributes redirectAttributes, ModelMap model) {

        Users user = (Users) session.getAttribute("userSession");

        if (endDate == null || startDate == null) {
            redirectAttributes.addAttribute("provinceName", provinceName);
            redirectAttributes.addAttribute("houseType", houseType);
            redirectAttributes.addAttribute("precioMin", precioMin);
            redirectAttributes.addAttribute("precioMax", precioMax);
            redirectAttributes.addAttribute("error", "Complete los datos antes de enviar el formulario");

            if (redirectPage != null) {
                return "redirect:/" + redirectPage;
            } else {
                return "redirect:/";
            }
        }

        List<Reserve> reserves = reserveService.findReservesByCriteriaAndDates(provinceName, houseType, precioMin, precioMax, startDate, endDate);
        List<String> images = new ArrayList<>();

        for (Reserve reserve : reserves) {
            List<Integer> imageIds = reserve.getHouse().getImage_id();

            if (imageIds != null && !imageIds.isEmpty()) {
                Integer imageId = imageIds.get(0);
                try {
                    String base64Image = imageService.findById(imageId);
                    images.add(base64Image);
                } catch (Exception e) {
                    images.add(null);
                }
            } else {
                images.add(null);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("reserves", reserves);
        model.addAttribute("images", images);
        model.addAttribute("infoReserve", null);
        return "viewAllReserves.html";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") Integer id, HttpSession session, ModelMap model) {
        Users user = (Users) session.getAttribute("userSession");
        try {
            Reserve reserve = reserveService.findReserveById(id);
            House house = reserve.getHouse();
            List<Integer> imagesId = house.getImage_id();
            List<String> images = new ArrayList<String>();

            if (!imagesId.isEmpty()) {
                for (Integer idImage : imagesId) {
                    String image = imageService.findById(idImage);
                    images.add(image);
                }
            }

            model.addAttribute("reserve", reserve);
            model.addAttribute("user", user);
            model.addAttribute("house", house);
            model.addAttribute("images", images);
            model.addAttribute("idReserve", id);

            return "reserveView.html";

        } catch (ServicesException ex) {
            return "redirect:/home";
        }
    }
}
