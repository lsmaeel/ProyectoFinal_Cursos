package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findByEstadoIgnoreCase(String estado, Pageable pageable);

    long countByEstadoIgnoreCase(String estado);

    long countByEstado(String estado);

    Page<Curso> findByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String titulo, String descripcion, Pageable pageable);

    @Query("SELECT i.curso FROM Inscripcion i GROUP BY i.curso ORDER BY COUNT(i) DESC")
    List<Curso> findTopCursoByInscripciones(Pageable pageable);
}
