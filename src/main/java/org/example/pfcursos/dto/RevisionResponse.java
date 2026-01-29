package org.example.pfcursos.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.example.pfcursos.modelo.Revision}
 */
@Value

public class RevisionResponse implements Serializable {
    Long id_revision;
    double puntuacion;
    String comentario;
    CursoDto curso;
    AlumnoDto alumno;

    /**
     * DTO for {@link org.example.pfcursos.modelo.Curso}
     */
    @Value
    public static class CursoDto implements Serializable {
        Long id_curso;
        String titulo;
    }

    /**
     * DTO for {@link org.example.pfcursos.modelo.Alumno}
     */
    @Value
    public static class AlumnoDto implements Serializable {
        Long id_alumno;
        String nombre;
    }
}