package com.example.pokedex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.pokedex.R;
import com.example.pokedex.database.FavoriteRepository;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonAdapter;
import com.example.pokedex.services.ApiServices;
import com.example.pokedex.services.SearchObserver;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, SearchObserver, AdapterView.OnItemClickListener {
    private ProgressBar progressBar;
    private ImageButton imageButtonFav;
    private PokemonAdapter adapter;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Pokemon> res;
    ArrayList<Pokemon> listFavoris;
    private boolean research;
    private boolean favoris;
    private SearchObserver listener;
    private int finish;
    public void setListener(SearchObserver listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_search, null);
        SearchView searchView = v.findViewById(R.id.searchViewMain);
        searchView.setOnQueryTextListener(this);
        progressBar = v.findViewById(R.id.pBar);
        ListView listView = v.findViewById(R.id.listViewMain);
        imageButtonFav = v.findViewById(R.id.imageButtonFav);
        imageButtonFav.setOnClickListener(v2 -> {
            research = false;
            if (favoris) {
                imageButtonFav.setImageResource(R.mipmap.etoile_vide_black);
                favoris = false;
                adapter.setPokemons(pokemons);
                adapter.notifyDataSetChanged();
            } else {
                listFavoris.clear();
                listFavoris = FavoriteRepository.getInstance(getContext()).getAll();
                adapter.setPokemons(listFavoris);
                adapter.notifyDataSetChanged();
                imageButtonFav.setImageResource(R.mipmap.etoile_pleine_black);
                favoris = true;
            }
        });
        listFavoris = new ArrayList<>();
        pokemons = new ArrayList<>();
        res = new ArrayList<>();
        favoris = false;
        research = false;
        adapter = new PokemonAdapter(pokemons, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        ApiServices.getAllPokemon(getContext(), this);
        finish = 0;
        return v;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (favoris) {
            imageButtonFav.setImageResource(R.mipmap.etoile_vide_black);
            favoris = false;
        }
        res.clear();
        for (int i = 0; i < pokemons.size(); i++) {
            try {
                int value = Integer.parseInt(query);
                if (pokemons.get(i).getId() == value) res.add(pokemons.get(i));
            } catch (NumberFormatException e) {
                if (pokemons.get(i).getNom().contains(query)) res.add(pokemons.get(i));
            }
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
        if (!pokemons.contains(pokemon)) {
            finish += 1;
            if (finish == 1000) progressBar.setVisibility(View.INVISIBLE);
            pokemons.add(pokemon);
            adapter.setPokemons(pokemons);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onReceivePokemonData(Pokemon pokemon) {}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (research) listener.onReceivePokemonData(res.get(position));
        else if (favoris) listener.onReceivePokemonData(listFavoris.get(position));
        else listener.onReceivePokemonData(pokemons.get(position));
    }
}
