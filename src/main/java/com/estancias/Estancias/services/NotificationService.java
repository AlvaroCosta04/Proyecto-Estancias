package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Notification;
import com.estancias.Estancias.repositories.NotificationRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Transactional
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Integer id) throws ServicesException {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new ServicesException("Notification not found with id " + id));
        notificationRepository.delete(notification);
    }

    @Transactional(readOnly = true)
    public Notification findById(Integer id) throws ServicesException {
        return notificationRepository.findById(id).orElseThrow(() -> new ServicesException("Notification not found with id " + id));
    }
}
