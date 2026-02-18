package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    boolean existsByCorreo(String correo);

    Optional<Alumno> findByCorreo(String correo);

}
