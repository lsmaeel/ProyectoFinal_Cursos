package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findByEstadoIgnoreCase(String estado, Pageable pageable);

    Page<Curso> findByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String titulo, String descripcion, Pageable pageable);
}
