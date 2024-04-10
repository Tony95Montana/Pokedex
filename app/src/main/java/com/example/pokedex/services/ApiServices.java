package com.example.pokedex.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.Talent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiServices {
    private static final String URL_API="https://pokeapi.co/api/v2/pokemon?limit=20";
    private static void getUrlPokemon(Context context, String url, SearchObserver listener){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest requestFinal = new StringRequest(url, result -> {
            try {
                JSONObject pokeJSON = new JSONObject(result);
                Pokemon pokemon = new Pokemon(pokeJSON.getInt("id"),
                        pokeJSON.getString("name"),
                        pokeJSON.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default"),
                        pokeJSON.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_shiny"),
                        pokeJSON.getString("height"),
                        pokeJSON.getString("weight"),
                        pokeJSON.getJSONArray("types"),
                        pokeJSON.getJSONArray("abilities"),
                        pokeJSON.getString("location_area_encounters"));
                listener.onReceivePokemonInfo(pokemon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(requestFinal);
    }
    public static void getAllPokemon(Context context, SearchObserver listener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(URL_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray results = jsonObject.getJSONArray("results");
                for(int i=0; i<results.length(); i++) {
                    getUrlPokemon(context, results.getJSONObject(i).getString("url"), listener);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
    public static void loadPokemonAvatar(Context context, String url, final ImageView imageView){
        RequestQueue queue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                error -> imageView.setImageResource(android.R.drawable.ic_menu_gallery));
        queue.add(request);
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
        }, error -> error.printStackTrace());
        queue.add(request);
    }
}
