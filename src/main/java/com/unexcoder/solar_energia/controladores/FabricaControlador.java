package com.unexcoder.solar_energia.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.servicios.FabricaServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/fabrica")
public class FabricaControlador {
    
    @Autowired
    private FabricaServicio fabricaServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form","fabrica");
        return "form.html";
    }

    @PostMapping("/registro")
    public String registro(ModelMap model) {
        model.put("form","fabrica");
        return "form.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap model) {
        model.put("list","fabrica");
        return "list.html";
    }
    
    @GetMapping("/editar")
    public String editar(ModelMap model) {
        model.put("edit","fabrica");
        return "edit.html";
    }
    
    @PostMapping("/editar")
    public String editado(ModelMap model) {
        model.put("edit","fabrica");
        return "edit.html";
    }
    
}
