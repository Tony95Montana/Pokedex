package com.example.pokedex.models;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private int id;
    private String nom;
    private String nomFR;
    private String height;
    private String poids;
    private ArrayList<String> types;
    private ArrayList<Talent> talents;
    private ArrayList<Integer> stats;
    private ArrayList<String> evolutions;
    private String link;
    private String cri;

    public Pokemon(int id, String nom, String height, String poids, String cri) {
        this.id = id;
        this.nom = nom;
        this.height = height;
        this.poids = poids;
        this.types = new ArrayList<>();
        this.talents = new ArrayList<>();
        this.stats = new ArrayList<>();
        this.cri = cri;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {this.nom = nom;}
    public String getNomFR() {
        return nomFR;
    }
    public void setNomFR(String nomFR) {this.nomFR = nomFR;}
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
        return this.types;
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
    public ArrayList<Talent> getTalents() {return this.talents;}
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
    public ArrayList<String> getEvolutions() {return this.evolutions;}
    public void setEvolutions(ArrayList<String> evolutions) {this.evolutions = evolutions;}
    public String getLink() {return this.link;}
    public void setLink(String link) {this.link = link;}
    public String getCri() {return this.cri;}
    public void setCri(String cri) {this.cri = cri;}
    public ArrayList<Integer> getStat() {return this.stats;}
    public void setStat(@NonNull JSONArray stat) {
        this.stats = new ArrayList<>();
        for (int i = 0; i < stat.length(); i++) {
            try {
                stats.add(Integer.parseInt(stat.getJSONObject(i).getString("base_stat")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
