package org.example.pfcursos.servicios;

import org.example.pfcursos.dto.CursoRequest;
import org.example.pfcursos.dto.CursoResponse;
import org.example.pfcursos.modelo.Curso;
import org.example.pfcursos.modelo.Profesor;
import org.example.pfcursos.repository.CursoRepository;
import org.example.pfcursos.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    public List<CursoResponse> findAll() {
        return cursoRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<CursoResponse> findAll(Pageable pageable) {
        return cursoRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public Optional<CursoResponse> findById(Long id) {
        return cursoRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Page<CursoResponse> findByEstado(String estado, Pageable pageable) {
        return cursoRepository.findByEstadoIgnoreCase(estado, pageable)
                .map(this::convertToResponse);
    }

    public Page<CursoResponse> buscarPorTexto(String texto, Pageable pageable) {
        return cursoRepository.findByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
                        texto, texto, pageable)
                .map(this::convertToResponse);
    }

    public CursoResponse save(CursoRequest request) {
        //La fecha de inicio debe ser anterior a la fecha de finalización
        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de finalización");
        }

        //Estado válido
        if (!request.getEstado().equalsIgnoreCase("ACTIVO") &&
                !request.getEstado().equalsIgnoreCase("INACTIVO")) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO o INACTIVO");
        }

        Curso curso = new Curso();
        curso.setTitulo(request.getTitulo());
        curso.setDescripcion(request.getDescripcion());
        curso.setFechaInicio(request.getFechaInicio());
        curso.setFechaFin(request.getFechaFin());
        curso.setEstado(request.getEstado().toUpperCase());

        // Asignar profesores si se proporciona su id
        if (request.getProfesoresIds() != null && !request.getProfesoresIds().isEmpty()) {
            List<Profesor> profesores = profesorRepository.findAllById(request.getProfesoresIds());
            curso.setProfesores(profesores);
        }

        Curso savedCurso = cursoRepository.save(curso);
        return convertToResponse(savedCurso);
    }

    public CursoResponse updateCurso(Long id, CursoRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + id));

        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de finalización");
        }

        if (!request.getEstado().equalsIgnoreCase("ACTIVO") &&
                !request.getEstado().equalsIgnoreCase("INACTIVO")) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO o INACTIVO");
        }

        curso.setTitulo(request.getTitulo());
        curso.setDescripcion(request.getDescripcion());
        curso.setFechaInicio(request.getFechaInicio());
        curso.setFechaFin(request.getFechaFin());
        curso.setEstado(request.getEstado().toUpperCase());

        // Actualizar profesores si se proporcionan IDs
        if (request.getProfesoresIds() != null) {
            List<Profesor> profesores = profesorRepository.findAllById(request.getProfesoresIds());
            curso.setProfesores(profesores);
        }

        Curso updatedCurso = cursoRepository.save(curso);
        return convertToResponse(updatedCurso);
    }

    public void deleteById(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return cursoRepository.existsById(id);
    }

    public boolean isActivo(Long id) {
        return cursoRepository.findById(id)
                .map(curso -> "ACTIVO".equalsIgnoreCase(curso.getEstado()))
                .orElse(false);
    }

    public CursoResponse cambiarEstado(Long id, String nuevoEstado) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + id));

        if (!nuevoEstado.equalsIgnoreCase("ACTIVO") &&
                !nuevoEstado.equalsIgnoreCase("INACTIVO")) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO o INACTIVO");
        }

        curso.setEstado(nuevoEstado.toUpperCase());
        Curso updatedCurso = cursoRepository.save(curso);
        return convertToResponse(updatedCurso);
    }

    public long countCursos() {
        return cursoRepository.count();
    }


    public long countByEstado(String estado) {
        return cursoRepository.findByEstadoIgnoreCase(estado, Pageable.unpaged()).getTotalElements();
    }

    private CursoResponse convertToResponse(Curso curso) {
        List<CursoResponse.ProfesorDto> profesores = curso.getProfesores() != null
                ? curso.getProfesores().stream()
                .map(p -> new CursoResponse.ProfesorDto(
                        p.getId_profesor(),
                        p.getNombre_profe()))
                .collect(Collectors.toList())
                : List.of();

        List<CursoResponse.InscripcionDto> inscripciones = curso.getInscripciones() != null
                ? curso.getInscripciones().stream()
                .map(i -> new CursoResponse.InscripcionDto(
                        i.getId_inscripcion(),
                        i.getFechaInscripcion()))
                .collect(Collectors.toList())
                : List.of();

        List<CursoResponse.RevisionDto> revisiones = curso.getRevisiones() != null
                ? curso.getRevisiones().stream()
                .map(r -> new CursoResponse.RevisionDto(
                        r.getId_revision(),
                        (int) r.getPuntuacion()))
                .collect(Collectors.toList())
                : List.of();

        return new CursoResponse(
                curso.getIdCurso(),
                curso.getTitulo(),
                curso.getDescripcion(),
                curso.getFechaInicio(),
                curso.getFechaFin(),
                curso.getEstado(),
                profesores,
                inscripciones,
                revisiones
        );
    }
}