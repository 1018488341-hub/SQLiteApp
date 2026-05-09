package com.example.sqliteapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre DB
    private static final String DATABASE_NAME = "empresa.db";

    // Versión
    private static final int DATABASE_VERSION = 3;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Crear tablas
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "correo TEXT," +
                "telefono TEXT)";

        db.execSQL(sql);

        String sql2 = "CREATE TABLE productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ProdNombre TEXT," +
                "Precio FLOAT)";

        db.execSQL(sql2);
    }
    

    // Actualización
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }

}
