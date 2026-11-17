package com.upiiz.empleadoscrud.model;

/** Envoltura CustomResponse de la API: { estado, mensaje, datos }. */
public class ApiResponse<T> {
    private int estado;
    private String mensaje;
    private T datos;

    public int getEstado() { return estado; }
    public String getMensaje() { return mensaje; }
    public T getDatos() { return datos; }
}
