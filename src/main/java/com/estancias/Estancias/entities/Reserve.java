package com.estancias.Estancias.entities;

import com.estancias.Estancias.enums.Acceptance;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
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
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "reserve")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @OneToOne
    protected Users owner;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected List<Booking> bookings = new ArrayList<>();

    @ElementCollection
    @Temporal(TemporalType.DATE)
    protected List<Date> occupiedDates;

    @NotBlank
    protected int minDays;

    @NotBlank
    protected int maxDays;

    @NotBlank
    protected double price;

    @Enumerated(EnumType.STRING)
    private Acceptance acceptance = Acceptance.PUBLICADO;

    @OneToOne
    @JoinColumn(name = "house")
    protected House house;

}
