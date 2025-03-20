package com.unexcoder.solar_energia.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.servicios.ArticuloServicio;

@Controller
@RequestMapping("/articulo")
public class ArticuloControlador {
    
    @Autowired
    ArticuloServicio articuloServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "articulo.html";
    }

    @PostMapping("/registro")
    public String registro() {
        return "articulo.html";
    }
}
