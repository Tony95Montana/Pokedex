package com.example.pokedex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.pokedex.R;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.services.ApiServices;

public class PokemonFragment extends Fragment {
    private ImageView imageView, imageViewTypes1, imageViewTypes2;
    private TextView textViewTitle, textViewPoids;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_pokemon, null);
        super.onCreate(savedInstanceState);
        textViewTitle = v.findViewById(R.id.textViewTitle);
        textViewPoids = v.findViewById(R.id.textViewPoids);
        imageViewTypes1 = v.findViewById(R.id.imageViewTypes1);
        imageViewTypes2 = v.findViewById(R.id.imageViewTypes2);
        imageView = v.findViewById(R.id.imageViewPokemon);
        return v;
    }
    @SuppressLint("SetTextI18n")
    public void onSelectPokemon(@NonNull Pokemon pokemon) {
        textViewTitle.setText(Html.fromHtml(pokemon.getNom() + " (N° <strong><i>" + pokemon.getId() + "</i></strong>)", Html.FROM_HTML_MODE_COMPACT));
        ApiServices.loadPokemonAvatar(getContext(), pokemon.getAvatar(), imageView);
        textViewPoids.setText("Poids : "+pokemon.getPoids()+" Kg");
        for (int i = 0; i < pokemon.getTypes().size(); i++) {
            int image = 0;
            if (pokemon.getTypes().get(i).equals("fire")) image = R.mipmap.fire;
            else if (pokemon.getTypes().get(i).equals("water")) image = R.mipmap.water;
            else if (pokemon.getTypes().get(i).equals("poison")) image = R.mipmap.poison;
            else if (pokemon.getTypes().get(i).equals("grass")) image = R.mipmap.grass;
            if (i == 0) imageViewTypes1.setImageResource(image);
            else imageViewTypes2.setImageResource(image);
        }
    }
}
