package com.unexcoder.solar_energia.entidades;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "articulo", uniqueConstraints = {@UniqueConstraint(columnNames = "nroArticulo")})
public class Articulo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private UUID id;

    // Use AtomicInteger in your service (for in-memory handling) 
    // or use AUTO_INCREMENT in MySQL (for DB-managed numbering).
    @NotNull(message = "El número de artículo no puede ser nulo")
    @Min(value = 1, message = "El número de artículo debe ser mayor o igual a 1")
    @Column(unique = true, nullable = false)
    private Integer nroArticulo;

    @NotNull(message = "El nombre del artículo no puede ser nulo")
    @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String nombreArticulo;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    @Column(length = 1000, nullable = true)
    private String descripcion;

    // Fetch type LAZY is recommended for many-to-one relationships to improve performance.
    @NotNull(message = "La fábrica no puede ser nula")
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "fabrica_id", referencedColumnName = "id", nullable = false)
    private Fabrica fabrica;
}
