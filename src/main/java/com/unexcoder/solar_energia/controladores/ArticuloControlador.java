package com.unexcoder.solar_energia.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.entidades.Articulo;
import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.NotFoundException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.servicios.ArticuloServicio;
import com.unexcoder.solar_energia.servicios.FabricaServicio;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/articulo")
public class ArticuloControlador {
    
    @Autowired
    ArticuloServicio articuloServicio;
 
    @Autowired
    FabricaServicio fabricaServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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
        // List<Articulo> productos = articuloServicio.listarArticulosPaginados(1, 10).getContent();
        List<Articulo> productos = articuloServicio.listarArticulos();
        model.put("list","articulo");
        model.addAttribute("articulos",productos);
        return "list.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar/{id}")
    public String editar( @PathVariable String id, ModelMap model) throws NotFoundException {
        model.put("edit","articulo");
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);
        model.put("producto",articuloServicio.getOne(UUID.fromString(id)));
        return "form.html";
    }
    
    @PostMapping("/editar/{id}")
    public String editado(@PathVariable String id, @RequestParam String nombre,@RequestParam String descripcion,
    @RequestParam MultipartFile file,@RequestParam String fabricaId,ModelMap model) throws ValidationException, NotFoundException, InvalidOperationException {
        articuloServicio.editarArticulo(UUID.fromString(id),nombre, descripcion, UUID.fromString(fabricaId), file, false);
        model.put("edit","articulo");
        return "redirect:/articulo/lista";
    }    
}
