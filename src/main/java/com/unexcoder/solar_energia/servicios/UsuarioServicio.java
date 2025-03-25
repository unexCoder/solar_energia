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
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.UserNotFoundException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.repositorios.UsuarioRepositorio;
import com.unexcoder.solar_energia.utilities.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    // private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif");

    UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }
    
    @Transactional
    public void crearUsuario(String email, String nombre, String apellido, String password, String passwordRepeat,
            MultipartFile file) throws ValidationException, InvalidOperationException{

        // validations
        ValidationUtils.validarNombreUsuario(nombre, apellido, email, password, passwordRepeat);
        Usuario user = usuarioRepositorio.buscarPorEmail(email);
        if (user != null) {
            throw new ValidationException("El email ya está en uso.");
        }        

        Usuario newUser = new Usuario();
        newUser.setEmail(email);
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        // newUser.setPassword(password);
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        newUser.setRol(LoginRol.USER);
        if (file != null && !file.isEmpty()) {
            ValidationUtils.validarArchivo(file, MAX_FILE_SIZE, ALLOWED_MIME_TYPES);
            Imagen img = imagenServicio.guardarImagen(file, "profile_pic");
            newUser.setImagen(img);
        } // If no file, imagen remains null
        usuarioRepositorio.save(newUser);
    }

    @Transactional
    public void editarUsuario(UUID id, String email, String nombre, String apellido, String password,
            String passwordRepeat, MultipartFile file) throws ValidationException, UserNotFoundException {
        
        ValidationUtils.validarNombreUsuario(nombre, apellido, email, password, passwordRepeat);  
        Usuario user = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No se encontró el usuario con el ID: " + id));
        
        user.setEmail(email);
        user.setNombre(nombre);
        user.setApellido(apellido);
        // user.setPassword(password);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        if (file != null && !file.isEmpty()) {
            ValidationUtils.validarArchivo(file, MAX_FILE_SIZE, ALLOWED_MIME_TYPES);
            if (user.getImagen() != null) {
                user.setImagen(imagenServicio.actualizarImagen(user.getImagen().getId(), file, "profile_pic"));
            } else {
                user.setImagen(imagenServicio.guardarImagen(file, "profile_pic"));
            }
        }
        usuarioRepositorio.save(user);
    }

    @Transactional
    public void borrarUsuario(UUID id) throws UserNotFoundException {
        if (!usuarioRepositorio.existsById(id)) {
            throw new UserNotFoundException("No se encontró el usuario con el ID: " + id);
        }
        usuarioRepositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional
    public void cambiarRol(UUID id) throws UserNotFoundException {
        Usuario user = usuarioRepositorio.findById(id)
            .orElseThrow(() -> new UserNotFoundException("No se encontró el usuario con el ID: " + id));
        user.setRol(user.getRol() == LoginRol.ADMIN ? LoginRol.USER : LoginRol.ADMIN);
        usuarioRepositorio.save(user);
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

    // validations and service methoids
    // private void validar(String nombre,String apellido, String email, String password, String passwordRepeat)
    //         throws ValidationException {
    //     if (nombre.isEmpty() || nombre == null || nombre.length() < 2) {
    //         throw new ValidationException("El campo nombre debe ser correcto");
    //     }
    //     if (apellido.isEmpty() || nombre == null) {
    //         throw new ValidationException("El campo apellido debe ser correcto");
    //     }
    //     if (email.isEmpty() || email == null) {
    //         throw new ValidationException("El campo email ser correcto");
    //     }
    //     if (password.isEmpty() || password == null || password.length() < 8) {
    //         throw new ValidationException("El campo password debe ser contener al menos 8 caracteres");
    //     }
    //     if (passwordRepeat.isEmpty() || passwordRepeat == null || !passwordRepeat.equals(password)) {
    //         throw new ValidationException("El password debe coincidir con el anterior");
    //     }
    // }

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

}
