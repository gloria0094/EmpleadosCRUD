package com.upiiz.empleadoscrud.model;

import java.io.Serializable;

/** Representa ClienteDto / NuevoClienteDto de la API. Si id es null no se envía en el JSON (POST). */
public class Empleado implements Serializable {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private boolean registrado;

    public Empleado() { }

    public Empleado(Long id, String nombre, String correo, String telefono, String direccion, boolean registrado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.registrado = registrado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public boolean isRegistrado() { return registrado; }
    public void setRegistrado(boolean registrado) { this.registrado = registrado; }
}
