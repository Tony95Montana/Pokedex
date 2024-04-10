package com.example.pokedex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.pokedex.R;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.Talent;
import com.example.pokedex.services.ApiServices;
import java.util.ArrayList;

public class PokemonFragment extends Fragment {
    private ImageButton imageButtonGoBack;
    private ImageView imageView, imageViewTypes1, imageViewTypes2;
    private TextView textViewTitle, textViewTaille, textViewPoids, textViewTalent1, textViewTalent2, textViewDescription1, textViewDescription2;
    private boolean shiny;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_pokemon, null);
        super.onCreate(savedInstanceState);
        textViewTitle = v.findViewById(R.id.textViewTitle);
        textViewTaille = v.findViewById(R.id.textViewTaille);
        textViewPoids = v.findViewById(R.id.textViewPoids);
        textViewTalent1 = v.findViewById(R.id.textViewTalent1);
        textViewTalent2 = v.findViewById(R.id.textViewTalent2);
        textViewDescription1 = v.findViewById(R.id.textViewDescription1);
        textViewDescription2 = v.findViewById(R.id.textViewDescription2);
        imageViewTypes1 = v.findViewById(R.id.imageViewTypes1);
        imageViewTypes2 = v.findViewById(R.id.imageViewTypes2);
        imageView = v.findViewById(R.id.imageViewPokemon);
        imageButtonGoBack = v.findViewById(R.id.imageButtonGoBack);
        this.shiny = false;
        return v;
    }
    @SuppressLint("SetTextI18n")
    public void onSelectPokemon(@NonNull Pokemon pokemon) {
        textViewTitle.setText(Html.fromHtml(pokemon.getNom() + " (N° <strong><i>" + pokemon.getId() + "</i></strong>)", Html.FROM_HTML_MODE_COMPACT));
        ApiServices.loadPokemonAvatar(getContext(), pokemon.getAvatar(), imageView);
        imageView.setOnClickListener(v -> {
            if (shiny) {
                shiny = false;
                ApiServices.loadPokemonAvatar(getContext(), pokemon.getAvatar(), imageView);
            }
            else {
                shiny = true;
                ApiServices.loadPokemonAvatar(getContext(), pokemon.getShiny(), imageView);
            }
        });
        ArrayList<Talent> talents = pokemon.getTalents();
        for (int i = 0; i < talents.size(); i++) {
            if (i == 0) ApiServices.loadPokemonTalent(getContext(), talents.get(i).getLink(), textViewTalent1, textViewDescription1, talents.get(i).getHidden());
            else ApiServices.loadPokemonTalent(getContext(), talents.get(i).getLink(), textViewTalent2, textViewDescription2, talents.get(i).getHidden());
        }
        textViewTaille.setText("Taille : "+(Double.parseDouble(pokemon.getHeight())/10)+" m");
        textViewPoids.setText("Poids : "+(Double.parseDouble(pokemon.getPoids())/10)+" Kg");
        for (int i = 0; i < pokemon.getTypes().size(); i++) {
            int image = 0;
            switch (pokemon.getTypes().get(i)) {
                case "fire":
                    image = R.mipmap.fire;
                    break;
                case "water":
                    image = R.mipmap.water;
                    break;
                case "poison":
                    image = R.mipmap.poison;
                    break;
                case "grass":
                    image = R.mipmap.grass;
                    break;
            }
            if (i == 0) imageViewTypes1.setImageResource(image);
            else imageViewTypes2.setImageResource(image);
        }
        imageButtonGoBack.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }
}
