package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Image;
import com.estancias.Estancias.repositories.ImageRepository;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    /*@Transactional
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Transactional
    public void delete(@NotNull Integer id) throws ServicesException {
        Image image = getImageFromOptional(imageRepository.findById(id));
        imageRepository.delete(image);
    }

    @Transactional
    public void saveImage(MultipartFile picture) throws IOException {
        byte[] imageBytes = picture.getBytes();
        String ImageString = Base64.getEncoder().encodeToString(imageBytes);
        Image image = new Image();
        image.setImage(ImageString);
        imageRepository.save(image);
    }

    public String findById(Integer image_id) throws ServicesException {
        Image image = getImageFromOptional(imageRepository.findById(image_id));
        return image.getImage();
    }

    private Image getImageFromOptional(Optional<Image> imageOpt) throws ServicesException {
        if (imageOpt.isPresent()) {
            return imageOpt.get();
        }
        throw new ServicesException("No se ha encontrado una imagen");
    }*/
    @Transactional
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Transactional
    public void deleteById(@NotNull Integer id) throws ServicesException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ServicesException("No se ha encontrado una imagen"));
        imageRepository.delete(image);
    }

    @Transactional
    public void delete(Image image) {
        imageRepository.delete(image);
    }

    @Transactional
    public void saveImage(MultipartFile picture) throws IOException {
        byte[] imageBytes = picture.getBytes();
        String ImageString = Base64.getEncoder().encodeToString(imageBytes);
        Image image = new Image();
        image.setImage(ImageString);
        imageRepository.save(image);
    }

    /*@Transactional(readOnly = true)
    public String findById(Integer image_id) throws ServicesException {
        Image image = imageRepository.findById(image_id)
                .orElseThrow(() -> new ServicesException("No se ha encontrado una imagen"));
        return image.getImage();
    }*/
    @Transactional(readOnly = true)
    public String findById(Integer image_id) throws ServicesException {
        Optional<Image> optionalImage = imageRepository.findById(image_id);
        if (!optionalImage.isPresent()) {
            throw new ServicesException("No se ha encontrado una imagen con el id " + image_id);
        }
        Image image = optionalImage.get();
        return image.getImage();
    }

}
