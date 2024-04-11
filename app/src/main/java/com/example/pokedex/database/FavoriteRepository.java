package com.example.pokedex.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import com.example.pokedex.models.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FavoriteRepository implements IFavoriteRepository{
    private DatabaseManager dbm;
    private static FavoriteRepository instance;
    private FavoriteRepository(Context context){
        this.dbm = DatabaseManager.getInstance(context);
    }

    public static FavoriteRepository getInstance(Context context){
        if (instance == null) instance= new FavoriteRepository(context);
        return instance;
    }
    @Override
    public boolean add(Pokemon m) {
        if(isFavorite(m)) return false;
        ContentValues values = new ContentValues();
        values.put("id", m.getId());
        values.put("nom", m.getNom());
        values.put("height", m.getHeight());
        values.put("poids", m.getPoids());
        StringBuilder types = new StringBuilder();
        for (int i = 0; i < m.getTypes().size(); i++) {
            types.append(m.getTypes().get(i));
        }
        values.put("types", String.valueOf(types));
        StringBuilder talents = new StringBuilder();
        for (int i = 0; i < m.getTalents().size(); i++) {
            talents.append(m.getTalents().get(i).getNom());
        }
        values.put("talents", String.valueOf(talents));
        long line= dbm.getWritableDatabase().insert("favorite", null, values);
        return line != 0;
    }
    @Override
    public boolean remove(@NonNull Pokemon m) {
        String[] identifier = {String.valueOf(m.getId())};
        long line= dbm.getWritableDatabase().delete("favorite", "id=?", identifier);
        return line != 0;
    }
    @Override
    public boolean isFavorite(@NonNull Pokemon m) {
        String[] identifier = {String.valueOf(m.getId())};
        @SuppressLint("Recycle") Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite where id = ?", identifier);
        return c.getCount() > 0;
    }
    @Override
    public ArrayList<Pokemon> getAll() {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        Cursor c = dbm.getReadableDatabase().rawQuery("select * from favorite ", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Pokemon m = new Pokemon(c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3));
            try {
                JSONArray types = new JSONArray(c.getString(4));
                JSONArray talents = new JSONArray(c.getString(5));
                m.setTypes(types);
                m.setTalents(talents);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            pokemons.add(m);
            c.moveToNext();
        }
        c.close();
        return pokemons;
    }
}
