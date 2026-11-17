package com.upiiz.empleadoscrud.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upiiz.empleadoscrud.R;
import com.upiiz.empleadoscrud.model.ApiResponse;
import com.upiiz.empleadoscrud.model.Empleado;
import com.upiiz.empleadoscrud.network.ApiCallback;
import com.upiiz.empleadoscrud.network.RetrofitClient;

import java.util.regex.Pattern;

/** Formulario para registrar (sin extra) o actualizar (con EXTRA_EMPLEADO) un empleado. */
public class FormularioActivity extends AppCompatActivity {

    public static final String EXTRA_EMPLEADO = "extra_empleado";

    // Solo dígitos, espacios, +, -, paréntesis; entre 7 y 20 caracteres.
    private static final Pattern TELEFONO = Pattern.compile("^\\+?[0-9()\\- ]{7,20}$");

    private Empleado editando;
    private View root;
    private ProgressBar progress;
    private MaterialButton btnGuardar;
    private TextInputLayout tilNombre, tilCorreo, tilTelefono, tilDireccion;
    private TextInputEditText etNombre, etCorreo, etTelefono, etDireccion;
    private SwitchMaterial swRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        root = findViewById(R.id.root);
        progress = findViewById(R.id.progress);
        btnGuardar = findViewById(R.id.btnGuardar);
        tilNombre = findViewById(R.id.tilNombre);
        tilCorreo = findViewById(R.id.tilCorreo);
        tilTelefono = findViewById(R.id.tilTelefono);
        tilDireccion = findViewById(R.id.tilDireccion);
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        swRegistrado = findViewById(R.id.swRegistrado);

        TextView titulo = findViewById(R.id.tvTituloForm);
        editando = (Empleado) getIntent().getSerializableExtra(EXTRA_EMPLEADO);

        if (editando != null) {
            titulo.setText("Editar empleado (ID " + editando.getId() + ")");
            etNombre.setText(editando.getNombre());
            etCorreo.setText(editando.getCorreo());
            etTelefono.setText(editando.getTelefono());
            etDireccion.setText(editando.getDireccion());
            swRegistrado.setChecked(editando.isRegistrado());
        } else {
            titulo.setText("Nuevo empleado");
        }

        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardar());
    }

    private String texto(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private boolean validar() {
        boolean ok = true;
        tilNombre.setError(null);
        tilCorreo.setError(null);
        tilTelefono.setError(null);
        tilDireccion.setError(null);

        String nombre = texto(etNombre);
        String correo = texto(etCorreo);
        String telefono = texto(etTelefono);
        String direccion = texto(etDireccion);

        if (nombre.isEmpty()) {
            tilNombre.setError("El nombre es obligatorio");
            ok = false;
        }
        if (correo.isEmpty()) {
            tilCorreo.setError("El correo es obligatorio");
            ok = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico no válido");
            ok = false;
        }
        if (telefono.isEmpty()) {
            tilTelefono.setError("El teléfono es obligatorio");
            ok = false;
        } else if (!TELEFONO.matcher(telefono).matches()) {
            tilTelefono.setError("Solo números, espacios, + - ( ) (7 a 20 caracteres)");
            ok = false;
        }
        if (direccion.isEmpty()) {
            tilDireccion.setError("La dirección es obligatoria");
            ok = false;
        }
        return ok;
    }

    private void guardar() {
        if (!validar()) return;

        Empleado e = new Empleado();
        e.setNombre(texto(etNombre));
        e.setCorreo(texto(etCorreo));
        e.setTelefono(texto(etTelefono));
        e.setDireccion(texto(etDireccion));
        e.setRegistrado(swRegistrado.isChecked());

        setCargando(true);

        if (editando == null) {
            RetrofitClient.getApi().crear(e).enqueue(callback("Empleado registrado correctamente."));
        } else {
            e.setId(editando.getId());
            RetrofitClient.getApi().actualizar(editando.getId(), e)
                    .enqueue(callback("Empleado actualizado correctamente."));
        }
    }

    private ApiCallback<Empleado> callback(String mensajeExito) {
        return new ApiCallback<Empleado>() {
            @Override
            public void onExito(ApiResponse<Empleado> respuesta) {
                setCargando(false);
                Toast.makeText(FormularioActivity.this, mensajeExito, Toast.LENGTH_LONG).show();
                finish(); // las pantallas anteriores recargan sus datos en onResume
            }

            @Override
            public void onError(String mensaje) {
                setCargando(false);
                Snackbar.make(root, "No se pudo guardar: " + mensaje, Snackbar.LENGTH_LONG).show();
            }
        };
    }

    private void setCargando(boolean cargando) {
        progress.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnGuardar.setEnabled(!cargando);
    }
}
