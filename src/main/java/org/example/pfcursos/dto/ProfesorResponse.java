package org.example.pfcursos.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link org.example.pfcursos.modelo.Profesor}
 */
@Value

public class ProfesorResponse implements Serializable {
    Long id_profesor;
    String nombre_profe;
    String correo;
    String password;
    List<CursoDto> cursos;

    /**
     * DTO for {@link org.example.pfcursos.modelo.Curso}
     */
    @Value
    public static class CursoDto implements Serializable {
        Long id_curso;
        String titulo;
    }
}