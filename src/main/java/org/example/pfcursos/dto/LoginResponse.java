package org.example.pfcursos.dto;

public class LoginResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String rol; // "ALUMNO" o "PROFESOR"
    private String mensaje;

    public LoginResponse(Long id, String nombre, String correo, String rol, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    // Getters (los setters son opcionales si solo es para lectura)
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getMensaje() { return mensaje; }
    public String getRol() { return rol; }
}