package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.House;
import com.estancias.Estancias.entities.Users;
import com.estancias.Estancias.enums.Rol;
import com.estancias.Estancias.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if (null == user) {
            throw new UsernameNotFoundException("No se ha encontrado el usuario");
        }
        List<GrantedAuthority> permissions = new ArrayList();
        GrantedAuthority grantedAuth = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
        permissions.add(grantedAuth);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("userSession", user);
        return new User(user.getEmail(), user.getPassword(), permissions);
    }

    //create
    @Transactional
    public void createUser(Users user) {
        user.setCreationDate(new Date());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    //delete
    @Transactional
    public void delete(@NotNull Integer id) throws ServicesException {
        Users user = findById(id);
        userRepository.delete(user);
    }

    //update
    @Transactional
    public void update(Users user) {
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Users user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean validateUser(String email, String password) {
        Users user = userRepository.findByEmail(email);
        if (user != null) {
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    //read
    @Transactional(readOnly = true)
    public Users findById(@NotNull Integer id) throws ServicesException {
        return userRepository.findById(id).orElseThrow(() -> new ServicesException("No se ha encontrado un usuario con el id ingresado"));
    }

    @Transactional(readOnly = true)
    public Users getRandomUser() {
        return userRepository.findRandomUser();
    }

    @Transactional(readOnly = true)
    public List<Users> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Users> findAllByRol(Rol rol) {
        return userRepository.findAllByRol(rol);
    }

    @Transactional(readOnly = true)
    public Users findByUsername(String username) throws ServicesException {
        return userRepository.findByUserName(username);
    }

    @Transactional(readOnly = true)
    public Users findByLastName(@NotNull String lastName) throws ServicesException {
        return userRepository.findByLastName(lastName).orElseThrow(() -> new ServicesException("No se ha encontrado un usuario con ese apellido"));
    }

    @Transactional(readOnly = true)
    public Users findByEmail(@NotNull String email) throws ServicesException {
        return userRepository.findByEmail(email);
    }
}
