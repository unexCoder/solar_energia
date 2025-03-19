package com.unexcoder.solar_energia.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.repositorios.FabricaRepositorio;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FabricaServicio {
    
    @Autowired
    private FabricaRepositorio fabricaRepositorio;
    private static final Logger logger = LoggerFactory.getLogger(FabricaServicio.class);

    @Transactional
    public void guardarFabrica(String nombre) {
        validarNombre(nombre);
        if (fabricaRepositorio.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una fábrica con el mismo nombre.");
        }
        try {
            Fabrica fabrica = new Fabrica();
            fabrica.setNombre(nombre);
            fabricaRepositorio.save(fabrica);            
        } catch (Exception e) {
            logger.error("Error al guardar la fábrica: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar la fábrica.", e);
        }
    }
    
    @Transactional
    public void editarFabrica(UUID id,String nombre) {
        validarNombre(nombre);
        Fabrica fabrica = fabricaRepositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No se encontró la fábrica con el ID: " + id));
        fabrica.setNombre(nombre.trim());
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

    // validations
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la fábrica no puede estar vacío.");
        }
        if (nombre.length() > 100) { // Adjust based on DB constraints
            throw new IllegalArgumentException("El nombre de la fábrica no puede superar los 100 caracteres.");
        }
    }
}
