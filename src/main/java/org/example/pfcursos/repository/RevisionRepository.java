package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // REVISA QUE SEA ESTE
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {


    // Si usas idCurso (CamelCase) en tu entidad:
    Page<Revision> findByCurso_IdCurso(Long idCurso, Pageable pageable);
}