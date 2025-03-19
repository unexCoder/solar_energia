package com.unexcoder.solar_energia.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.entidades.Imagen;
import com.unexcoder.solar_energia.entidades.Usuario;
import com.unexcoder.solar_energia.enumeraciones.LoginRol;
import com.unexcoder.solar_energia.repositorios.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UsuarioServicio {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

    @Transactional
    public void crearUsuario(String email, String nombre, String apellido, String password, String passwordRepeat, MultipartFile file) {
        Usuario newUser = new Usuario(); 
        newUser.setEmail(email);
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        // newUser.setPassword(password);
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        newUser.setRol(LoginRol.USER);
        if (file != null && !file.isEmpty()) {
            Imagen img = imagenServicio.guardarImagen(file,"profile_pic");
            newUser.setImagen(img);
        } // If no file, imagen remains null
        usuarioRepositorio.save(newUser);
    }
    
    @Transactional
    public void editarUsuario(UUID id, String email, String nombre, String apellido, String password, String passwordRepeat, MultipartFile file) {
        Usuario user = usuarioRepositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con el ID: " + id));
        user.setEmail(email);
        user.setNombre(nombre);
        user.setApellido(apellido);
        // user.setPassword(password);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        UUID idImagen = null;
            if (user.getImagen() != null) {
                idImagen = user.getImagen().getId();
            }
            Imagen img = imagenServicio.actualizarImagen(idImagen, file, "profile_pic");
            user.setImagen(img);
        usuarioRepositorio.save(user);
    }
    
    @Transactional
    public void borrarUsuario(UUID id) {
        usuarioRepositorio.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }
    
    @Transactional
    public void cambiarRol(UUID id) {
        Optional<Usuario> u = usuarioRepositorio.findById(id);
        if (u.isPresent()) {
            Usuario user = u.get();
            if (user.getRol().equals(LoginRol.ADMIN)) {
                user.setRol(LoginRol.USER);
            } else if (user.getRol().equals(LoginRol.USER)) {
                user.setRol(LoginRol.ADMIN);
            }
        }
    }

    @Transactional(readOnly = true)
    public Usuario getOne(UUID id) {
        Optional<Usuario> u = usuarioRepositorio.findById(id);
        if (u.isPresent()) {
            Usuario user = u.get();
            return user;
        }
        return null;
    }

}
