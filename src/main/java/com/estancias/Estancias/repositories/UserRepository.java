package com.estancias.Estancias.repositories;

import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u WHERE u.id = :id")
    Optional<Users> findById(@Param("id") Integer id);

    @Query("SELECT u FROM Users u WHERE u.user_name = :user_name")
    public Users findByUserName(@Param("user_name") String user_name);

    @Query("SELECT u FROM Users u WHERE u.rol = :rol")
    public List<Users> findAllByRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Users u WHERE u.last_name = :last_name")
    Optional<Users> findByLastName(@Param("last_name") String last_name);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    public Users findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Users findRandomUser();
}
