package org.example.pfcursos.repository;

import org.example.pfcursos.modelo.Inscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.alumno.idAlumno = :idAlumno AND i.curso.idCurso = :idCurso")
    boolean existsByAlumnoYCurso(@Param("idAlumno") Long idAlumno, @Param("idCurso") Long idCurso);

    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.idAlumno = :idAlumno")
    Page<Inscripcion> findByAlumnoPaginado(@Param("idAlumno") Long idAlumno, Pageable pageable);
}