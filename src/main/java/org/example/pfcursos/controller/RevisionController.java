package org.example.pfcursos.controller;


import jakarta.validation.Valid;
import org.example.pfcursos.dto.RevisionEstadisticas;
import org.example.pfcursos.dto.RevisionRequest;
import org.example.pfcursos.dto.RevisionResponse;
import org.example.pfcursos.servicios.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/revisiones")
@CrossOrigin(origins = "*")
public class RevisionController {

    @Autowired
    private RevisionService revisionService;

    @GetMapping
    public List<RevisionResponse> getAllRevisiones() {
        return revisionService.findAll();
    }

    @GetMapping("/paginacion")
    public List<RevisionResponse> getAllRevisionesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return revisionService.findAll(PageRequest.of(page, size)).getContent();
    }

    @GetMapping("/{id}")
    public RevisionResponse getRevisionById(@PathVariable Long id) {
        return revisionService.findById(id).orElse(null);
    }

    @GetMapping("/curso/{idCurso}")
    public List<RevisionResponse> getRevisionesByCurso(
            @PathVariable Long idCurso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return revisionService.findByCurso(idCurso, PageRequest.of(page, size)).getContent();
    }

    @PostMapping
    public RevisionResponse createRevision(@Valid @RequestBody RevisionRequest request) {
        return revisionService.saveRevision(request);
    }

    @PutMapping("/{id}")
    public RevisionResponse updateRevision(@PathVariable Long id, @Valid @RequestBody RevisionRequest request) {
        return revisionService.updateRevision(id, request);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteRevision(@PathVariable Long id) {
        revisionService.deleteById(id);
        return Map.of("message", "Revisión eliminada exitosamente");
    }


    @GetMapping("/count")
    public Map<String, Long> countRevisiones() {
        return Map.of("total", revisionService.countRevisiones());
    }

    @GetMapping("/count/curso/{idCurso}")
    public Map<String, Long> countByCurso(@PathVariable Long idCurso) {
        return Map.of("total", revisionService.countByCurso(idCurso));
    }

    @GetMapping("/curso/{idCurso}/promedio")
    public Map<String, Object> calcularPromedioCurso(@PathVariable Long idCurso) {
        Double promedio = revisionService.calcularPromedioPuntuacion(idCurso);
        return Map.of(
                "idCurso", idCurso,
                "promedio", promedio
        );
    }

    @GetMapping("/curso/{idCurso}/estadisticas")
    public Map<String, Object> getEstadisticasCurso(@PathVariable Long idCurso) {
        RevisionEstadisticas estadisticas = revisionService.getEstadisticasCurso(idCurso);
        return Map.of(
                "idCurso", idCurso,
                "totalRevisiones", estadisticas.totalRevisiones(),
                "promedio", estadisticas.promedio(),
                "puntuacionMaxima", estadisticas.puntuacionMaxima(),
                "puntuacionMinima", estadisticas.puntuacionMinima()
        );
    }
}
