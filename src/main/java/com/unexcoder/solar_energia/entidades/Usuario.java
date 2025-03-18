package com.unexcoder.solar_energia.entidades;

import java.util.UUID;

import com.unexcoder.solar_energia.enumeraciones.LoginRol;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email") // Ensures email uniqueness at DB level
    })
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private UUID id;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser válido")
    @Size(max = 255, message = "El email no puede exceder los 255 caracteres")
    @Column(nullable = false, unique = true, length = 255) // Ensure uniqueness
    private String email;
    
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El apellido no puede ser nulo")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellido;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "El rol no puede ser nulo")
    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private  LoginRol rol;

    @OneToOne(cascade = CascadeType.ALL, optional = true) 
    @JoinColumn(name = "imagen_id", referencedColumnName = "id", nullable = true)
    private Imagen imagen;
}
