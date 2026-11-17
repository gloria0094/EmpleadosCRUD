package com.upiiz.empleadoscrud.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://fw-ejercicio-08.onrender.com/";
    private static EmpleadoApi api;

    private RetrofitClient() { }

    public static synchronized EmpleadoApi getApi() {
        if (api == null) {
            // El log de las peticiones aparece en Logcat (filtra por "OkHttp"): sirve como evidencia.
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Render (plan gratuito) "duerme" el servicio: la primera petición puede tardar ~1 minuto.
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(90, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(90, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(EmpleadoApi.class);
        }
        return api;
    }
}
