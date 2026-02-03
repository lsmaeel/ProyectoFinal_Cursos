
# PFCursos

API REST de gestión académica desarrollada como Proyecto Final de Carrera. El sistema permite crear cursos impartidos por profesores, inscribir alumnos en los mismos y recopilar revisiones con puntuaciones y comentarios, incluyendo cálculo de estadísticas por curso.

---

## Tecnologías

| Tecnología | Versión / Detalle |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.6 |
| Spring Data JPA | Hibernate 6 |
| Lombok | Generación de boilerplate |
| Jakarta Validation | Validación en DTOs |
| MariaDB | Base de datos |
| Docker | Contenedor de la BD |

---

## Estructura del proyecto

```
src/main/java/org/example/pfcursos/
│
├── modelo/
│   ├── Alumno.java
│   ├── Curso.java
│   ├── Inscripcion.java
│   ├── Profesor.java
│   └── Revision.java
│
├── dto/
│   ├── AlumnoRequest.java          ├── AlumnoResponse.java
│   ├── CursoRequest.java           ├── CursoResponse.java
│   ├── InscripcionRequest.java     ├── InscripcionResponse.java
│   ├── ProfesorRequest.java        ├── ProfesorResponse.java
│   ├── RevisionRequest.java        └── RevisionResponse.java
│
├── repository/
│   ├── AlumnoRepository.java
│   ├── CursoRepository.java
│   ├── InscripcionRepository.java
│   ├── ProfesorRepository.java
│   └── RevisionRepository.java
│
├── servicios/
│   ├── AlumnoService.java
│   ├── CursoService.java
│   ├── InscripcionService.java
│   ├── ProfesorService.java
│   └── RevisionService.java
│
├── controller/
│   ├── AlumnoController.java
│   ├── CursoController.java
│   ├── InscripcionController.java
│   ├── ProfesorController.java
│   └── RevisionController.java
│
└── PfcursosApplication.java

src/main/resources/
└── application.properties
```

---

## Modelo de datos

```
Profesor ──── N:M ──── Curso              (tabla intermedia: profesor_curso)
                         │
               ┌─────────┴─────────┐
             1:N                  1:N
               │                   │
         Inscripcion           Revision
               │                   │
             N:1                 N:1
               │                   │
            Alumno ────────────────┘
```

| Tabla | Columnas |
|---|---|
| `alumnos` | `id_alumno` · `nombre` · `correo` (único) · `password` |
| `profesores` | `id_profesor` · `nombre_profesor` · `correo` (único) · `password` |
| `cursos` | `id_curso` · `titulo` · `descripcion` · `fechaInicio` · `fechaFin` · `estado` |
| `inscripciones` | `id_inscripcion` · `fechaInscripcion` · `alumno_id` (FK) · `curso_id` (FK) |
| `revisiones` | `id_revision` · `puntuacion` · `comentario` · `alumno_id` (FK) · `curso_id` (FK) |
| `profesor_curso` | `curso_id` (FK) · `profesor_id` (FK) |

---

## Reglas de negocio

1. Un alumno no puede inscribirse dos veces en el mismo curso.
2. Solo los cursos con estado `ACTIVO` aceptan nuevas inscripciones.
3. Solo un alumno inscrito puede dejar una revisión sobre ese curso.
4. La puntuación de una revisión debe estar entre 1 y 10.
5. Los correos de alumnos y profesores deben ser únicos.
6. La contraseña de un alumno debe tener al menos 8 caracteres con letras y números.
7. La fecha de inicio de un curso debe ser anterior a su fecha de fin.
8. Un profesor se considera activo si tiene al menos un curso asignado.

---

## Endpoints

### Alumnos `/api/alumnos`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/alumnos` | Lista todos los alumnos |
| GET | `/api/alumnos/paginacion` | Lista paginada (`page`, `size`) |
| GET | `/api/alumnos/{id}` | Alumno por ID |
| GET | `/api/alumnos/correo/{correo}` | Alumno por correo |
| POST | `/api/alumnos` | Crear alumno |
| PUT | `/api/alumnos/{id}` | Actualizar alumno |
| DELETE | `/api/alumnos/{id}` | Eliminar alumno |
| GET | `/api/alumnos/existe/correo/{correo}` | Verificar si el correo existe |
| GET | `/api/alumnos/count` | Total de alumnos |

