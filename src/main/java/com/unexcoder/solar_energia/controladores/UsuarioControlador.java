package com.unexcoder.solar_energia.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.unexcoder.solar_energia.servicios.UsuarioServicio;


@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/lista")
    public String listar(ModelMap model) {
        model.put("list", "users");
        return "list.html";
    }
    
    
    @GetMapping("/editar")
    public String editar(ModelMap model) {
        model.put("edit", "users");
        return "edit.html";
    }
    
    @PostMapping("/editar")
    public String editado(ModelMap model) {
        model.put("edit", "users");
        return "edit.html";
    }
    
}
