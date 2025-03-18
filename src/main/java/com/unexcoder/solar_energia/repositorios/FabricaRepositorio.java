package com.unexcoder.solar_energia.repositorios;
import com.unexcoder.solar_energia.entidades.Fabrica;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricaRepositorio extends JpaRepository<Fabrica,UUID>{
    
}