### Profesores `/api/profesores`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/profesores` | Lista todos los profesores |
| GET | `/api/profesores/paginacion` | Lista paginada (`page`, `size`) |
| GET | `/api/profesores/{id}` | Profesor por ID |
| GET | `/api/profesores/activos` | Solo profesores con cursos asignados |
| POST | `/api/profesores` | Crear profesor |
| PUT | `/api/profesores/{id}` | Actualizar profesor |
| DELETE | `/api/profesores/{id}` | Eliminar profesor |
| GET | `/api/profesores/existe/correo/{correo}` | Verificar si el correo existe |
| GET | `/api/profesores/{id}/activo` | Verificar si el profesor está activo |
| GET | `/api/profesores/count` | Total de profesores |
| GET | `/api/profesores/count/activos` | Total de profesores activos |

### Cursos `/api/cursos`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/cursos` | Lista todos los cursos |
| GET | `/api/cursos/paginacion` | Lista paginada (`page`, `size`) |
| GET | `/api/cursos/{id}` | Curso por ID |
| GET | `/api/cursos/estado/{estado}` | Cursos filtrados por estado |
| GET | `/api/cursos/activos` | Solo cursos activos |
| GET | `/api/cursos/demandado` | Curso con más inscripciones |
| GET | `/api/cursos/buscar` | Buscar por texto en título o descripción (`texto`) |
| POST | `/api/cursos` | Crear curso |
| PUT | `/api/cursos/{id}` | Actualizar curso |
| PATCH | `/api/cursos/{id}/estado` | Cambiar solo el estado |
| DELETE | `/api/cursos/{id}` | Eliminar curso |
| GET | `/api/cursos/existe/{id}` | Verificar si el curso existe |
| GET | `/api/cursos/{id}/activo` | Verificar si el curso está activo |
| GET | `/api/cursos/count` | Total de cursos |
| GET | `/api/cursos/count/estado/{estado}` | Total de cursos por estado |

### Inscripciones `/api/inscripciones`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/inscripciones` | Lista todas las inscripciones |
| GET | `/api/inscripciones/paginacion` | Lista paginada (`page`, `size`) |
| GET | `/api/inscripciones/{id}` | Inscripción por ID |
| GET | `/api/inscripciones/alumno/{idAlumno}` | Inscripciones de un alumno (paginado) |
| GET | `/api/inscripciones/curso/{idCurso}` | Inscripciones de un curso (paginado) |
| POST | `/api/inscripciones` | Crear inscripción |
| PUT | `/api/inscripciones/{id}` | Actualizar inscripción |
| DELETE | `/api/inscripciones/{id}` | Eliminar inscripción |
| GET | `/api/inscripciones/existe/alumno/{idAlumno}/curso/{idCurso}` | Verificar si existe la inscripción |
| GET | `/api/inscripciones/count` | Total de inscripciones |
| GET | `/api/inscripciones/count/alumno/{idAlumno}` | Total de inscripciones de un alumno |

### Revisiones `/api/revisiones`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/revisiones` | Lista todas las revisiones |
| GET | `/api/revisiones/paginacion` | Lista paginada (`page`, `size`) |
| GET | `/api/revisiones/{id}` | Revisión por ID |
| GET | `/api/revisiones/curso/{idCurso}` | Revisiones de un curso |
| POST | `/api/revisiones` | Crear revisión |
| PUT | `/api/revisiones/{id}` | Actualizar revisión |
| DELETE | `/api/revisiones/{id}` | Eliminar revisión |
| GET | `/api/revisiones/count` | Total de revisiones |
| GET | `/api/revisiones/count/curso/{idCurso}` | Total de revisiones de un curso |
| GET | `/api/revisiones/curso/{idCurso}/promedio` | Promedio de puntuaciones del curso |
| GET | `/api/revisiones/curso/{idCurso}/estadisticas` | Estadísticas completas (total, promedio, máx, mín) |

---

## Configuración y ejecución

### MariaDB con Docker

```yaml
# docker-compose.yml
services:
  mariadb:
    image: mariadb:latest
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: pfcursos_db
      MYSQL_USER: pfcursos_user
      MYSQL_PASSWORD: pfcursos_pass
    ports:
      - "3306:3306"
```

```bash
docker-compose up -d
```

### application.properties

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/pfcursos_db
spring.datasource.username=pfcursos_user
spring.datasource.password=pfcursos_pass
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

### Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.
