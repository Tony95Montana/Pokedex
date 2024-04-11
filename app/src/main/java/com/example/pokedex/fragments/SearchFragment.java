package com.example.pokedex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    private ProgressBar progressBar;
    private ListView listView;
    private PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Pokemon> res;
    private boolean research;
    private SearchObserver listener;
    private int offset;
    private int finish;
    public void setListener(SearchObserver listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_search, null);
        searchView = v.findViewById(R.id.searchViewMain);
        searchView.setOnQueryTextListener(this);
        progressBar = v.findViewById(R.id.pBar);
        listView = v.findViewById(R.id.listViewMain);
        pokemons = new ArrayList<>();
        res = new ArrayList<>();
        research = false;
        adapter = new PokemonAdapter(pokemons, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        offset = 0;
        ApiServices.getAllPokemon(getContext(), offset, 200, this);
        offset = 200;
        finish = 0;
        return v;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        res.clear();
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getNom().contains(query)) res.add(pokemons.get(i));
        }
        adapter.setPokemons(res);
        adapter.notifyDataSetChanged();
        research = true;
        return false;
    }
    @Override
    public boolean onQueryTextChange(@NonNull String newText) {
        if (newText.isEmpty()) {
            research = false;
            adapter.setPokemons(pokemons);
            adapter.notifyDataSetChanged();
        }
        return false;
    }
    @Override
    public void onReceivePokemonInfo(Pokemon pokemon) {
        if(!pokemons.contains(pokemon)){
            finish += 1;
            if (finish == offset) progressBar.setVisibility(View.INVISIBLE);
            pokemons.add(pokemon);
            adapter.setPokemons(pokemons);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (research) listener.onReceivePokemonInfo(res.get(position));
        else listener.onReceivePokemonInfo(pokemons.get(position));
    }
}
