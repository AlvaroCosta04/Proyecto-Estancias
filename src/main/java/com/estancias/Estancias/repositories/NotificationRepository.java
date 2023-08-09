package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

   /* @Query("SELECT n FROM Notification n WHERE n.id = :id")
    Optional<Notification> findById(@Param("id") Integer id);

    @Query("SELECT n FROM Notification n WHERE n.receiver = :user")
    List<Notification> findByReceiver(@Param("user") Users user);

    @Query("SELECT n FROM Notification n WHERE n.sender = :user")
    List<Notification> findBySender(@Param("user") Users user);

    @Query("SELECT n FROM Notification n WHERE n.sender = :user OR n.receiver = :user")
    List<Notification> findBySenderOrReceiver(@Param("user") Users user);

    //Obtener todas las notificaciones que contienen una palabra clave en el título o el mensaje:
    @Query("SELECT n FROM Notification n WHERE n.Title LIKE %:keyword% OR n.message LIKE %:keyword%")
    List<Notification> findByKeyword(@Param("keyword") String keyword);*/

}
