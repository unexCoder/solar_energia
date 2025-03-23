package com.unexcoder.solar_energia.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.excepciones.UserNotFoundException;
import com.unexcoder.solar_energia.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("dashboard/usuarios/{id}")
    public String cambiarRol(@PathVariable String id) throws UserNotFoundException {
        usuarioServicio.cambiarRol(UUID.fromString(id));
        // return "redirect:/admin/dashboard/usuarios";
        return "redirect:/usuarios/lista";
    }
}
