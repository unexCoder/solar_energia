package com.unexcoder.solar_energia.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.entidades.Usuario;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.servicios.FabricaServicio;
import com.unexcoder.solar_energia.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private FabricaServicio fabricaServicio;

    @GetMapping("/")
    public String index(ModelMap model, HttpSession session) {
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);
        return "index.html";
    }

    @PreAuthorize("!isAuthenticated() || hasRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form", "users");
        return "form.html";
    }

    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String apellido,
            @RequestParam String nombre, @RequestParam String password,
            @RequestParam String passwordRepeat, @RequestParam MultipartFile file,
            ModelMap model) {
        
        model.put("form", "users");
        
        // Add these to preserve form data on error
        model.addAttribute("email", email);
        model.addAttribute("apellido", apellido);
        model.addAttribute("nombre", nombre);
        
        try {
            usuarioServicio.crearUsuario(email, nombre, apellido, password, passwordRepeat, file);
            model.put("exito", "Usuario registrado correctamente");
            // Clear form data after success
            model.addAttribute("email", null);
            model.addAttribute("apellido", null);
            model.addAttribute("nombre", null);
            return "form.html";
        } catch (ValidationException | InvalidOperationException e) {
            model.put("error", e.getMessage());
            return "form.html";
        } catch (Exception e) {
            model.put("error", "Error inesperado al registrar usuario");
            return "form.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o Contraseña inválidos!");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logged = (Usuario) session.getAttribute("sessionUser");
        System.out.println(logged.getId());
        if (logged.getRol().toString().equals("ADMIN")) {
            // return "redirect:/admin/dashboard";
            return "redirect:/";
        }
        return "redirect:/";
    }
}
