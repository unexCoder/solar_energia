package com.unexcoder.solar_energia.servicios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.entidades.Imagen;
import com.unexcoder.solar_energia.excepciones.ImagenNotFoundException;
// import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.repositorios.ImagenRepositorio;
import com.unexcoder.solar_energia.utilities.ValidationUtils;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ImagenServicio {
    
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    
    private static final Logger logger = LoggerFactory.getLogger(ImagenServicio.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif");

    ImagenServicio(ImagenRepositorio imagenRepositorio) {
        this.imagenRepositorio = imagenRepositorio;
    }

    @Transactional
    public Imagen guardarImagen(MultipartFile file, String descripcion) {
       
        Optional<Imagen> existingImage = imagenRepositorio.findByNombreAndMime(file.getOriginalFilename(), file.getContentType());
        if (existingImage.isPresent()) {
            throw new IllegalArgumentException("Una imagen con el mismo nombre y tipo ya existe.");
        } else if (file.getSize() == 0) {
            throw new IllegalArgumentException("El archivo de imagen está vacío.");
        }

        ValidationUtils.validarArchivo(file,MAX_FILE_SIZE,ALLOWED_MIME_TYPES);

        try {
            Imagen imagen = new Imagen();
            imagen.setMime(file.getContentType());
            imagen.setNombre(file.getOriginalFilename());
            imagen.setContenido(file.getBytes());
            imagen.setDescripcion(sanitizarDescripcion(descripcion));
            return imagenRepositorio.save(imagen);
        } catch (Exception e) {
            logger.error("Error while processing image: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la imagen.", e);
        }
    }

    @Transactional
    public Imagen actualizarImagen(UUID id, MultipartFile file, String description) {
        Optional<Imagen> existingImage = imagenRepositorio.findByNombreAndMime(file.getOriginalFilename(), file.getContentType());
        if (existingImage.isPresent()) {
            throw new IllegalArgumentException("Una imagen con el mismo nombre y tipo ya existe.");
        } else if (id == null) {
            throw new IllegalArgumentException("El ID de la imagen no puede ser nulo.");
        }  else if (file.getSize() == 0) {
            throw new IllegalArgumentException("El archivo de imagen está vacío.");
        }
        
        ValidationUtils.validarArchivo(file, MAX_FILE_SIZE, ALLOWED_MIME_TYPES);

        return imagenRepositorio.findById(id)
        .map(imagen -> {
            try {
                imagen.setMime(file.getContentType());
                imagen.setNombre(file.getOriginalFilename());
                imagen.setContenido(file.getBytes());
                imagen.setDescripcion(sanitizarDescripcion(description));
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                logger.error("Error al actualizar la imagen: {}", e.getMessage(), e);
                throw new RuntimeException("Error al actualizar la imagen.", e);
            }
        })
        .orElseThrow(() -> new ImagenNotFoundException("Imagen no encontrada con el ID: " + id));
    }

    @Transactional
    public void eliminarImagen(UUID id) {
        Imagen imagen = imagenRepositorio.findById(id)
            .orElseThrow(() -> new ImagenNotFoundException("Imagen no encontrada con el ID: " + id));
        imagenRepositorio.deleteById(imagen.getId());
    }

    // private void validarArchivo(MultipartFile file) {
    //     if (file == null || file.isEmpty()) {
    //         throw new IllegalArgumentException("El archivo de imagen no puede estar vacío.");
    //     }
    //     if (file.getSize() > MAX_FILE_SIZE) {
    //         throw new IllegalArgumentException("El tamaño del archivo no debe superar los 5MB.");
    //     }
    //     if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
    //         throw new IllegalArgumentException("Formato de archivo no permitido. Solo se permiten JPG, PNG y GIF.");
    //     }
    // }

    private String sanitizarDescripcion(String descripcion) {
        if (descripcion != null) {
            descripcion = descripcion.trim();
            if (descripcion.length() > 500) {
                throw new IllegalArgumentException("La descripción no puede superar los 500 caracteres.");
            }
            return descripcion.replaceAll("<.*?>", ""); // Remove HTML tags
        }
        return descripcion;
    }
}
