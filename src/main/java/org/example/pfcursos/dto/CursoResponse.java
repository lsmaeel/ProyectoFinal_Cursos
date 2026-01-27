package org.example.pfcursos.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link org.example.pfcursos.modelo.Curso}
 */
@Value
public class CursoResponse implements Serializable {
    Long id_curso;
    String titulo;
    String descripcion;
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    String estado;
    List<ProfesorDto> profesores;
    List<InscripcionDto> inscripciones;
    List<RevisionDto> revisiones;

    /**
     * DTO for {@link org.example.pfcursos.modelo.Profesor}
     */
    @Value
    public static class ProfesorDto implements Serializable {
        Long id_profesor;
        String nombre_profe;
    }

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