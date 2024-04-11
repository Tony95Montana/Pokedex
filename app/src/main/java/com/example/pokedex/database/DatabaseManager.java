package com.example.pokedex.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME= "mydb.sqlite";
    private static final int CURRENT_DB_VERSION = 1;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context){
        if (instance == null) instance = new DatabaseManager(context);
        return instance;
    }

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DB_VERSION);
    }
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("create table favorite (id INTEGER, nom TEXT, height TEXT, poids TEXT, types TEXT, talents TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
