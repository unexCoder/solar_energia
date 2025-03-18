package com.unexcoder.solar_energia.entidades;

import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "imagen")
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "El tipo MIME no puede ser nulo")
    @Size(max = 100, message = "El tipo MIME no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String mime;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    @Column(nullable = false, length = 255)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    @NotNull(message = "El contenido de la imagen no puede ser nulo")
    private byte[] contenido;
}
