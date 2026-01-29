package org.example.pfcursos.controller;

import jakarta.validation.Valid;
import org.example.pfcursos.dto.ProfesorRequest;
import org.example.pfcursos.dto.ProfesorResponse;
import org.example.pfcursos.servicios.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping
    public List<ProfesorResponse> getAllProfesores() {
        return profesorService.findAll();
    }

    @GetMapping("/paginacion")
    public List<ProfesorResponse> getAllProfesoresPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profesorService.findAll(PageRequest.of(page, size)).getContent();
    }

    @GetMapping("/{id}")
    public ProfesorResponse getProfesorById(@PathVariable Long id) {
        return profesorService.findById(id).orElse(null);
    }

    @GetMapping("/activos")
    public List<ProfesorResponse> getProfesoresActivos() {
        return profesorService.findProfesoresActivos();
    }

    @PostMapping
    public ProfesorResponse createProfesor(@Valid @RequestBody ProfesorRequest request) {
        return profesorService.saveProfesor(request);
    }

    @PutMapping("/{id}")
    public ProfesorResponse updateProfesor(@PathVariable Long id, @Valid @RequestBody ProfesorRequest request) {
        return profesorService.updateProfesor(id, request);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProfesor(@PathVariable Long id) {
        profesorService.deleteById(id);
        return Map.of("message", "Profesor eliminado exitosamente");
    }


    @GetMapping("/existe/correo/{correo}")
    public Map<String, Boolean> existsByCorreo(@PathVariable String correo) {
        return Map.of("exists", profesorService.existsByCorreo(correo));
    }

    @GetMapping("/{id}/activo")
    public Map<String, Boolean> isActivo(@PathVariable Long id) {
        return Map.of("activo", profesorService.isActivo(id));
    }

    @GetMapping("/count")
    public Map<String, Long> countProfesores() {
        return Map.of("total", profesorService.countProfesores());
    }

    @GetMapping("/count/activos")
    public Map<String, Long> countProfesoresActivos() {
        return Map.of("total_activos", profesorService.countProfesoresActivos());
    }
}
