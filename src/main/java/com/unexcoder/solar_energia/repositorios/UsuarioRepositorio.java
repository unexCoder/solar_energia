package com.unexcoder.solar_energia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import com.unexcoder.solar_energia.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository <Usuario, UUID> {
    @Query ("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmail(@Param("email") String email);
}