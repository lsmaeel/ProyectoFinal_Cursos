package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    // Validación de correo único para profesores
    boolean existsByCorreo(String correo);

    // Un profesor es activo si tiene al menos un curso asignado
    @Query("SELECT p FROM Profesor p WHERE size(p.cursos) > 0")
    List<Profesor> findActiveProfesores();

    Optional<Profesor> findByCorreo(String correo);
}
