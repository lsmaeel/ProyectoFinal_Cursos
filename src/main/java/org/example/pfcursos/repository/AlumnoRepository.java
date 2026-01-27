package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    // Para validar que no existan correos duplicados antes de registrar [cite: 109]
    boolean existsByCorreo(String correo);

    // Para procesos de login o búsqueda por credenciales
    Optional<Alumno> findByCorreo(String correo);
}
