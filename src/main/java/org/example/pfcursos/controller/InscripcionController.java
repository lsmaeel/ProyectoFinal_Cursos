package org.example.pfcursos.controller;

import jakarta.validation.Valid;
import org.example.pfcursos.dto.InscripcionRequest;
import org.example.pfcursos.dto.InscripcionResponse;
import org.example.pfcursos.servicios.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    public List<InscripcionResponse> getAllInscripciones() {
        return inscripcionService.findAll();
    }

    @GetMapping("/paginacion")
    public Page<InscripcionResponse> getAllInscripcionesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return inscripcionService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public InscripcionResponse getInscripcionById(@PathVariable Long id) {
        return inscripcionService.findById(id).orElse(null);
    }

    @GetMapping("/alumno/{idAlumno}")
    public Page<InscripcionResponse> getInscripcionesByAlumno(
            @PathVariable Long idAlumno,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return inscripcionService.findByAlumnoPaginado(idAlumno, PageRequest.of(page, size));
    }

    @GetMapping("/curso/{id}")
    public ResponseEntity<Page<InscripcionResponse>> getInscripcionesByCurso(
            @PathVariable Long id,
            Pageable pageable) {
        return ResponseEntity.ok(inscripcionService.findByCurso(id, pageable));
    }

    @PostMapping
    public InscripcionResponse createInscripcion(@Valid @RequestBody InscripcionRequest request) {
        return inscripcionService.saveInscripcion(request);
    }

    @PutMapping("/{id}")
    public InscripcionResponse updateInscripcion(
            @PathVariable Long id,
            @Valid @RequestBody InscripcionRequest request) {
        return inscripcionService.updateInscripcion(id, request);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteInscripcion(@PathVariable Long id) {
        inscripcionService.deleteById(id);
        return Map.of("message", "Inscripción eliminada exitosamente");
    }


    @GetMapping("/existe/alumno/{idAlumno}/curso/{idCurso}")
    public Map<String, Boolean> existsByAlumnoYCurso(
            @PathVariable Long idAlumno,
            @PathVariable Long idCurso) {
        return Map.of("exists", inscripcionService.existsByAlumnoYCurso(idAlumno, idCurso));
    }

    @GetMapping("/count")
    public Map<String, Long> countInscripciones() {
        return Map.of("total", inscripcionService.countInscripciones());
    }

    @GetMapping("/count/alumno/{idAlumno}")
    public Map<String, Long> countByAlumno(@PathVariable Long idAlumno) {
        return Map.of("total", inscripcionService.countByAlumno(idAlumno));
    }
}
