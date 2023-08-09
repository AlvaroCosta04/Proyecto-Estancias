package com.estancias.Estancias;

import com.estancias.Estancias.entities.Booking;
import com.estancias.Estancias.entities.Continent;
import com.estancias.Estancias.entities.Country;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Image;
import com.estancias.Estancias.entities.Province;
import com.estancias.Estancias.entities.Reserve;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Rol;
import com.estancias.Estancias.services.BookingService;
import com.estancias.Estancias.services.ContinentService;
import com.estancias.Estancias.services.CountryService;
import com.estancias.Estancias.services.HouseService;
import com.estancias.Estancias.services.ImageService;
import com.estancias.Estancias.services.ProvinceService;
import com.estancias.Estancias.services.ReserveService;
import com.estancias.Estancias.services.UserService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ContinentService continentService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private UserService userService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (continentService.getRandomContinent() == null) {
            List<Continent> continents = new ArrayList<>();
            continents.add(new Continent("América del Sur"));
            continents.add(new Continent("América del Norte"));
            continents.add(new Continent("Europa"));
            continents.add(new Continent("Oceanía"));
            continents.add(new Continent("África"));

            /*Country argentina = new Country();
            List<String> provincesString = new ArrayList<>(Arrays.asList(
                    "Buenos Aires", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes",
                    "Entre Ríos", "Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza",
                    "Misiones", "Neuquén", "Río Negro", "Salta", "San Juan", "San Luis", "Santa Cruz",
                    "Santa Fe", "Santiago del Estero", "Tierra del Fuego", "Tucumán"
            ));

            for (String provinceString : provincesString) {
                Province province = new Province();
                province.setName(provinceString);
                province.setCountry(argentina);
                provinceService.create(province);
            }
            
            List<Province> provinces = new ArrayList<>();
            argentina.setName("Argentina");
            argentina.setContinent(continentService.getContinentById(1));
            argentina.setProvinces(provinces);
            countryService.saveCountry(argentina);*/
            //
            for (Continent continent : continents) {
                continentService.saveContinent(continent);
            }

            Map<String, List<String>> countriesByContinent = new HashMap<>();
            countriesByContinent.put("América del Sur", Arrays.asList(
                    "Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Ecuador", "Guyana", "Paraguay", "Perú", "Surinam", "Uruguay", "Venezuela"
            ));
            countriesByContinent.put("América del Norte", Arrays.asList(
                    "Canadá", "Estados Unidos", "México"
            ));
            countriesByContinent.put("Europa", Arrays.asList(
                    "Alemania", "Francia", "Italia", "Reino Unido", "España", "Polonia", "Rumania", "Países Bajos", "Bélgica", "Grecia"
            ));
            countriesByContinent.put("Oceanía", Arrays.asList(
                    "Australia", "Nueva Zelanda", "Fiyi", "Papúa Nueva Guinea"
            ));
            countriesByContinent.put("África", Arrays.asList(
                    "Nigeria", "Egipto", "Sudáfrica", "Argelia", "Etiopía", "Kenia", "Marruecos", "Uganda", "Tanzania", "Ghana"
            ));

            for (String continentName : countriesByContinent.keySet()) {
                Continent continent = continentService.getContinentByName(continentName);
                List<String> countryNames = countriesByContinent.get(continentName);

                for (String countryName : countryNames) {
                    Country country = new Country();
                    country.setName(countryName);
                    country.setContinent(continent);
                    countryService.saveCountry(country);
                }

            }

            List<String> provincesString = new ArrayList<>(Arrays.asList(
                    "Buenos Aires", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes",
                    "Entre Ríos", "Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza",
                    "Misiones", "Neuquén", "Río Negro", "Salta", "San Juan", "San Luis", "Santa Cruz",
                    "Santa Fe", "Santiago del Estero", "Tierra del Fuego", "Tucumán"
            ));

            for (String provinceString : provincesString) {
                Province province = new Province();
                province.setName(provinceString);
                province.setCountry(countryService.findByName("Argentina"));
                provinceService.create(province);
            }

            // Create users and users with houses
            for (int i = 1; i <= 100; i++) {
                Users user = new Users();
                user.setName("User " + i);
                user.setLast_name("Last Name " + i);
                user.setUser_name("user_" + i);
                user.setPassword("password" + i);
                user.setDescription("Description " + i);
                user.setCreationDate(new Date());

                if (i <= 50) {
                    // Create a regular user
                    user.setRol(Rol.USER);
                    user.setEmail("user" + i + "@exampleuser.com");
                    userService.createUser(user);
                } else {
                    // Create a owner user
                    user.setRol(Rol.USER);
                    user.setEmail("user" + i + "@exampleowner.com");
                    userService.createUser(user);

                    // Crear una casa para cada usuario
                    System.out.println("Crear casa: " + (i - 50));
                    House house = new House();
                    house.setStreet("Street " + i);
                    house.setNumber(i);
                    house.setPostalCode("Postal Code " + i);
                    house.setCity("City " + i);
                    house.setProvince(provinceService.findRandomProvince());
                    house.setCountry(countryService.findByName("Argentina"));
                    house.setHouseType(getHouseType(i));

                    // Crear una reserva para cada usuario y casa
                    System.out.println("Crear reserva: " + (i - 50));
                    Reserve reserve = new Reserve();
                    reserve.setOwner(user);
                    reserve.setMinDays(3);
                    reserve.setMaxDays(30);
                    reserve.setPrice(30.0 + (i - 50) * 2);
                    reserve.setHouse(house);

                    // Crear una reserva de 3 días a partir de hoy
                    List<Booking> bookings = new ArrayList<>();
                    Booking booking = new Booking();

                    //fechas
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //fecha inicio
                    Date startDate = null;
                    try {
                        startDate = sdf.parse("2024-06-03");
                    } catch (ParseException ex) {
                    }
                    booking.setStartDate(startDate);

                    //fecha fin
                    Date endDate = null;
                    try {
                        endDate = sdf.parse("2024-06-10");
                    } catch (ParseException ex) {
                    }
                    booking.setEndDate(endDate);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endDate);
                    calendar.add(Calendar.DATE, i);
                    Date newEndDate = calendar.getTime();
                    booking.setEndDate(newEndDate);

                    booking.setReserve(reserve);

                    List<Users> users = userService.findAllByRol(Rol.USER);
                    booking.setTenant(users.get(i - 51));
                    booking.setAvailable(true);
                    System.out.println("Crear booking: " + (i - 50));
                    bookingService.createBooking(booking);

                    reserve.setBookings(bookings);

                    try {
                        setImage(house, getFileList());
                    } catch (IOException ex) {
                        System.out.println("NO SE PUDO AGREGAR LAS IMAGENES A LA CASA");
                    }
                    houseService.create(house);

                    user.setHouse(house);
                    userService.createUser(user);
                    reserveService.create(reserve);
                }
            }
        }

    }

    public String getHouseType(int i) {
        if (i % 2 == 0) {
            return "casa";
        } else {
            return "departamento";
        }
    }

    public List<File> getFileList() {
        List<File> imageFiles = new ArrayList<>();
        String[] imagePaths = new String[]{
            "src/main/resources/static/img/image1.jpg",
            "src/main/resources/static/img/image2.jpg",
            "src/main/resources/static/img/image3.jpg",
            "src/main/resources/static/img/image4.jpg",
            "src/main/resources/static/img/image5.jpg",
            "src/main/resources/static/img/image6.jpg",
            "src/main/resources/static/img/image7.jpg",
            "src/main/resources/static/img/image8.jpg",
            "src/main/resources/static/img/image9.jpg",
            "src/main/resources/static/img/image10.jpg",
            "src/main/resources/static/img/image11.jpng",
            "src/main/resources/static/img/image12.jpeg",
            "src/main/resources/static/img/image13.jpg",
            "src/main/resources/static/img/image14.jpg",
            "src/main/resources/static/img/image15.jpg",
            "src/main/resources/static/img/image16.jpg",
            "src/main/resources/static/img/image17.jpg",
            "src/main/resources/static/img/image18.jpg",
            "src/main/resources/static/img/image19.jpg",
            "src/main/resources/static/img/image20.jpg",
            "src/main/resources/static/img/image21.jpg"
        };

        for (String imagePath : imagePaths) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFiles.add(imageFile);
            } else {
                // System.err.println("FILE " + imagePath + " DOES NOT EXIST");
            }
        }
        return imageFiles;

    }

    public House setImage(House house, List<File> imagesFile) throws IOException {
        Random rand = new Random();
        ArrayList<Integer> imageIds = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            File randomImageFile = imagesFile.get(rand.nextInt(imagesFile.size()));
            byte[] imagenBytes = Files.readAllBytes(randomImageFile.toPath());
            String imageString = Base64.getEncoder().encodeToString(imagenBytes);

            Image image = new Image();
            image.setImage(imageString);
            imageService.save(image);

            imageIds.add(image.getId());
        }

        house.setImage_id(imageIds);
        return house;
    }
}
