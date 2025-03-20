package com.unexcoder.solar_energia.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.servicios.ArticuloServicio;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/articulo")
public class ArticuloControlador {
    
    @Autowired
    ArticuloServicio articuloServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form","articulo");
        return "form.html";
    }
    
    @PostMapping("/registro")
    public String registro(ModelMap model) {
        model.put("form","articulo");
        return "form.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap model) {
        model.put("list","articulo");
        return "list.html";
    }
    
    @GetMapping("/editar")
    public String editar(ModelMap model) {
        model.put("edit","articulo");
        return "edit.html";
    }
    
    @PostMapping("/editar")
    public String editado(ModelMap model) {
        model.put("edit","articulo");
        return "edit.html";
    }    
}
