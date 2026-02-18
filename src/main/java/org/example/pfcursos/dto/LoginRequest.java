package org.example.pfcursos.dto;

public class LoginRequest {
    private String correo;
    private String password;

    // Constructores
    public LoginRequest() {}
    public LoginRequest(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    // Getters y Setters
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}