package com.example.pokedex.models;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private int id;
    private String nom;
    private String avatar;
    private String shiny;
    private String height;
    private String poids;
    private ArrayList<String> types;
    private ArrayList<Talent> talents;
    private String zone;

    public Pokemon(int id, String nom, String avatar, String shiny, String height, String poids, @NonNull JSONArray types, @NonNull JSONArray talents, String zone) {
        this.id = id;
        this.nom = nom;
        this.avatar = avatar;
        this.shiny = shiny;
        this.height = height;
        this.poids = poids;
        this.types = new ArrayList<>();
        for (int i = 0; i < types.length(); i++) {
            try {
                this.types.add(types.getJSONObject(i).getJSONObject("type").getString("name"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        this.talents = new ArrayList<>();
        for (int i = 0; i < talents.length(); i++) {
            try {
                this.talents.add(new Talent("", "", talents.getJSONObject(i).getJSONObject("ability").getString("url"), talents.getJSONObject(i).getBoolean("is_hidden")));
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
    public String getShiny() {
        return shiny;
    }
    public void setShiny(String shiny) {
        this.shiny = shiny;
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
    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }
    public ArrayList<Talent> getTalents() {return talents;}
    public void setTalents(ArrayList<Talent> talents) {this.talents = talents;}
    public String getZone() {return zone;}
    public void setZone(String zone) {
        this.zone = zone;
    }
}
