package com.example.pokedex.services;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokedex.models.Pokemon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiServices {
    private static final String URL_API="https://pokeapi.co/api/v2/pokemon?limit=10000";
    private static final String URL_AVATAR="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";
    private static final String URL_SHINY="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/";
    private static final String URL_DATA="https://pokeapi.co/api/v2/pokemon/";
    public static void loadPokemonData(Context context, int id, Pokemon pokemon, SearchObserver listener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest requestFinal = new StringRequest(URL_DATA + id, result -> {
            try {
                JSONObject pokeJSON = new JSONObject(result);
                pokemon.setHeight(pokeJSON.getString("height"));
                pokemon.setPoids(pokeJSON.getString("weight"));
                pokemon.setTypes(pokeJSON.getJSONArray("types"));
                pokemon.setTalents(pokeJSON.getJSONArray("abilities"));
                listener.onReceivePokemonData(pokemon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(requestFinal);
    }
    public static void getAllPokemon(Context context, SearchObserver listener) {
        new Thread(() -> {
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(URL_API, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Pokemon pokemon = new Pokemon(i + 1, results.getJSONObject(i).getString("name"), "", "");
                        listener.onReceivePokemonInfo(pokemon);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            queue.add(request);
        }).start();
    }
    public static void loadPokemonAvatar(Context context, int id, boolean shiny, @NonNull final ImageView imageView){
        new Thread(() -> {
            String url;
            if (shiny) url = URL_SHINY + id + ".png";
            else url = URL_AVATAR + id + ".png";
            RequestQueue queue = Volley.newRequestQueue(context);
            ImageRequest request = new ImageRequest(url,
                    imageView::setImageBitmap, 0, 0, null,
                    error -> imageView.setImageResource(android.R.drawable.ic_menu_gallery));
            queue.add(request);
        }).start();
    }
    public static void loadPokemonTalent(Context context, String url, final TextView nom,  final TextView desc, boolean hidden){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray name = jsonObject.getJSONArray("names");
                for(int i=0; i < name.length(); i++) {
                    if (name.getJSONObject(i).getJSONObject("language").getString("name").equals("fr")) {
                        if (hidden) nom.setText(Html.fromHtml(name.getJSONObject(i).getString("name")+"<br> <small>(<i>Talent Caché</i>)</small>", Html.FROM_HTML_MODE_COMPACT));
                        else nom.setText(name.getJSONObject(i).getString("name"));
                        break;
                    }
                }
                JSONArray description = jsonObject.getJSONArray("flavor_text_entries");
                for(int i=0; i < description.length(); i++) {
                    if (description.getJSONObject(i).getJSONObject("language").getString("name").equals("fr")) {
                        desc.setText(description.getJSONObject(i).getString("flavor_text"));
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(request);
    }
}
