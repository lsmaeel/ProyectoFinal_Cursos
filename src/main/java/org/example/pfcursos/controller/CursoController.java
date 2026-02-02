package org.example.pfcursos.controller;

import jakarta.validation.Valid;
import org.example.pfcursos.dto.CursoRequest;
import org.example.pfcursos.dto.CursoResponse;
import org.example.pfcursos.servicios.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public List<CursoResponse> getAllCursos() {
        return cursoService.findAll();
    }

    @GetMapping("/paginacion")
    public Page<CursoResponse> getAllCursosPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return cursoService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public CursoResponse getCursoById(@PathVariable Long id) {
        return cursoService.findById(id).orElse(null);
    }

    @GetMapping("/estado/{estado}")
    public Page<CursoResponse> getCursosByEstado(
            @PathVariable String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return cursoService.findByEstado(estado, PageRequest.of(page, size));
    }

    @GetMapping("/activos")
    public Page<CursoResponse> getCursosActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return cursoService.findByEstado("ACTIVO", PageRequest.of(page, size));
    }

    @GetMapping("/demandado")
    public ResponseEntity<CursoResponse> getCursoMasDemandado() {
        CursoResponse curso = cursoService.getCursoMasDemandado();
        return ResponseEntity.ok(curso);
    }

    @GetMapping("/buscar")
    public Page<CursoResponse> buscarCursos(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return cursoService.buscarPorTexto(texto, PageRequest.of(page, size));
    }

    @PostMapping
    public CursoResponse createCurso(@Valid @RequestBody CursoRequest request) {
        return cursoService.saveCurso(request);
    }

    @PutMapping("/{id}")
    public CursoResponse updateCurso(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
        return cursoService.updateCurso(id, request);
    }

    @PatchMapping("/{id}/estado")
    public CursoResponse cambiarEstadoCurso(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return cursoService.cambiarEstado(id, body.get("estado"));
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteCurso(@PathVariable Long id) {
        cursoService.deleteById(id);
        return Map.of("message", "Curso eliminado exitosamente");
    }

    @GetMapping("/existe/{id}")
    public Map<String, Boolean> existsById(@PathVariable Long id) {
        return Map.of("exists", cursoService.existsById(id));
    }

    @GetMapping("/{id}/activo")
    public Map<String, Boolean> isActivo(@PathVariable Long id) {
        return Map.of("activo", cursoService.isActivo(id));
    }

    @GetMapping("/count")
    public Map<String, Long> countCursos() {
        return Map.of("total", cursoService.countCursos());
    }

    @GetMapping("/count/estado/{estado}")
    public Map<String, Long> countByEstado(@PathVariable String estado) {
        return Map.of("total", cursoService.countByEstado(estado));
    }
}
