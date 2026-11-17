package com.upiiz.empleadoscrud.network;

import com.upiiz.empleadoscrud.model.ApiResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Callback asíncrono (se ejecuta fuera del hilo principal y responde en él) que unifica el manejo de:
 * errores HTTP, problemas de conexión y respuestas vacías.
 * Nota: en onExito la respuesta o sus datos pueden ser null (p. ej. DELETE sin cuerpo).
 */
public abstract class ApiCallback<T> implements Callback<ApiResponse<T>> {

    public abstract void onExito(ApiResponse<T> respuesta);

    public abstract void onError(String mensaje);

    @Override
    public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
        if (response.isSuccessful()) {
            ApiResponse<T> body = response.body();
            if (body != null && body.getEstado() >= 400) {
                onError(body.getMensaje() != null ? body.getMensaje()
                        : "El servidor devolvió un error (" + body.getEstado() + ").");
                return;
            }
            onExito(body);
        } else {
            onError(mensajeDeError(response));
        }
    }

    @Override
    public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
        if (call.isCanceled()) return;
        if (t instanceof IOException) {
            onError("No se pudo conectar con el servidor. Revisa tu conexión a Internet e inténtalo de nuevo.");
        } else {
            onError("Respuesta inesperada del servidor: " + t.getMessage());
        }
    }

    private String mensajeDeError(Response<?> response) {
        String detalle = null;
        try {
            if (response.errorBody() != null) {
                String raw = response.errorBody().string();
                ApiResponse<?> err = new Gson().fromJson(raw, ApiResponse.class);
                if (err != null) detalle = err.getMensaje();
            }
        } catch (Exception ignorada) {
            // El cuerpo de error no era JSON; se usa un mensaje genérico.
        }
        if (detalle != null && !detalle.trim().isEmpty()) return detalle;

        switch (response.code()) {
            case 400: return "Los datos enviados no son válidos (400).";
            case 404: return "El empleado no existe o ya fue eliminado (404).";
            case 409: return "Conflicto con los datos existentes (409).";
            case 500: return "Error interno del servidor (500).";
            case 503: return "El servidor no está disponible en este momento (503).";
            default:  return "Error al comunicarse con el servidor (código " + response.code() + ").";
        }
    }
}
