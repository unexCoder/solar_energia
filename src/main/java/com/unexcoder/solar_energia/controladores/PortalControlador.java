package com.unexcoder.solar_energia.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.servicios.FabricaServicio;
import com.unexcoder.solar_energia.servicios.UsuarioServicio;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private FabricaServicio fabricaServicio;
    
    @GetMapping("/index")
    public String index(ModelMap model) {
        List<Fabrica> fabricas = fabricaServicio.listarFabricas();
        model.addAttribute("fabricas", fabricas);
        return "index.html";
        // return "redirect:/login";
    }
    
    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        model.put("form","users");
        return "form.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String apellido,
                @RequestParam String nombre,@RequestParam String password, 
                @RequestParam String passwordRepeat,@RequestParam MultipartFile  file,
                ModelMap model) throws InvalidOperationException {
        model.put("form","users");
        try {
            usuarioServicio.crearUsuario(email, nombre, apellido, password, passwordRepeat, file);
            model.put("exito","Usuario registrado correctamente");
            return "form.html";            
        } catch (ValidationException e) {
            model.put("error",e.getMessage());
            model.put("email",email);
            model.put("apellido",apellido);
            model.put("nombre",nombre);
            model.put("password",password);
            return "form.html";  
        }
    }
}
