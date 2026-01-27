package org.example.pfcursos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profesores")
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor", nullable = false, unique = true)
    private Long id_profesor;

    @Column(name = "nombre_profesor", nullable = false)
    private String nombre_profe;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(mappedBy = "profesores", fetch = FetchType.LAZY)
    private List<Curso> cursos;
}
