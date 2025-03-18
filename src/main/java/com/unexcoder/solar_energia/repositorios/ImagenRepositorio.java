package com.unexcoder.solar_energia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.unexcoder.solar_energia.entidades.Imagen;
import java.util.UUID;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen,UUID>{
    
}