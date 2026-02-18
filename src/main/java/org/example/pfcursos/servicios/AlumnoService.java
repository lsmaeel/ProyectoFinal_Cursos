package org.example.pfcursos.servicios;

import org.example.pfcursos.dto.AlumnoRequest;
import org.example.pfcursos.dto.AlumnoResponse;
import org.example.pfcursos.modelo.Alumno;
import org.example.pfcursos.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.pfcursos.dto.LoginRequest;
import org.example.pfcursos.dto.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<AlumnoResponse> findAll() {
        return alumnoRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<AlumnoResponse> findAll(Pageable pageable) {
        return alumnoRepository.findAll(pageable)
                .map(this::convertToResponse);
    }


    public Optional<AlumnoResponse> findById(Long id) {
        return alumnoRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Optional<AlumnoResponse> findByCorreo(String correo) {
        return alumnoRepository.findByCorreo(correo)
                .map(this::convertToResponse);
    }

    public AlumnoResponse saveAlumno(AlumnoRequest request) {
        if (alumnoRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // La contraseña debe tener al menos 8 caracteres, incluyendo letras y números
        if (!validarPassword(request.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluyendo letras y números");
        }

        Alumno alumno = new Alumno();
        alumno.setNombre(request.getNombre());
        alumno.setCorreo(request.getCorreo());
        alumno.setPassword(request.getPassword());

        Alumno savedAlumno = alumnoRepository.save(alumno);
        return convertToResponse(savedAlumno);
    }

    public AlumnoResponse updateAlumno(Long id, AlumnoRequest request) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado con ID: " + id));

        if (!alumno.getCorreo().equals(request.getCorreo()) &&
                alumnoRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (!validarPassword(request.getPassword())) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluyendo letras y números");
            }
            alumno.setPassword(request.getPassword());
        }

        alumno.setNombre(request.getNombre());
        alumno.setCorreo(request.getCorreo());

        Alumno updatedAlumno = alumnoRepository.save(alumno);
        return convertToResponse(updatedAlumno);
    }

    public void deleteById(Long id) {
        if (!alumnoRepository.existsById(id)) {
            throw new IllegalArgumentException("Alumno no encontrado con ID: " + id);
        }
        alumnoRepository.deleteById(id);
    }


    public boolean existsByCorreo(String correo) {
        return alumnoRepository.existsByCorreo(correo);
    }

    public long countAlumnos() {
        return alumnoRepository.count();
    }

    private boolean validarPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean tieneLetras = password.matches(".*[a-zA-Z].*");
        boolean tieneNumeros = password.matches(".*\\d.*");

        return tieneLetras && tieneNumeros;
    }


    private AlumnoResponse convertToResponse(Alumno alumno) {
        List<AlumnoResponse.InscripcionDto> inscripciones = alumno.getInscripciones() != null
                ? alumno.getInscripciones().stream()
                .map(i -> new AlumnoResponse.InscripcionDto(
                        i.getId_inscripcion(),
                        i.getFechaInscripcion()))
                .collect(Collectors.toList())
                : List.of();

        List<AlumnoResponse.RevisionDto> revisiones = alumno.getRevisiones() != null
                ? alumno.getRevisiones().stream()
                .map(r -> new AlumnoResponse.RevisionDto(
                        r.getId_revision(),
                        (int) r.getPuntuacion()))
                .collect(Collectors.toList())
                : List.of();

        return new AlumnoResponse(
                alumno.getIdAlumno(),
                alumno.getNombre(),
                alumno.getCorreo(),
                alumno.getPassword(),
                inscripciones,
                revisiones
        );
    }
}