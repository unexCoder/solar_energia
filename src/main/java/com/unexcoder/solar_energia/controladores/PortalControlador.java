package com.unexcoder.solar_energia.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.servicios.UsuarioServicio;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/index")
    public String index() {
        return "index.html";
        // return "redirect:/login";
    }
    
    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form","users");
        return "form.html";
    }
    
    @PostMapping("/registro")
    public String registro(ModelMap model) {
        model.put("form","users");
        return "form.html";
    }
}
