package com.unexcoder.solar_energia.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.servicios.ArticuloServicio;
import com.unexcoder.solar_energia.servicios.FabricaServicio;

// import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/articulo")
public class ArticuloControlador {
    
    @Autowired
    ArticuloServicio articuloServicio;
 
    @Autowired
    FabricaServicio fabricaServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form","articulo");
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);
        return "form.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,@RequestParam String descripcion,
                @RequestParam MultipartFile file,@RequestParam String fabricaId, ModelMap model) {
        model.put("form","articulo");
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        try {
            articuloServicio.crearArticulo(nombre, descripcion, file, UUID.fromString(fabricaId));
            model.put("exito","Artículo registrado correctamente");
            return "form.html";
        } catch (ValidationException e) {
            model.put("error",e.getMessage());
            model.addAttribute("nombre",nombre);
            model.addAttribute("descripcion",descripcion);
            model.addAttribute("fabricas",fabricas);
            model.addAttribute("fabrica",fabricaId);
            model.addAttribute("file",file);
            return "form.html";
        } catch (IllegalArgumentException e) {
            // model.put("error",e.getMessage());
            model.put("error","El campo fabricante es incorrecto " + e.getMessage());
            return "form.html";
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap model) {
        model.put("list","articulo");
        return "list.html";
    }
    
    @GetMapping("/editar")
    public String editar( ModelMap model) {
        model.put("edit","articulo");
        return "edit.html";
    }
    
    @PostMapping("/editar")
    public String editado(ModelMap model) {
        model.put("edit","articulo");
        return "edit.html";
    }    
}
