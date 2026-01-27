package org.example.pfcursos.controller;

import jakarta.validation.Valid;
import org.example.pfcursos.dto.AlumnoRequest;
import org.example.pfcursos.dto.AlumnoResponse;
import org.example.pfcursos.servicios.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping
    public List<AlumnoResponse> getAllAlumnos() {
        return alumnoService.findAll();
    }

    @GetMapping("/paginated")
    public Page<AlumnoResponse> getAllAlumnosPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return alumnoService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponse> getAlumnoById(@PathVariable Long id) {
        return alumnoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<AlumnoResponse> getAlumnoByCorreo(@PathVariable String correo) {
        return alumnoService.findByCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AlumnoResponse createAlumno(@Valid @RequestBody AlumnoRequest request) {
        return alumnoService.saveAlumno(request);
    }

    @PutMapping("/{id}")
    public AlumnoResponse updateAlumno(@PathVariable Long id, @Valid @RequestBody AlumnoRequest request) {
        return alumnoService.updateAlumno(id, request);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteAlumno(@PathVariable Long id) {
        alumnoService.deleteById(id);
        return Map.of("message", "Alumno eliminado exitosamente");
    }

    @GetMapping("/exists/{id}")
    public Map<String, Boolean> existsById(@PathVariable Long id) {
        return Map.of("exists", alumnoService.existsById(id));
    }

    @GetMapping("/exists/correo/{correo}")
    public Map<String, Boolean> existsByCorreo(@PathVariable String correo) {
        return Map.of("exists", alumnoService.existsByCorreo(correo));
    }

    @GetMapping("/count")
    public Map<String, Long> countAlumnos() {
        return Map.of("total", alumnoService.countAlumnos());
    }
}
