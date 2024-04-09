package com.example.pokedex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.fragments.PokemonFragment;
import com.example.pokedex.fragments.SearchFragment;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.services.SearchObserver;

public class MainActivity extends AppCompatActivity implements SearchObserver {
    private SearchFragment searchFragment;
    private PokemonFragment pokemonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchFragment = new SearchFragment();
        searchFragment.setListener(this);
        pokemonFragment = new PokemonFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, searchFragment)
                .add(R.id.frameLayout, pokemonFragment)
                .hide(pokemonFragment).commit();
    }
    @Override
    public void onReceivePokemonInfo(Pokemon pokemon) {
        pokemonFragment.onSelectPokemon(pokemon);
        getSupportFragmentManager().beginTransaction().hide(searchFragment).show(pokemonFragment).commit();
    }
}