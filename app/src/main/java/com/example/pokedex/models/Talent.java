package com.example.pokedex.models;

public class Talent {
    private String nom;
    private String description;
    private String link;
    private Boolean hidden;
    public Talent(String nom, String description, String link, boolean hidden) {
        this.nom = nom;
        this.description = description;
        this.link = link;
        this.hidden = hidden;
    }
    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}
    public Boolean getHidden() {return hidden;}
    public void setHidden(Boolean hidden) {this.hidden = hidden;}
}
