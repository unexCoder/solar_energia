package com.unexcoder.solar_energia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.unexcoder.solar_energia.entidades.Imagen;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen,UUID>{

    Optional<Imagen> findByNombre(String nombre);

    @Query("SELECT i FROM Imagen i WHERE LOWER(i.nombre) = LOWER(:nombre) AND i.mime = :mime")
    Optional<Imagen> findByNombreAndMime(String nombre, String mime);

    // List<Imagen> findByMime(String mime);
}