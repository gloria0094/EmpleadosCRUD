package com.upiiz.empleadoscrud.network;

import com.google.gson.JsonElement;
import com.upiiz.empleadoscrud.model.ApiResponse;
import com.upiiz.empleadoscrud.model.Empleado;
import com.upiiz.empleadoscrud.model.ListaEmpleados;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** Rutas tomadas del Swagger de la API. */
public interface EmpleadoApi {

    @GET("api/v1/clientes")
    Call<ApiResponse<JsonElement>> listar();

    @GET("api/v1/clientes/{id}")
    Call<ApiResponse<Empleado>> obtener(@Path("id") long id);

    @POST("api/v1/clientes")
    Call<ApiResponse<Empleado>> crear(@Body Empleado empleado);

    @PUT("api/v1/clientes/{id}")
    Call<ApiResponse<Empleado>> actualizar(@Path("id") long id, @Body Empleado empleado);

    @DELETE("api/v1/clientes/{id}")
    Call<ApiResponse<Object>> eliminar(@Path("id") long id);
}
