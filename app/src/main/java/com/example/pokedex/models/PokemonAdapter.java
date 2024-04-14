package com.example.pokedex.models;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokedex.R;
import com.example.pokedex.services.ApiServices;

import java.util.ArrayList;

public class PokemonAdapter extends BaseAdapter {
    private ArrayList<Pokemon> pokemons;
    private final Context context;

    public PokemonAdapter(ArrayList<Pokemon> pokemons, Context context) {
        this.pokemons = pokemons;
        this.context = context;
    }
    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
    @Override
    public int getCount() {
        return pokemons.size();
    }
    @Override
    public Object getItem(int position) {
        return pokemons.get(position);
    }
    @Override
    public long getItemId(int position) {
        return pokemons.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false);
        TextView textViewNom = convertView.findViewById(R.id.textViewNom);
        textViewNom.setText(pokemons.get(position).getNom());
        TextView textViewId = convertView.findViewById(R.id.textViewId);
        textViewId.setText(Html.fromHtml("N° <strong><i>"+pokemons.get(position).getId()+"</i></strong>", Html.FROM_HTML_MODE_COMPACT));

        ImageView imageView = convertView.findViewById(R.id.imageViewItemPokemon);
        imageView.setImageResource(R.mipmap.no_pokemon);
        ApiServices.loadPokemonAvatar(context, pokemons.get(position).getId(), false, imageView);
        return convertView;
    }
}
