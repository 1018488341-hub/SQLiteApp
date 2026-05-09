package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductosActivity extends AppCompatActivity {

    EditText txtIdProd, txtNombreProd, txtPrecioProd;
    Button btnGuardarProd, btnMostrarProd, btnActualizarProd, btnBorrarProd;
    TextView txtResultadoProd;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        txtIdProd = findViewById(R.id.txtIdProd);
        txtNombreProd = findViewById(R.id.txtNombreProd);
        txtPrecioProd = findViewById(R.id.txtPrecioProd);
        btnGuardarProd = findViewById(R.id.btnGuardarProd);
        btnMostrarProd = findViewById(R.id.btnMostrarProd);
        btnActualizarProd = findViewById(R.id.btnActualizarProd);
        btnBorrarProd = findViewById(R.id.btnBorrarProd);
        txtResultadoProd = findViewById(R.id.txtResultadoProd);

        dbHelper = new DatabaseHelper(this);

        // BOTÓN MOSTRAR
        btnMostrarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM productos", null);

                StringBuilder stringBuilder = new StringBuilder();
                while(cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String nombre = cursor.getString(1);
                    float precio = cursor.getFloat(2);
                    stringBuilder.append("ID: ").append(id)
                            .append(" - ").append(nombre)
                            .append(" ($").append(precio).append(")\n");
                }
                cursor.close();
                txtResultadoProd.setText(stringBuilder.toString());
            }
        });

        // BOTÓN ACTUALIZAR
        btnActualizarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                String id = txtIdProd.getText().toString();
                String nombre = txtNombreProd.getText().toString();
                String precio = txtPrecioProd.getText().toString();

                if (id.isEmpty()) {
                    Toast.makeText(ProductosActivity.this, "Ingresa un ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                values.put("ProdNombre", nombre);
                try {
                    float precioFloat = Float.parseFloat(precio);
                    values.put("Precio", precioFloat);
                } catch (NumberFormatException e) {
                    values.put("Precio", 0.0f);
                }

                int filas = db.update("productos", values, "id=?", new String[]{id});

                if (filas > 0) {
                    Toast.makeText(ProductosActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductosActivity.this, "No se encontró el ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // BOTÓN BORRAR
        btnBorrarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String id = txtIdProd.getText().toString();

                if (id.isEmpty()) {
                    Toast.makeText(ProductosActivity.this, "Ingresa un ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                int filas = db.delete("productos", "id=?", new String[]{id});

                if (filas > 0) {
                    Toast.makeText(ProductosActivity.this, "Producto borrado", Toast.LENGTH_SHORT).show();
                    txtIdProd.setText("");
                } else {
                    Toast.makeText(ProductosActivity.this, "No se encontró el ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // BOTÓN GUARDAR
        btnGuardarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("ProdNombre", txtNombreProd.getText().toString());
                String precioStr = txtPrecioProd.getText().toString();
                try {
                    float precioFloat = Float.parseFloat(precioStr);
                    values.put("Precio", precioFloat);
                } catch (NumberFormatException e) {
                    values.put("Precio", 0.0f);
                }

                db.insert("productos", null, values);

                Toast.makeText(ProductosActivity.this, "Producto guardado", Toast.LENGTH_SHORT).show();
                txtNombreProd.setText("");
                txtPrecioProd.setText("");
            }
        });
    }
}
