package com.unexcoder.solar_energia.utilities;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.repositorios.FabricaRepositorio;

public class ValidationUtils {
    public static void validarArchivo(MultipartFile file, long maxSize, List<String> allowedMimeTypes) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío.");
        }
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("El tamaño del archivo no debe superar los " + maxSize + " bytes.");
        }
        if (!allowedMimeTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Formato de archivo no permitido.");
        }
    }

    public static void validarArticulo(String nombre, String descripcion, UUID fabricaId,
            FabricaRepositorio fabricaRepositorio) throws ValidationException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("El nombre del artículo no puede estar vacío.");
        }
        if (nombre.length() > 255) {
            throw new ValidationException("El nombre del artículo no puede superar los 255 caracteres.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ValidationException("La descripción del artículo no puede estar vacía.");
        }
        if (descripcion.length() > 1000) {
            throw new ValidationException("La descripción del artículo no puede superar los 1000 caracteres.");
        }
        if (fabricaId == null || !fabricaRepositorio.existsById(fabricaId)) {
            throw new ValidationException("Debe especificar una fábrica válida.");
        }
    }

    public static String sanitizarDescripcion(String descripcion, int maxChars) {
        if (descripcion != null) {
            descripcion = descripcion.trim();
            if (descripcion.length() > maxChars) {
                throw new IllegalArgumentException("La descripción no puede superar los " + maxChars + " caracteres.");
            }
            return descripcion.replaceAll("<.*?>", ""); // Remove HTML tags
        }
        return "";
    }

    public static void validarNombreFabrica(String nombre,FabricaRepositorio fabricaRepositorio) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la fábrica no puede estar vacío.");
        }
        if (nombre.length() > 100) { // Adjust based on DB constraints
            throw new IllegalArgumentException("El nombre de la fábrica no puede superar los 100 caracteres.");
        }
        if (fabricaRepositorio.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una fábrica con el mismo nombre.");
        }
    }

    public static void validarNombreUsuario(String nombre,String apellido, String email, String password, String passwordRepeat)
            throws ValidationException {
        if (nombre.isEmpty() || nombre == null || nombre.length() < 2) {
            throw new ValidationException("El campo nombre debe ser correcto");
        }
        if (apellido.isEmpty() || nombre == null) {
            throw new ValidationException("El campo apellido debe ser correcto");
        }
        if (email.isEmpty() || email == null) {
            throw new ValidationException("El campo email ser correcto");
        }
        if (password.isEmpty() || password == null || password.length() < 8) {
            throw new ValidationException("El campo password debe ser contener al menos 8 caracteres");
        }
        if (passwordRepeat.isEmpty() || passwordRepeat == null || !passwordRepeat.equals(password)) {
            throw new ValidationException("El password debe coincidir con el anterior");
        }
    }

}