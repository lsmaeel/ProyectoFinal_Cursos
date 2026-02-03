package org.example.pfcursos.servicios;

import org.example.pfcursos.dto.RevisionEstadisticas;
import org.example.pfcursos.dto.RevisionRequest;
import org.example.pfcursos.dto.RevisionResponse;
import org.example.pfcursos.modelo.Alumno;
import org.example.pfcursos.modelo.Curso;
import org.example.pfcursos.modelo.Revision;
import org.example.pfcursos.repository.AlumnoRepository;
import org.example.pfcursos.repository.CursoRepository;
import org.example.pfcursos.repository.InscripcionRepository;
import org.example.pfcursos.repository.RevisionRepository;
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
public class RevisionService {

    @Autowired
    private RevisionRepository revisionRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    public List<RevisionResponse> findAll() {
        return revisionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<RevisionResponse> findAll(Pageable pageable) {
        return revisionRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public Optional<RevisionResponse> findById(Long id) {
        return revisionRepository.findById(id)
                .map(this::convertToResponse);
    }

    // Obtener revisiones de un curso
    public Page<RevisionResponse> findByCurso(Long idCurso, Pageable pageable) {
        return revisionRepository.findByCurso_IdCurso(idCurso, pageable)
                .map(this::convertToResponse);
    }

    public RevisionResponse saveRevision(RevisionRequest request) {
        // El alumno existe
        Alumno alumno = alumnoRepository.findById(request.getId_alumno())
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado con ID: " + request.getId_alumno()));

        // El curso existe
        Curso curso = cursoRepository.findById(request.getId_curso())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.getId_curso()));

        // Solo estudiantes inscritos pueden dejar una revisión
        if (!inscripcionRepository.existsByAlumno_IdAlumnoAndCurso_IdCurso(request.getId_alumno(), request.getId_curso())) {
            throw new IllegalArgumentException("El alumno debe estar inscrito en el curso para dejar una revisión");
        }

        // La puntuación debe estar en un rango de 1 a 10
        if (request.getPuntuacion() < 1 || request.getPuntuacion() > 10) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 10");
        }

        Revision revision = new Revision();
        revision.setAlumno(alumno);
        revision.setCurso(curso);
        revision.setPuntuacion(request.getPuntuacion());
        revision.setComentario(request.getComentario());

        Revision savedRevision = revisionRepository.save(revision);
        return convertToResponse(savedRevision);
    }

    public RevisionResponse updateRevision(Long id, RevisionRequest request) {
        Revision revision = revisionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revisión no encontrada con ID: " + id));

        Alumno alumno = alumnoRepository.findById(request.getId_alumno())
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado con ID: " + request.getId_alumno()));

        Curso curso = cursoRepository.findById(request.getId_curso())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + request.getId_curso()));

        if (!inscripcionRepository.existsByAlumno_IdAlumnoAndCurso_IdCurso(request.getId_alumno(), request.getId_curso())) {
            throw new IllegalArgumentException("El alumno debe estar inscrito en el curso para dejar una revisión");
        }

        if (request.getPuntuacion() < 1 || request.getPuntuacion() > 10) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 10");
        }

        revision.setAlumno(alumno);
        revision.setCurso(curso);
        revision.setPuntuacion(request.getPuntuacion());
        revision.setComentario(request.getComentario());

        Revision updatedRevision = revisionRepository.save(revision);
        return convertToResponse(updatedRevision);
    }

    public void deleteById(Long id) {
        if (!revisionRepository.existsById(id)) {
            throw new IllegalArgumentException("Revisión no encontrada con ID: " + id);
        }
        revisionRepository.deleteById(id);
    }

    public long countRevisiones() {
        return revisionRepository.count();
    }

    // Contar revisiones de un curso
    public long countByCurso(Long idCurso) {
        return revisionRepository.findByCurso_IdCurso(idCurso, Pageable.unpaged())
                .getTotalElements();
    }

    // Calcula el promedio de puntuaciones de las revisiones de un curso
    public Double calcularPromedioPuntuacion(Long idCurso) {

        if (!cursoRepository.existsById(idCurso)) {
            throw new IllegalArgumentException("Curso no encontrado con ID: " + idCurso);
        }

        Page<Revision> revisiones = revisionRepository.findByCurso_IdCurso(idCurso, Pageable.unpaged());

        if (revisiones.isEmpty()) {
            return 0.0;
        }

        // Suma todas las puntuaciones de las revisiones
        double suma = revisiones.stream()
                .mapToDouble(Revision::getPuntuacion) // Extrae la puntuación de cada revisión
                .sum(); // Suma todas las puntuaciones

        return suma / revisiones.getTotalElements();
    }

    public RevisionEstadisticas getEstadisticasCurso(Long idCurso) {

        if (!cursoRepository.existsById(idCurso)) {
            throw new IllegalArgumentException("Curso no encontrado con ID: " + idCurso);
        }

        Page<Revision> revisiones = revisionRepository.findByCurso_IdCurso(idCurso, Pageable.unpaged());

        if (revisiones.isEmpty()) {
            return new RevisionEstadisticas(
                    0,
                    0.0,
                    0.0,
                    0.0
            );
        }

        long totalRevisiones = revisiones.getTotalElements();

        double promedio = calcularPromedioPuntuacion(idCurso);

        double puntuacionMaxima = revisiones.stream()
                .mapToDouble(Revision::getPuntuacion)
                .max()
                .orElse(0.0);

        double puntuacionMinima = revisiones.stream()
                .mapToDouble(Revision::getPuntuacion)
                .min()
                .orElse(0.0);

        return new RevisionEstadisticas(
                totalRevisiones,
                promedio,
                puntuacionMaxima,
                puntuacionMinima
        );
    }


    private RevisionResponse convertToResponse(Revision revision) {
        RevisionResponse.CursoDto cursoDto = new RevisionResponse.CursoDto(
                revision.getCurso().getIdCurso(),
                revision.getCurso().getTitulo()
        );

        RevisionResponse.AlumnoDto alumnoDto = new RevisionResponse.AlumnoDto(
                revision.getAlumno().getIdAlumno(),
                revision.getAlumno().getNombre()
        );

        return new RevisionResponse(
                revision.getId_revision(),
                revision.getPuntuacion(),
                revision.getComentario(),
                cursoDto,
                alumnoDto
        );
    }

}