package org.example.pfcursos.servicios;

import org.example.pfcursos.dto.InscripcionRequest;
import org.example.pfcursos.dto.InscripcionResponse;
import org.example.pfcursos.modelo.Alumno;
import org.example.pfcursos.modelo.Curso;
import org.example.pfcursos.modelo.Inscripcion;
import org.example.pfcursos.repository.AlumnoRepository;
import org.example.pfcursos.repository.CursoRepository;
import org.example.pfcursos.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<InscripcionResponse> findAll() {
        return inscripcionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<InscripcionResponse> findAll(Pageable pageable) {
        return inscripcionRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public Optional<InscripcionResponse> findById(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Page<InscripcionResponse> findByAlumnoPaginado(Long idAlumno, Pageable pageable) {
        return inscripcionRepository.findByAlumnoPaginado(idAlumno, pageable)
                .map(this::convertToResponse);
    }

    public InscripcionResponse saveInscripcion(InscripcionRequest request) {
        // El alumno existe
        Alumno alumno = alumnoRepository.findById(request.getId_alumno())
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado con ID: " + request.getId_alumno()));

        // El curso existe
        Curso curso = cursoRepository.findById(request.getId_curso())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.getId_curso()));

        // Solo cursos activos pueden recibir inscripciones
        if (!"ACTIVO".equalsIgnoreCase(curso.getEstado())) {
            throw new IllegalArgumentException("No se pueden crear inscripciones en cursos inactivos");
        }

        // Un estudiante no puede inscribirse dos veces en el mismo curso
        if (inscripcionRepository.existsByAlumno_IdAlumnoAndCurso_IdCurso(request.getId_alumno(), request.getId_curso())) {
            throw new IllegalArgumentException("El alumno ya está inscrito en este curso");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setAlumno(alumno);
        inscripcion.setCurso(curso);
        inscripcion.setFechaInscripcion(LocalDateTime.now());

        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToResponse(savedInscripcion);
    }


    public InscripcionResponse updateInscripcion(Long id, InscripcionRequest request) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada con ID: " + id));

        Alumno alumno = alumnoRepository.findById(request.getId_alumno())
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado con ID: " + request.getId_alumno()));


        Curso curso = cursoRepository.findById(request.getId_curso())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.getId_curso()));

        // No existe otra inscripción con la misma combinación
        if (!inscripcion.getAlumno().getIdAlumno().equals(request.getId_alumno()) ||
                !inscripcion.getCurso().getIdCurso().equals(request.getId_curso())) {
            if (inscripcionRepository.existsByAlumno_IdAlumnoAndCurso_IdCurso(request.getId_alumno(), request.getId_curso())) {
                throw new IllegalArgumentException("Ya existe una inscripción con estos datos");
            }
        }

        inscripcion.setAlumno(alumno);
        inscripcion.setCurso(curso);

        Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcion);
        return convertToResponse(updatedInscripcion);
    }

    public void deleteById(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + id);
        }
        inscripcionRepository.deleteById(id);
    }

    // Inscripción de alumno en curso
    public boolean existsByAlumnoYCurso(Long idAlumno, Long idCurso) {
        return inscripcionRepository.existsByAlumno_IdAlumnoAndCurso_IdCurso(idAlumno, idCurso);
    }

    public long countInscripciones() {
        return inscripcionRepository.count();
    }

    // Contar inscripciones de un alumno
    public long countByAlumno(Long idAlumno) {
        return inscripcionRepository.findByAlumnoPaginado(idAlumno, Pageable.unpaged())
                .getTotalElements();
    }

    public Page<InscripcionResponse> findByCurso(Long idCurso, Pageable pageable) {
        // 1. Llamamos al repositorio (Esto ya devuelve un Page, no hace falta cast)
        Page<Inscripcion> inscripciones = inscripcionRepository.findByCurso_IdCurso(idCurso, pageable);

        // 2. Mapeamos a Response usando el .map del propio Page (así no se rompe)
        return inscripciones.map(this::convertToResponse);
    }

    private InscripcionResponse convertToResponse(Inscripcion inscripcion) {
        InscripcionResponse.CursoDto cursoDto = new InscripcionResponse.CursoDto(
                inscripcion.getCurso().getIdCurso(),
                inscripcion.getCurso().getTitulo()
        );

        InscripcionResponse.AlumnoDto alumnoDto = new InscripcionResponse.AlumnoDto(
                inscripcion.getAlumno().getIdAlumno(),
                inscripcion.getAlumno().getNombre()
        );

        return new InscripcionResponse(
                inscripcion.getId_inscripcion(),
                inscripcion.getFechaInscripcion(),
                cursoDto,
                alumnoDto
        );
    }
}