package org.example.pfcursos.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link org.example.pfcursos.modelo.Alumno}
 */
@Value
public class AlumnoResponse implements Serializable {
    Long id_alumno;
    String nombre;
    String correo;
    String password;
    List<InscripcionDto> inscripciones;
    List<RevisionDto> revisiones;

    /**
     * DTO for {@link org.example.pfcursos.modelo.Inscripcion}
     */
    @Value
    public static class InscripcionDto implements Serializable {
        Long id_revision;
        LocalDateTime fechaInscripcion;
    }

    /**
     * DTO for {@link org.example.pfcursos.modelo.Revision}
     */
    @Value
    public static class RevisionDto implements Serializable {
        Long id_revision;
        Integer puntuacion;
    }
}