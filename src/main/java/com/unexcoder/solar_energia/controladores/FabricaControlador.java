package com.unexcoder.solar_energia.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.servicios.FabricaServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String registro(@RequestParam String nombre,@RequestParam MultipartFile file,
                    ModelMap model) {
        model.put("form","fabrica");
        try {
            fabricaServicio.guardarFabrica(nombre, file);
            model.put("exito","Fabricante registrado correctamente");
            return "form.html";
        } catch (ValidationException e) {
            model.put("error",e.getMessage());
            return "form.html";
        } catch (InvalidOperationException e) {
            model.put("error",e.getMessage());
            return "form.html";
        } catch (IllegalArgumentException e) {
            model.put("error",e.getMessage());
            return "form.html";
        }
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap model) {
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        model.put("list","fabrica");
        model.addAttribute("fabricas", fabricas);
        return "list.html";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, ModelMap model) {
        model.put("edit","fabrica");
        model.put("fabrica",fabricaServicio.getOne(UUID.fromString(id)));
        return "form.html";
    }
    
    @PostMapping("/editado/{id}")
    public String editado(@PathVariable String id, @RequestParam String nombre,@RequestParam MultipartFile file,
    ModelMap model) {
        model.put("edit","fabrica");
        model.put("fabrica",fabricaServicio.getOne(UUID.fromString(id)));
        try {
            fabricaServicio.editarFabrica(UUID.fromString(id), nombre, file);
            model.put("fabrica",fabricaServicio.getOne(UUID.fromString(id)));
            return "redirect:/fabrica/lista";
        }  catch (ValidationException e) {
            model.put("error",e.getMessage());
            return "form.html";
        } catch (InvalidOperationException e) {
            model.put("error",e.getMessage());
            return "form.html";
        } catch (IllegalArgumentException e) {
            model.put("error",e.getMessage());
            return "form.html";
        }
    }
    
}
