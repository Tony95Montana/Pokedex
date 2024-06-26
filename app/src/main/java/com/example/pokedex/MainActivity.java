package com.example.pokedex;

import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.pokedex.fragments.PokemonFragment;
import com.example.pokedex.fragments.SearchFragment;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.services.SearchObserver;

public class MainActivity extends AppCompatActivity implements SearchObserver {
    private SearchFragment searchFragment;
    private PokemonFragment pokemonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
    public void onReceivePokemonInfo(Pokemon pokemon) {}
    @Override
    public void onReceivePokemonData(@NonNull Pokemon pokemon) {
        pokemonFragment.onSelectPokemon(pokemon);
        getSupportFragmentManager().beginTransaction().hide(searchFragment).show(pokemonFragment).commit();
        // on click "button retour"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            public void handleOnBackPressed() {
                getSupportFragmentManager().beginTransaction().hide(pokemonFragment).show(searchFragment).commit();
            }
        });
    }
}