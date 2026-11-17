package com.upiiz.empleadoscrud.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.upiiz.empleadoscrud.R;
import com.upiiz.empleadoscrud.model.ApiResponse;
import com.upiiz.empleadoscrud.model.Empleado;
import com.upiiz.empleadoscrud.model.ListaEmpleados;
import com.upiiz.empleadoscrud.network.ApiCallback;
import com.upiiz.empleadoscrud.network.RetrofitClient;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
/* Itzel Citlalli Flores Díaz
* Gloria Leticia González Troncoso
*  */
public class MainActivity extends AppCompatActivity {

    private EmpleadoAdapter adapter;
    private ProgressBar progress;
    private TextView tvVacio;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);
        progress = findViewById(R.id.progress);
        tvVacio = findViewById(R.id.tvVacio);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmpleadoAdapter(empleado -> {
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra(DetalleActivity.EXTRA_ID, empleado.getId());
            startActivity(i);
        });
        recycler.setAdapter(adapter);

        findViewById(R.id.fabAgregar).setOnClickListener(v ->
                startActivity(new Intent(this, FormularioActivity.class)));
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        progress.setVisibility(View.VISIBLE);
        RetrofitClient.getApi().listar().enqueue(new ApiCallback<JsonElement>() {
            @Override
            public void onExito(ApiResponse<JsonElement> respuesta) {
                progress.setVisibility(View.GONE);
                List<Empleado> lista = new ArrayList<>();
                if (respuesta != null && respuesta.getDatos() != null) {
                    lista = ListaEmpleados.desde(respuesta.getDatos());
                }
                adapter.setDatos(lista);
                tvVacio.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String mensaje) {
                progress.setVisibility(View.GONE);
                Snackbar.make(root, mensaje, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reintentar", v -> cargarEmpleados())
                        .show();
            }
        });
    }
}
