package com.example.pokedex.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.pokedex.R;
import com.example.pokedex.database.FavoriteRepository;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.Talent;
import com.example.pokedex.services.ApiServices;
import com.example.pokedex.services.SearchObserver;
import java.util.ArrayList;

public class PokemonFragment extends Fragment implements SearchObserver {
    private LinearLayout linearTalent, linearDescription;
    private Button buttonTalents;
    private ImageButton imageButtonGoBack;
    private ImageView imageView, imageViewTypes1, imageViewTypes2, imageViewEtoile, imageViewEvo1, imageViewEvo2, imageViewEvo3;
    private TextView textViewTitle, textViewTaille, textViewPoids, textViewTalent1, textViewTalent2, textViewDescription1, textViewDescription2;
    private ProgressBar ProgressBarHp, ProgressBarAttack, ProgressBarDefense, ProgressBarAttackSpecial, ProgressBarDefenseSpecial, ProgressBarSpeed;
    private boolean shiny, talent;
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
        imageViewEtoile = v.findViewById(R.id.imageViewEtoile);
        imageButtonGoBack = v.findViewById(R.id.imageButtonGoBack);
        imageViewEvo1 = v.findViewById(R.id.imageViewEvo1);
        imageViewEvo2 = v.findViewById(R.id.imageViewEvo2);
        imageViewEvo3 = v.findViewById(R.id.imageViewEvo3);
        buttonTalents = v.findViewById(R.id.buttonTalents);
        linearTalent = v.findViewById(R.id.linearTalent);
        linearDescription = v.findViewById(R.id.linearDescription);
        ProgressBarHp = v.findViewById(R.id.ProgressBarHp);
        ProgressBarAttack = v.findViewById(R.id.ProgressBarAttack);
        ProgressBarDefense = v.findViewById(R.id.ProgressBarDefense);
        ProgressBarAttackSpecial = v.findViewById(R.id.ProgressBarAttackSpecial);
        ProgressBarDefenseSpecial = v.findViewById(R.id.ProgressBarDefenseSpecial);
        ProgressBarSpeed = v.findViewById(R.id.ProgressBarSpeed);
        this.shiny = false;
        this.talent = false;
        return v;
    }
    private void cri(@NonNull String cri) {
        if (!cri.equals("null")) {
            AudioManager manager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
            manager.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
            MediaPlayer shootMP = MediaPlayer.create(getContext(), Uri.parse(cri));
            shootMP.start();
        }
    }
    public void onSelectPokemon(@NonNull Pokemon pokemon) {
        imageViewEvo1.setBackground(null);
        imageViewEvo2.setBackground(null);
        imageViewEvo3.setBackground(null);
        imageView.setImageResource(R.mipmap.no_pokemon);
        ApiServices.loadPokemonAvatar(getContext(), pokemon.getId(), false, imageView);
        FavoriteRepository favRepo = FavoriteRepository.getInstance(getContext());
        if (favRepo.isFavorite(pokemon)) imageViewEtoile.setImageResource(R.mipmap.etoile_pleine);
        else imageViewEtoile.setImageResource(R.mipmap.etoile_vide);
        imageViewEtoile.setOnClickListener(v -> {
            if (favRepo.isFavorite(pokemon)) {
                imageViewEtoile.setImageResource(R.mipmap.etoile_vide);
                favRepo.remove(pokemon);
            } else {
                imageViewEtoile.setImageResource(R.mipmap.etoile_pleine);
                favRepo.add(pokemon);
            }
        });
        buttonTalents.setOnClickListener(v -> {
            if (talent) {
                talent = false;
                linearTalent.setVisibility(View.GONE);
                linearDescription.setVisibility(View.GONE);
            } else {
                talent = true;
                linearTalent.setVisibility(View.VISIBLE);
                linearDescription.setVisibility(View.VISIBLE);
            }
        });
        textViewTitle.setText(Html.fromHtml(pokemon.getNom() + " N° <strong><i>" + pokemon.getId() + "</i></strong>", Html.FROM_HTML_MODE_COMPACT));
        ApiServices.loadPokemonData(getContext(), pokemon.getId(), pokemon, this);
        imageView.setOnClickListener(v -> {
            cri(pokemon.getCri());
            if (shiny) {
                shiny = false;
                ApiServices.loadPokemonAvatar(getContext(), pokemon.getId(), false, imageView);
            } else {
                shiny = true;
                ApiServices.loadPokemonAvatar(getContext(), pokemon.getId(), true, imageView);
            }
        });
        imageButtonGoBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }
    @Override
    public void onReceivePokemonInfo(Pokemon pokemon) {}
    @SuppressLint("SetTextI18n")
    @Override
    public void onReceivePokemonData(@NonNull Pokemon pokemon) {
        textViewTitle.setText(Html.fromHtml(pokemon.getNomFR() + " N° <strong><i>" + pokemon.getId() + "</i></strong>", Html.FROM_HTML_MODE_COMPACT));
        textViewTaille.setText("Taille : "+(Double.parseDouble(pokemon.getHeight())/10)+" m");
        textViewPoids.setText("Poids : "+(Double.parseDouble(pokemon.getPoids())/10)+" Kg");
        ArrayList<Talent> talents = pokemon.getTalents();
        for (int i = 0; i < talents.size(); i++) {
            if (i == 0) ApiServices.loadPokemonTalent(getContext(), talents.get(i).getLink(), textViewTalent1, textViewDescription1, talents.get(i).getHidden());
            else ApiServices.loadPokemonTalent(getContext(), talents.get(i).getLink(), textViewTalent2, textViewDescription2, talents.get(i).getHidden());
        }
        ArrayList<Integer> stats = pokemon.getStat();
        for (int i = 0; i < stats.size(); i++) {
            switch (i) {
                case 0:
                    ProgressBarHp.setProgress(stats.get(i));
                    break;
                case 1:
                    ProgressBarAttack.setProgress(stats.get(i));
                    break;
                case 2:
                    ProgressBarDefense.setProgress(stats.get(i));
                    break;
                case 3:
                    ProgressBarAttackSpecial.setProgress(stats.get(i));
                    break;
                case 4:
                    ProgressBarDefenseSpecial.setProgress(stats.get(i));
                    break;
                case 5:
                    ProgressBarSpeed.setProgress(stats.get(i));
                    break;
            }
        }
        for (int i = 0; i < pokemon.getTypes().size(); i++) {
            int image = 0;
            switch (pokemon.getTypes().get(i)) {
                case "fire":
                    image = R.mipmap.fire;
                    break;
                case "water":
                    image = R.mipmap.water;
                    break;
                case "electric":
                    image = R.mipmap.electric;
                    break;
                case "dark":
                    image = R.mipmap.dark;
                    break;
                case "dragon":
                    image = R.mipmap.dragon;
                    break;
                case "poison":
                    image = R.mipmap.poison;
                    break;
                case "psychic":
                    image = R.mipmap.psychic;
                    break;
                case "grass":
                    image = R.mipmap.grass;
                    break;
                case "ice":
                    image = R.mipmap.ice;
                    break;
                case "ghost":
                    image = R.mipmap.ghost;
                    break;
                case "fighting":
                    image = R.mipmap.fighting;
                    break;
                case "steel":
                    image = R.mipmap.steel;
                    break;
                case "ground":
                    image = R.mipmap.ground;
                    break;
                case "rock":
                    image = R.mipmap.rock;
                    break;
                case "flying":
                    image = R.mipmap.flying;
                    break;
                case "fairy":
                    image = R.mipmap.fairy;
                    break;
                case "bug":
                    image = R.mipmap.bug;
                    break;
                case "normal":
                    image = R.mipmap.normal;
                    break;
            }
            if (i == 0) imageViewTypes1.setImageResource(image);
            else imageViewTypes2.setImageResource(image);
        }
        cri(pokemon.getCri());
        if (pokemon.getEvolutions() != null) {
            for (int i = 0; i < pokemon.getEvolutions().size(); i++) {
                if (i == 0) {
                    if (Integer.parseInt(pokemon.getEvolutions().get(i).split("/")[6]) == pokemon.getId()) imageViewEvo1.setBackgroundResource(R.drawable.search_border);
                    ApiServices.loadPokemonAvatar(getContext(), Integer.parseInt(pokemon.getEvolutions().get(i).split("/")[6]), false, imageViewEvo1);
                } else if (i == 1) {
                    if (Integer.parseInt(pokemon.getEvolutions().get(i).split("/")[6]) == pokemon.getId()) imageViewEvo2.setBackgroundResource(R.drawable.search_border);
                    ApiServices.loadPokemonAvatar(getContext(), Integer.parseInt(pokemon.getEvolutions().get(i).split("/")[6]), false, imageViewEvo2);
                } else {
                    if (Integer.parseInt(pokemon.getEvolutions().get(i).split("/")[6]) == pokemon.getId()) imageViewEvo3.setBackgroundResource(R.drawable.search_border);
                    ApiServices.loadPokemonAvatar(getContext(), Integer.parseInt(pokemon.getEvolutions().get(i).split("/")[6]), false, imageViewEvo3);
                }
            }
        }
    }
}
