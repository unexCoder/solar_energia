package com.unexcoder.solar_energia.entidades;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "fabrica")
public class Fabrica {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private UUID id;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @OneToOne(cascade = CascadeType.ALL, optional = true) 
    @JoinColumn(name = "imagen_id", referencedColumnName = "id", nullable = true)
    private Imagen imagen;

}
