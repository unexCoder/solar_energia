package com.unexcoder.solar_energia.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.entidades.Imagen;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.repositorios.FabricaRepositorio;
import com.unexcoder.solar_energia.utilities.ValidationUtils;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FabricaServicio {
    
    @Autowired
    private FabricaRepositorio fabricaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;
    private static final Logger logger = LoggerFactory.getLogger(FabricaServicio.class);

    @Transactional
    public void guardarFabrica(String nombre,MultipartFile file) throws ValidationException, InvalidOperationException{
        
        // validations
        ValidationUtils.validarNombreFabrica(nombre,fabricaRepositorio);
        
        try {
            Fabrica fabrica = new Fabrica();
            fabrica.setNombre(nombre);
                    if (file != null && !file.isEmpty()) {
            Imagen img = imagenServicio.guardarImagen(file, "factory_logo");
            fabrica.setImagen(img);
        } // If no file, imagen remains null
            fabricaRepositorio.save(fabrica);            
        } catch (Exception e) {
            logger.error("Error al guardar la fábrica: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar la fábrica.", e);
        }
    }
    
    @Transactional
    public void editarFabrica(UUID id,String nombre, MultipartFile file) throws ValidationException, InvalidOperationException {
        ValidationUtils.validarNombreFabrica(nombre,fabricaRepositorio);
        Fabrica fabrica = fabricaRepositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No se encontró la fábrica con el ID: " + id));
        fabrica.setNombre(nombre.trim());
        if (file != null && !file.isEmpty()) {
            if (fabrica.getImagen() != null) {
                fabrica.setImagen(imagenServicio.actualizarImagen(fabrica.getImagen().getId(), file, "factory_logo"));
            } else {
                fabrica.setImagen(imagenServicio.guardarImagen(file, "factory_logo"));
            }
        }
        fabricaRepositorio.save(fabrica);

    }
    
    @Transactional
    public void borrarFabrica(UUID id) {
        if (!fabricaRepositorio.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la fábrica con el ID: " + id);
        }
        fabricaRepositorio.deleteById(id);
        // fabricaRepositorio.delete(fabrica);
    }

    
    @Transactional(readOnly = true)
    public List<Fabrica> listarFabricas() {
        List<Fabrica> fabricas = new ArrayList<>();
        fabricas = fabricaRepositorio.findAll();
        return fabricas;
    }


    @Transactional(readOnly = true)
    public Fabrica getOne(UUID id) {
        Optional<Fabrica> f = fabricaRepositorio.findById(id);
        if (f.isPresent()) {
            Fabrica fabrica = f.get();
            return fabrica;
        }
        return null;
    }
    

    // validations
    // private void validarNombre(String nombre) {
    //     if (nombre == null || nombre.trim().isEmpty()) {
    //         throw new IllegalArgumentException("El nombre de la fábrica no puede estar vacío.");
    //     }
    //     if (nombre.length() > 100) { // Adjust based on DB constraints
    //         throw new IllegalArgumentException("El nombre de la fábrica no puede superar los 100 caracteres.");
    //     }
    // }
}
