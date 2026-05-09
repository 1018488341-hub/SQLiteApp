package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtId, txtNombre, txtCorreo, txtTelefono;
    Button btnGuardar, btnMostrar, btnActualizar, btnBorrar, btnIrProductos;
    TextView txtResultado;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtId = findViewById(R.id.txtId);
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnIrProductos = findViewById(R.id.btnIrProductos);
        txtResultado = findViewById(R.id.txtResultado);

        dbHelper = new DatabaseHelper(this);

        btnIrProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductosActivity.class);
                startActivity(intent);
            }
        });

        // BOTÓN MOSTRAR (Consultar)
        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM usuarios", null);

                StringBuilder stringBuilder = new StringBuilder();
                while(cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String nombre = cursor.getString(1);
                    String correo = cursor.getString(2);
                    String telefono = cursor.getString(3);
                    stringBuilder.append("ID: ").append(id)
                            .append(" - ").append(nombre)
                            .append(" (").append(correo).append(") - ")
                            .append(telefono).append("\n");
                }
                cursor.close();
                txtResultado.setText(stringBuilder.toString());
            }
        });

        // BOTÓN ACTUALIZAR
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = txtId.getText().toString().trim();
                String nombre = txtNombre.getText().toString().trim();
                String correo = txtCorreo.getText().toString().trim();
                String telefono = txtTelefono.getText().toString().trim();

                if (id.isEmpty()) {
                    txtId.setError("Ingresa el ID");
                    txtId.requestFocus();
                    return;
                }

                if (nombre.isEmpty()) {
                    txtNombre.setError("Nombre requerido");
                    txtNombre.requestFocus();
                    return;
                }

                if (correo.isEmpty()) {
                    txtCorreo.setError("Correo requerido");
                    txtCorreo.requestFocus();
                    return;
                }

                if (telefono.isEmpty()) {
                    txtTelefono.setError("Teléfono requerido");
                    txtTelefono.requestFocus();
                    return;
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nombre", nombre);
                values.put("correo", correo);
                values.put("telefono", telefono);

                int filasAfectadas = db.update("usuarios", values, "id=?", new String[]{id});

                if (filasAfectadas > 0) {
                    Toast.makeText(MainActivity.this, "Usuario actualizado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "No se encontró el ID: " + id, Toast.LENGTH_LONG).show();
                }
            }
        });

        // BOTÓN BORRAR
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String id = txtId.getText().toString();

                if (id.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa el ID para borrar", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Usamos el código db.delete que solicitaste, pero dinámico:
                int filasBorradas = db.delete("usuarios",
                        "id=?",
                        new String[]{id});

                if (filasBorradas > 0) {
                    Toast.makeText(MainActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                    txtId.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "No se encontró el ID: " + id, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // BOTÓN GUARDAR
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString().trim();
                String correo = txtCorreo.getText().toString().trim();
                String telefono = txtTelefono.getText().toString().trim();

                if (nombre.isEmpty()) {
                    txtNombre.setError("Nombre requerido");
                    txtNombre.requestFocus();
                    return;
                }
                if (correo.isEmpty()) {
                    txtCorreo.setError("Correo requerido");
                    txtCorreo.requestFocus();
                    return;
                }
                if (telefono.isEmpty()) {
                    txtTelefono.setError("Teléfono requerido");
                    txtTelefono.requestFocus();
                    return;
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("nombre", nombre);
                values.put("correo", correo);
                values.put("telefono", telefono);

                db.insert("usuarios", null, values);

                Toast.makeText(MainActivity.this, "Usuario guardado con éxito", Toast.LENGTH_LONG).show();

                txtNombre.setText("");
                txtCorreo.setText("");
                txtTelefono.setText("");
                txtNombre.requestFocus();
            }
        });
    }
}
