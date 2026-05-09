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

    EditText txtId, txtNombre, txtCorreo;
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
                    stringBuilder.append("ID: ").append(id)
                            .append(" - ").append(nombre)
                            .append(" (").append(correo).append(")\n");
                }
                cursor.close();
                txtResultado.setText(stringBuilder.toString());
            }
        });

        // BOTÓN ACTUALIZAR
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                String id = txtId.getText().toString();
                String nombre = txtNombre.getText().toString();
                String correo = txtCorreo.getText().toString();

                if (id.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingresa un ID para actualizar", Toast.LENGTH_SHORT).show();
                    return;
                }

                values.put("nombre", nombre);
                values.put("correo", correo);

                int filasAfectadas = db.update("usuarios",
                        values,
                        "id=?",
                        new String[]{id});

                if (filasAfectadas > 0) {
                    Toast.makeText(MainActivity.this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No se encontró el ID: " + id, Toast.LENGTH_SHORT).show();
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

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put("nombre", txtNombre.getText().toString());
                values.put("correo", txtCorreo.getText().toString());

                db.insert("usuarios", null, values);

                Toast.makeText(MainActivity.this,
                        "Usuario guardado",
                        Toast.LENGTH_SHORT).show();

                txtNombre.setText("");
                txtCorreo.setText("");
            }
        });
    }
}
