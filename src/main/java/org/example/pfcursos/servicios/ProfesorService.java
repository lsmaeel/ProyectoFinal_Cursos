package org.example.pfcursos.servicios;

import org.example.pfcursos.dto.ProfesorRequest;
import org.example.pfcursos.dto.ProfesorResponse;
import org.example.pfcursos.modelo.Profesor;
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
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    public List<ProfesorResponse> findAll() {
        return profesorRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<ProfesorResponse> findAll(Pageable pageable) {
        return profesorRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public Optional<ProfesorResponse> findById(Long id) {
        return profesorRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<ProfesorResponse> findProfesoresActivos() {
        return profesorRepository.findActiveProfesores().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProfesorResponse save(ProfesorRequest request) {
        // No se permiten correos duplicados
        if (profesorRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        //  contraseña debe tener al menos 8 caracteres
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        Profesor profesor = new Profesor();
        profesor.setNombreProfe(request.getNombre_profe());
        profesor.setCorreo(request.getCorreo());
        profesor.setPassword(request.getPassword());

        Profesor savedProfesor = profesorRepository.save(profesor);
        return convertToResponse(savedProfesor);
    }


    public ProfesorResponse updateProfesor(Long id, ProfesorRequest request) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado con ID: " + id));

        if (!profesor.getCorreo().equals(request.getCorreo()) &&
                profesorRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (request.getPassword().length() < 8) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
            }
            profesor.setPassword(request.getPassword());
        }

        profesor.setNombreProfe(request.getNombre_profe());
        profesor.setCorreo(request.getCorreo());

        Profesor updatedProfesor = profesorRepository.save(profesor);
        return convertToResponse(updatedProfesor);
    }

    public void deleteById(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new IllegalArgumentException("Profesor no encontrado con ID: " + id);
        }
        profesorRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return profesorRepository.existsById(id);
    }

    public boolean existsByCorreo(String correo) {
        return profesorRepository.existsByCorreo(correo);
    }

    public boolean isActivo(Long id) {
        return profesorRepository.findById(id)
                .map(profesor -> profesor.getCursos() != null && !profesor.getCursos().isEmpty())
                .orElse(false);
    }

    public long countProfesores() {
        return profesorRepository.count();
    }


    public long countProfesoresActivos() {
        return profesorRepository.findActiveProfesores().size();
    }

    private ProfesorResponse convertToResponse(Profesor profesor) {
        List<ProfesorResponse.CursoDto> cursos = profesor.getCursos() != null
                ? profesor.getCursos().stream()
                .map(c -> new ProfesorResponse.CursoDto(
                        c.getIdCurso(),
                        c.getTitulo()))
                .collect(Collectors.toList())
                : List.of();

        return new ProfesorResponse(
                profesor.getId_profesor(),
                profesor.getNombre_profe(),
                profesor.getCorreo(),
                profesor.getPassword(),
                cursos
        );
    }
}