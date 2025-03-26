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

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form", "users");
        return "form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String apellido,
            @RequestParam String nombre, @RequestParam String password,
            @RequestParam String passwordRepeat, @RequestParam MultipartFile file,
            ModelMap model) throws InvalidOperationException {
        model.put("form", "users");
        try {
            usuarioServicio.crearUsuario(email, nombre, apellido, password, passwordRepeat, file);
            model.put("exito", "Usuario registrado correctamente");
            return "form.html";
        } catch (ValidationException e) {
            model.put("error", e.getMessage());
            model.put("email", email);
            model.put("apellido", apellido);
            model.put("nombre", nombre);
            model.put("password", password);
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
            return "index.html";
        }
        return "index.html";
    }
}
