package com.estancias.Estancias.entities;

import com.estancias.Estancias.enums.Rol;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    protected String name;

    @NotBlank
    @Size(min = 3, max = 30, message = "El apellido debe tener entre 3 y 30 caracteres")
    protected String last_name;

    @Size(min = 5, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    protected String user_name;

    @Column(columnDefinition = "MEDIUMTEXT")
    protected String image;

    @NotBlank
    @Column(unique = true)
    protected String email;

    @NotBlank
    protected String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receiver_id")
    private List<Notification> notificationsReceived;

    protected String description;

    @OneToOne
    @JoinColumn(name = "house")
    protected House house;

    @Temporal(TemporalType.DATE)
    protected Date creationDate;

    @Enumerated(EnumType.STRING)
    protected Rol rol;
}
