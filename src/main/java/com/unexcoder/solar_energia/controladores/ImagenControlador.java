package com.unexcoder.solar_energia.controladores;

import org.springframework.http.HttpHeaders;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.entidades.Usuario;
import com.unexcoder.solar_energia.servicios.FabricaServicio;
import com.unexcoder.solar_energia.servicios.UsuarioServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private FabricaServicio fabricaServicio;

    @GetMapping("perfil/{id}")
    public ResponseEntity<byte[]> imagenUser(@PathVariable UUID id) {
        Usuario user = usuarioServicio.getOne(id);
        if (user == null || user.getImagen() == null || user.getImagen().getContenido() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] img = user.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or IMAGE_PNG if applicable
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> imagenBrand(@PathVariable UUID id) {
        Fabrica fabrica = fabricaServicio.getOne(id);
        if (fabrica == null || fabrica.getImagen() == null || fabrica.getImagen().getContenido() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] img = fabrica.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or IMAGE_PNG if applicable
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }
}
