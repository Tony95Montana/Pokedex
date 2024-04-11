package com.example.pokedex.models;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private final int id;
    private final String nom;
    private String height;
    private String poids;
    private ArrayList<String> types;
    private ArrayList<Talent> talents;

    public Pokemon(int id, String nom, String height, String poids) {
        this.id = id;
        this.nom = nom;
        this.height = height;
        this.poids = poids;
        this.types = new ArrayList<>();
        this.talents = new ArrayList<>();
    }
    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getHeight() {return height;}
    public void setHeight(String height) {
        this.height = height;
    }
    public String getPoids() {
        return poids;
    }
    public void setPoids(String poids) {
        this.poids = poids;
    }
    public ArrayList<String> getTypes() {
        return types;
    }
    public void setTypes(@NonNull JSONArray types) {
        this.types = new ArrayList<>();
        for (int i = 0; i < types.length(); i++) {
            try {
                this.types.add(types.getJSONObject(i).getJSONObject("type").getString("name"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public ArrayList<Talent> getTalents() {return talents;}
    public void setTalents(@NonNull JSONArray talents) {
        this.talents = new ArrayList<>();
        for (int i = 0; i < talents.length(); i++) {
            try {
                this.talents.add(new Talent("", "", talents.getJSONObject(i).getJSONObject("ability").getString("url"), talents.getJSONObject(i).getBoolean("is_hidden")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
