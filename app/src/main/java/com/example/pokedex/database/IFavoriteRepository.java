package com.example.pokedex.database;

import com.example.pokedex.models.Pokemon;
import java.util.ArrayList;

public interface IFavoriteRepository {
    boolean add(Pokemon m);
    boolean remove(Pokemon m);
    boolean isFavorite(Pokemon m);
    ArrayList<Pokemon> getAll();
}