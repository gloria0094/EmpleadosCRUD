package com.upiiz.empleadoscrud.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.upiiz.empleadoscrud.R;
import com.upiiz.empleadoscrud.model.ApiResponse;
import com.upiiz.empleadoscrud.model.Empleado;
import com.upiiz.empleadoscrud.network.ApiCallback;
import com.upiiz.empleadoscrud.network.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";

    private long id;
    private Empleado actual;
    private View root;
    private ProgressBar progress;
    private TextView tvId, tvNombre, tvCorreo, tvTelefono, tvDireccion, tvRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        id = getIntent().getLongExtra(EXTRA_ID, -1);
        root = findViewById(R.id.root);
        progress = findViewById(R.id.progress);
        tvId = findViewById(R.id.tvId);
        tvNombre = findViewById(R.id.tvNombre);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvRegistrado = findViewById(R.id.tvRegistrado);

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());

        findViewById(R.id.btnEditar).setOnClickListener(v -> {
            if (actual == null) return;
            Intent i = new Intent(this, FormularioActivity.class);
            i.putExtra(FormularioActivity.EXTRA_EMPLEADO, actual);
            startActivity(i);
        });

        findViewById(R.id.btnEliminar).setOnClickListener(v -> {
            if (actual == null) return;
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Eliminar empleado")
                    .setMessage("¿Seguro que deseas eliminar a " + actual.getNombre() + "? Esta acción no se puede deshacer.")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (d, w) -> eliminar())
                    .show();
        });
    }

    /** Se recarga al volver del formulario para mostrar los datos ya actualizados. */
    @Override
    protected void onResume() {
        super.onResume();
        cargar();
    }

    private void cargar() {
        progress.setVisibility(View.VISIBLE);
        RetrofitClient.getApi().obtener(id).enqueue(new ApiCallback<Empleado>() {
            @Override
            public void onExito(ApiResponse<Empleado> respuesta) {
                progress.setVisibility(View.GONE);
                if (respuesta == null || respuesta.getDatos() == null) {
                    Snackbar.make(root, "El servidor devolvió una respuesta vacía.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                actual = respuesta.getDatos();
                mostrar(actual);
            }

            @Override
            public void onError(String mensaje) {
                progress.setVisibility(View.GONE);
                Snackbar.make(root, mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void mostrar(Empleado e) {
        tvId.setText("ID: " + e.getId());
        tvNombre.setText("Nombre: " + e.getNombre());
        tvCorreo.setText("Correo: " + e.getCorreo());
        tvTelefono.setText("Teléfono: " + e.getTelefono());
        tvDireccion.setText("Dirección: " + e.getDireccion());
        tvRegistrado.setText("Registrado: " + (e.isRegistrado() ? "Sí" : "No"));
    }

    private void eliminar() {
        progress.setVisibility(View.VISIBLE);
        RetrofitClient.getApi().eliminar(id).enqueue(new ApiCallback<Object>() {
            @Override
            public void onExito(ApiResponse<Object> respuesta) {
                progress.setVisibility(View.GONE);
                String msg = (respuesta != null && respuesta.getMensaje() != null)
                        ? respuesta.getMensaje() : "Empleado eliminado correctamente.";
                Toast.makeText(DetalleActivity.this, msg, Toast.LENGTH_LONG).show();
                finish(); // MainActivity recarga el listado en onResume
            }

            @Override
            public void onError(String mensaje) {
                progress.setVisibility(View.GONE);
                Snackbar.make(root, "No se pudo eliminar: " + mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
