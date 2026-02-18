package org.example.pfcursos.servicios;

import org.example.pfcursos.dto.LoginRequest;
import org.example.pfcursos.dto.LoginResponse;
import org.example.pfcursos.modelo.Alumno;
import org.example.pfcursos.modelo.Profesor;
import org.example.pfcursos.repository.AlumnoRepository;
import org.example.pfcursos.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private ProfesorRepository profesorRepository;

    public LoginResponse loginUniversal(LoginRequest request) {
        // 1. Intentar buscar en Alumnos
        Optional<Alumno> alumno = alumnoRepository.findByCorreo(request.getCorreo());
        if (alumno.isPresent()) {
            if (alumno.get().getPassword().equals(request.getPassword())) {
                return new LoginResponse(alumno.get().getIdAlumno(), alumno.get().getNombre(),
                        alumno.get().getCorreo(), "ALUMNO", "Login exitoso");
            }
            throw new IllegalArgumentException("Contraseña de alumno incorrecta");
        }

        // 2. Si no es alumno, intentar buscar en Profesores
        Optional<Profesor> profesor = profesorRepository.findByCorreo(request.getCorreo());
        if (profesor.isPresent()) {
            if (profesor.get().getPassword().equals(request.getPassword())) {
                return new LoginResponse(profesor.get().getId_profesor(), profesor.get().getNombre_profe(),
                        profesor.get().getCorreo(), "PROFESOR", "Login exitoso");
            }
            throw new IllegalArgumentException("Contraseña de profesor incorrecta");
        }

        // 3. Si no está en ninguno
        throw new IllegalArgumentException("Usuario no encontrado en el sistema");
    }
}
