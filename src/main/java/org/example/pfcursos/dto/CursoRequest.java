package org.example.pfcursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class CursoRequest {
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de finalización es obligatoria")
    private LocalDateTime fechaFin;

    @NotBlank(message = "El estado es obligatorio (activo/inactivo)")
    private String estado;

    // IDs de los profesores asignados (Relación N:M)
    private List<Long> profesoresIds;
}
