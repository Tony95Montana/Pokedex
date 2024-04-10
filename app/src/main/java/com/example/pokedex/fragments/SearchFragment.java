package com.example.pokedex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.pokedex.R;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonAdapter;
import com.example.pokedex.services.ApiServices;
import com.example.pokedex.services.SearchObserver;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, SearchObserver, AdapterView.OnItemClickListener {
    private SearchView searchView;
    private ListView listView;
    private PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    private SearchObserver listener;
    public void setListener(SearchObserver listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_search, null);
        searchView = v.findViewById(R.id.searchViewMain);
        searchView.setOnQueryTextListener(this);
        listView = v.findViewById(R.id.listViewMain);
        pokemons = new ArrayList<>();
        adapter = new PokemonAdapter(pokemons, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        ApiServices.getAllPokemon(getContext(), this);
        return v;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        ArrayList<Pokemon> newList = new ArrayList<>();
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getNom().contains(query)) newList.add(pokemons.get(i));
        }
        pokemons.clear();
        pokemons = newList;
        adapter.setPokemons(pokemons);
        adapter.notifyDataSetChanged();
        return false;
    }
    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        if (newText.isEmpty()) ApiServices.getAllPokemon(getContext(), this);
        return false;
    }
    @Override
    public void onReceivePokemonInfo(Pokemon pokemon) {
        if(!pokemons.contains(pokemon)){
            pokemons.add(pokemon);
            adapter.setPokemons(pokemons);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onReceivePokemonInfo(pokemons.get(position));
    }
}
