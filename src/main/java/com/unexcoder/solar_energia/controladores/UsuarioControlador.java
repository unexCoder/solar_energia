package com.unexcoder.solar_energia.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.unexcoder.solar_energia.entidades.Usuario;
import com.unexcoder.solar_energia.excepciones.UserNotFoundException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap model) {
        List<Usuario> users = usuarioServicio.listarUsuarios();
        model.put("list", "users");
        model.addAttribute("users", users);
        return "list.html";
    }
    
    // implementar autorizacion por id de usuario uutenticado
    // @PreAuthorize("#id == authentication.principal.id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/perfil/editar/{id}")
    public String editar(@PathVariable String id, ModelMap model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (!userId.equals(UUID.fromString(id))) {
            // throw new SecurityException("Unauthorized access");
            // redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este perfil");
            return "redirect:/inicio";  // redirect to inicio
        }
        model.put("edit", "users");
        model.put("user",usuarioServicio.getOne(UUID.fromString(id)));
        return "form.html";
    }
    // implementar autorizacion por id de usuario uutenticado
    
    // @PreAuthorize("#id == authentication.principal.id")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/perfil/editar/{id}")
    public String editado(@PathVariable String id,@RequestParam String email, @RequestParam String apellido,
                @RequestParam String nombre,@RequestParam String password, 
                @RequestParam String passwordRepeat,@RequestParam MultipartFile  file,
                ModelMap model) throws UserNotFoundException {
        model.put("edit", "users");
        try {
            usuarioServicio.editarUsuario(UUID.fromString(id), email, nombre, apellido, password, passwordRepeat, file);
            model.put("exito","Usuario editado correctamente");
            return "redirect:/usuarios/lista";
        } catch (ValidationException e) {
            model.put("error",e.getMessage());
            model.put("user",usuarioServicio.getOne(UUID.fromString(id)));
            return "form.html";
        }
    }
    
}
