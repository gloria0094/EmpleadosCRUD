package com.upiiz.empleadoscrud.model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Extrae la lista de empleados de "datos" sin importar cómo la nombre la API (content, _embedded, etc.). */
public class ListaEmpleados {
    private static final Gson GSON = new Gson();
    private static final Type TIPO_LISTA = new TypeToken<List<Empleado>>() { }.getType();

    public static List<Empleado> desde(JsonElement datos) {
        List<Empleado> resultado = new ArrayList<>();
        buscar(datos, resultado);
        return resultado;
    }

    private static void buscar(JsonElement el, List<Empleado> salida) {
        if (el == null || el.isJsonNull()) return;
        if (el.isJsonArray()) {
            List<Empleado> lista = GSON.fromJson(el, TIPO_LISTA);
            if (lista != null) salida.addAll(lista);
        } else if (el.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entrada : el.getAsJsonObject().entrySet()) {
                String clave = entrada.getKey();
                if (clave.equals("links") || clave.equals("_links")) continue;
                buscar(entrada.getValue(), salida);
            }
        }
    }
}