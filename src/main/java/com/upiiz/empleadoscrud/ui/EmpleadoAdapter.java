package com.upiiz.empleadoscrud.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upiiz.empleadoscrud.R;
import com.upiiz.empleadoscrud.model.Empleado;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoAdapter extends RecyclerView.Adapter<EmpleadoAdapter.ViewHolder> {

    public interface OnEmpleadoClick {
        void onClick(Empleado empleado);
    }

    private final List<Empleado> datos = new ArrayList<>();
    private final OnEmpleadoClick listener;

    public EmpleadoAdapter(OnEmpleadoClick listener) {
        this.listener = listener;
    }

    public void setDatos(List<Empleado> nuevos) {
        datos.clear();
        datos.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleado, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Empleado e = datos.get(position);
        h.tvId.setText("ID: " + e.getId());
        h.tvNombre.setText(e.getNombre());
        h.tvCorreo.setText(e.getCorreo());
        h.tvTelefono.setText(e.getTelefono());
        h.itemView.setOnClickListener(v -> listener.onClick(e));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvId, tvNombre, tvCorreo, tvTelefono;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCorreo = itemView.findViewById(R.id.tvCorreo);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
        }
    }
}
