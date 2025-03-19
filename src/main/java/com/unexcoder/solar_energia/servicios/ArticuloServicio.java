package com.unexcoder.solar_energia.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unexcoder.solar_energia.entidades.Articulo;
import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.entidades.Imagen;
import com.unexcoder.solar_energia.repositorios.ArticuloRepositorio;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ArticuloServicio {
    
    @Autowired
    private ArticuloRepositorio articuloRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;

    private static final Logger logger = LoggerFactory.getLogger(ArticuloServicio.class);
    private final AtomicInteger counter = new AtomicInteger(1); // Starts from 1

    @Transactional
    public void crearArticulo(String nombre, String descripcion, MultipartFile file, Fabrica fabrica) {
        Articulo articulo = new Articulo();
        articulo.setNroArticulo(generateId());
        articulo.setNombreArticulo(nombre);
        articulo.setDescripcion(descripcion);
        articulo.setFabrica(fabrica);
        Imagen img = imagenServicio.guardarImagen(file, descripcion);
        articulo.setImagen(img);
        articuloRepositorio.save(articulo);
    }

    @Transactional
    public void editarArticulo(UUID id,Integer numArticulo ,String nombre, String descripcion, MultipartFile file, Fabrica fabrica) {

        Articulo articulo = articuloRepositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No se encontró el artículo con el ID: " + id));
        articulo.setNombreArticulo(nombre);
        articulo.setDescripcion(descripcion);
        articulo.setFabrica(fabrica);
        Imagen img = imagenServicio.guardarImagen(file, descripcion);
        articulo.setImagen(img);        
        articuloRepositorio.save(articulo);
    }

    @Transactional
    public void borrarArticulo(UUID id,Integer numArticulo) {
        articuloRepositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Articulo> listarArticulos() {
        List<Articulo> articulos = new ArrayList<>();
        articulos = articuloRepositorio.findAll();
        return articulos;
    }

    private Integer generateId() {
        return counter.getAndIncrement(); // Returns current value and increments
    }

}
