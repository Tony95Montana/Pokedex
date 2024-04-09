package com.example.pokedex.models;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private int id;
    private String nom;
    private String avatar;
    private String poids;
    private ArrayList<String> types;
    private String zone;

    public Pokemon(int id, String nom, String avatar, String poids, @NonNull JSONArray types, String zone) {
        this.id = id;
        this.nom = nom;
        this.avatar = avatar;
        this.poids = poids;
        this.types = new ArrayList<>();
        for (int i = 0; i < types.length(); i++) {
            try {
                this.types.add(types.getJSONObject(i).getJSONObject("type").getString("name"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        this.zone = zone;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }
    public String getZone() {return zone;}
    public void setZone(String zone) {
        this.zone = zone;
    }
}
