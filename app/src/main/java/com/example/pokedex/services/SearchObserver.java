package com.example.pokedex.services;

import com.example.pokedex.models.Pokemon;

public interface SearchObserver {
    public void onReceivePokemonInfo(Pokemon pokemon);
}