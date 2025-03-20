package com.unexcoder.solar_energia.controladores;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErroresControlador implements ErrorController {
    @RequestMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView modelAndView = new ModelAndView("error"); // Vista corregida a "error"
        int httpErrorCode = getErrorCode(httpRequest);
        String errorMsg = obtenerMensajeError(httpErrorCode);
        modelAndView.addObject("codigo", httpErrorCode);
        modelAndView.addObject("mensaje", errorMsg);
        return modelAndView;
    }

    private int getErrorCode(HttpServletRequest httpRequest) { 
        Object statusCode = httpRequest.getAttribute("jakarta.servlet.error.status_code"); 
        return (statusCode instanceof Integer) ? (Integer) statusCode : -1;
    } 

    private String obtenerMensajeError(int codigo) {
        return switch (codigo) {
            case 400 -> "El recurso solicitado no existe.";
            case 401 -> "No se encuentra autorizado.";
            case 403 -> "No tiene permisos para acceder al recurso.";
            case 404 -> "El recurso solicitado no fue encontrado.";
            case 500 -> "Ha ocurrido un error interno.";
            default -> "Se produjo un error inesperado.";
        };
    }

    // // Método que devuelve la ruta a la página de error 
    // No es necesario en Spring Boot 3.x, ya que Spring maneja automáticamente el /error.
    // public String getErrorPath() { 
    //     return "/error.html"; 
    // } 
}
