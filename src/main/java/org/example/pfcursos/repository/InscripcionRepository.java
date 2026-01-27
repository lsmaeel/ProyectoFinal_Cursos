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

    // Comparamos el objeto completo, Spring se encarga de los IDs por debajo
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.alumno.id = :idAlumno AND i.curso.id = :idCurso")
    boolean existsByAlumnoYCurso(@Param("idAlumno") Long idAlumno, @Param("idCurso") Long idCurso);

    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :idAlumno")
    Page<Inscripcion> findByAlumnoPaginado(@Param("idAlumno") Long idAlumno, Pageable pageable);
}