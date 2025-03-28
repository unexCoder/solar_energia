package com.unexcoder.solar_energia.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unexcoder.solar_energia.entidades.Articulo;

@Repository
public interface ArticuloRepositorio extends JpaRepository<Articulo, UUID> {
   
    @Query("SELECT MAX(a.nroArticulo) FROM Articulo a")
    Integer findMaxNroArticulo();
}
