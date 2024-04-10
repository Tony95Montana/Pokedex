package com.example.pokedex.services;

import com.example.pokedex.models.Pokemon;

public interface SearchObserver {
    void onReceivePokemonInfo(Pokemon pokemon);
}