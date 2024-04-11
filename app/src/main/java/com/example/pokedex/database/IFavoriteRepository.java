package net.merryservices.appmusics.database;

import net.merryservices.appmusics.model.Music;

import java.util.ArrayList;

public interface IFavoriteRepository {

    public boolean add(Music m);

    public boolean remove(Music m);

    public boolean isFavorite(Music m);

    public ArrayList<Music> getAll();

}