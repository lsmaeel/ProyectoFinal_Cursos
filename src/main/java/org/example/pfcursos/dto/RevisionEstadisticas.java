package org.example.pfcursos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record RevisionEstadisticas(
        long totalRevisiones,
        double promedio,
        double puntuacionMaxima,
        double puntuacionMinima
) {}