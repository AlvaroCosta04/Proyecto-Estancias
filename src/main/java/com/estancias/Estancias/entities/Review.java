package com.estancias.Estancias.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @NotNull(message = "Inserte su calificacion")
    @Min(value = 1, message = "Por favor ingrese una calificación válida")
    @Max(value = 5, message = "Por favor ingrese una calificación válida")
    private Double score;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @OneToOne
    @JoinColumn(name = "reserve_id")
    private Reserve reserve;

}
