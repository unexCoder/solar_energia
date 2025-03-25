package com.unexcoder.solar_energia.servicios;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.unexcoder.solar_energia.entidades.Articulo;
import com.unexcoder.solar_energia.entidades.Fabrica;
import com.unexcoder.solar_energia.entidades.Imagen;
import com.unexcoder.solar_energia.excepciones.InvalidOperationException;
import com.unexcoder.solar_energia.excepciones.ValidationException;
import com.unexcoder.solar_energia.excepciones.NotFoundException;
import com.unexcoder.solar_energia.repositorios.ArticuloRepositorio;
import com.unexcoder.solar_energia.repositorios.FabricaRepositorio;
import com.unexcoder.solar_energia.utilities.ValidationUtils;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ArticuloServicio {

    // @Autowired
    private final FabricaRepositorio fabricaRepositorio;
    // @Autowired
    private final ArticuloRepositorio articuloRepositorio;
    // @Autowired
    private final ImagenServicio imagenServicio;

    private static final Logger logger = LoggerFactory.getLogger(ArticuloServicio.class);
    // private final AtomicInteger counter = new AtomicInteger(1); // Starts from 1
    private final AtomicInteger counter;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif");

    
    public ArticuloServicio(ArticuloRepositorio articuloRepositorio, FabricaRepositorio fabricaRepositorio, ImagenServicio imagenServicio) {
        this.articuloRepositorio = articuloRepositorio;
        this.fabricaRepositorio = fabricaRepositorio;
        this.imagenServicio = imagenServicio;
        this.counter = new AtomicInteger(initCounter());
    }

    @Transactional
    public void crearArticulo(String nombre, String descripcion, MultipartFile file, UUID fabricaId) throws ValidationException {
        // validations
        // validar(nombre, descripcion, fabricaId);
        // validarArchivo(file);
        ValidationUtils.validarArticulo(nombre, descripcion, fabricaId, fabricaRepositorio);
        ValidationUtils.validarArchivo(file, MAX_FILE_SIZE, ALLOWED_MIME_TYPES);
       
        try {
            Articulo articulo = new Articulo();
            articulo.setNroArticulo(generateId());
            articulo.setNombreArticulo(nombre);
            articulo.setDescripcion(descripcion);
            articulo.setFabrica(fabricaRepositorio.getReferenceById(fabricaId));
            if (file != null && !file.isEmpty()) {
                Imagen img = imagenServicio.guardarImagen(file, sanitizarDescripcion(descripcion,500));
                articulo.setImagen(img);            
            }
            articuloRepositorio.save(articulo);
        } catch (Exception e) {
            logger.error("Error al crear el artículo: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear el artículo", e);
        }
    }

    @Transactional
    public void editarArticulo(UUID id ,String nombre, String descripcion, UUID fabricaId, MultipartFile file, boolean eliminarImagen) throws ValidationException,NotFoundException,InvalidOperationException {
        ValidationUtils.validarArticulo(nombre, descripcion, fabricaId, fabricaRepositorio);

        Articulo articulo = articuloRepositorio.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró el artículo con el ID: " + id));
            
        if (file != null && !file.isEmpty()) {
            ValidationUtils.validarArchivo(file, MAX_FILE_SIZE, ALLOWED_MIME_TYPES);
        }

        try {
            // Handle image removal
            if (eliminarImagen) {
                Optional.ofNullable(articulo.getImagen())
                        .ifPresent(img -> imagenServicio.eliminarImagen(img.getId()));
                articulo.setImagen(null);
            }
            // Update image if new file provided
            if (file != null && !file.isEmpty()) {
                Optional.ofNullable(articulo.getImagen())
                        .ifPresent(img -> imagenServicio.eliminarImagen(img.getId()));
                Imagen nuevaImg = imagenServicio.guardarImagen(file, descripcion);
                articulo.setImagen(nuevaImg);
            }    
            articulo.setNombreArticulo(nombre);
            articulo.setDescripcion(descripcion);
            Fabrica f = fabricaRepositorio.getReferenceById(fabricaId);
            articulo.setFabrica(f);
            articuloRepositorio.save(articulo);
        } catch (Exception e) {
            logger.error("Error actualizando artículo ID {}: {}", id, e.getMessage(), e);
            throw new InvalidOperationException("Error al actualizar el artículo", e);
        }
        
    }

    @Transactional
    public void borrarArticulo(UUID id) throws InvalidOperationException {
        Articulo articulo = articuloRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado con ID: " + id));
        try {
            Optional.ofNullable(articulo.getImagen())
                    .ifPresent(img -> {
                        imagenServicio.eliminarImagen(img.getId());
                    });
            articuloRepositorio.delete(articulo);
            logger.info("Artículo eliminado con ID: {}", id);
        } catch (Exception e) {
            logger.error("Error eliminando artículo ID {}: {}", id, e.getMessage(), e);
            throw new InvalidOperationException("Error al eliminar el artículo", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Articulo> listarArticulos() {
        return articuloRepositorio.findAll();
    }
    @Transactional(readOnly = true)
    public Page<Articulo> listarArticulosPaginados(int page, int size) {
        logger.info("Listando artículos - Página: {}, Tamaño: {}", page, size);
        return articuloRepositorio.findAll(PageRequest.of(page, size));
    }
    @Transactional(readOnly = true)
    public Articulo getOne(UUID id) throws NotFoundException {
        return articuloRepositorio.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el artículo con el ID: " + id));
    } 
    
    // Initialized Counter from Database
    // Instead of always starting at 1, it now retrieves the highest nroArticulo 
    // from the database and continues from there.
    private Integer initCounter() {
        // return articuloRepositorio.findAll().stream()
        //         .mapToInt(Articulo::getNroArticulo)
        //         .max()
        //         .orElse(0) + 1; // Start from the next available number
        Integer maxNroArticulo =  articuloRepositorio.findMaxNroArticulo();
        return (maxNroArticulo != null ? maxNroArticulo : 0) + 1;
    }

    private synchronized Integer generateId() {
        return counter.getAndIncrement(); // Returns current value and increments
    }


    // private void validarArchivo(MultipartFile file) {
    //     if (file == null || file.isEmpty()) {
    //         throw new IllegalArgumentException("El archivo de imagen no puede estar vacío.");
    //     }
    //     if (file.getSize() > MAX_FILE_SIZE) {
    //         throw new IllegalArgumentException("El tamaño del archivo no debe superar los 5MB.");
    //     }
    //     if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
    //         throw new IllegalArgumentException("Formato de archivo no permitido. Solo se permiten JPG, PNG y GIF.");
    //     }
    // }
    
    private String sanitizarDescripcion(String descripcion, int maxChars) {
        if (descripcion != null) {
            descripcion = descripcion.trim();
            if (descripcion.length() > maxChars) {
                throw new IllegalArgumentException("La descripción no puede superar los " + maxChars +" caracteres.");
            }
            return descripcion.replaceAll("<.*?>", ""); // Remove HTML tags
        }
        return "";
    }

    // private void validar(String nombre, String descripcion, UUID fabricaId) throws ValidationException {
    //     if (nombre == null || nombre.trim().isEmpty()) {
    //         throw new ValidationException("El nombre del artículo no puede estar vacío.");
    //     }
    //     if (nombre.length() > 255) {
    //         throw new ValidationException("El nombre del artículo no puede superar los 255 caracteres.");
    //     }
    //     if (descripcion == null || descripcion.trim().isEmpty()) {
    //         throw new ValidationException("La descripción del artículo no puede estar vacía.");
    //     }
    //     if (descripcion.length() > 1000) {
    //         throw new ValidationException("La descripción del artículo no puede superar los 1000 caracteres.");
    //     }
    //     if (fabricaId == null || !fabricaRepositorio.existsById(fabricaId))  {
    //         throw new ValidationException("Debe especificar una fábrica válida.");
    //     }
    // }



    /*
    to consider for further refinement:

    Optimizing initCounter()
    Instead of findAll().stream(), consider using a query to fetch the max nroArticulo 
    directly from the database. This will be more efficient, especially if your table 
    grows large.

    Exception Handling Consistency
    
    In borrarArticulo(), you're still using IllegalArgumentException, while other 
    methods use NotFoundException and InvalidOperationException. It might be good to standardize this.
    The RuntimeException in crearArticulo() could be replaced with 
    InvalidOperationException.
    Transaction Read-Only for Fetching

    Your listarArticulosPaginados() method correctly uses @Transactional(readOnly = true),
     but initCounter() could also benefit from it.
    */
}
