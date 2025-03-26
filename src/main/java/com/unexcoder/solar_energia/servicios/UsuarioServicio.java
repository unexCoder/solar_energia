package com.unexcoder.solar_energia.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.entidades.Imagen;
import com.unexcoder.solar_energia.entidades.Usuario;
import com.unexcoder.solar_energia.enumeraciones.LoginRol;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.UserNotFoundException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.repositorios.UsuarioRepositorio;
import com.unexcoder.solar_energia.utilities.ValidationUtils;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@Service
public class UsuarioServicio implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    // private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif");

    UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepositorio.buscarPorEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con el email: " + email));

        List<GrantedAuthority> permisos = new ArrayList<>();
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
        permisos.add(p);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("userId", user.getId()); // Store user ID in session
        session.setAttribute("sessionUser", user);    // Store full user object if needed
        
        return new User(user.getEmail(), user.getPassword(), permisos);
    }
    
    @Transactional
    public void crearUsuario(String email, String nombre, String apellido, String password, String passwordRepeat,
            MultipartFile file) throws ValidationException, InvalidOperationException{

        // validations
        ValidationUtils.validarNombreUsuario(nombre, apellido, email, password, passwordRepeat);
        usuarioRepositorio.buscarPorEmail(email)
            .orElseThrow(() -> new ValidationException("El email ya está en uso."));

        Usuario newUser = new Usuario();
        newUser.setEmail(email);
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
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

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) throws UserNotFoundException {
        return usuarioRepositorio.buscarPorEmail(email)
            .orElseThrow(() -> new UserNotFoundException("No se encontró el usuario con el email: " + email));
    }

}
