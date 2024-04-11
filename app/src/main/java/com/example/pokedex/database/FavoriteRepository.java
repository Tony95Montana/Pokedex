package net.merryservices.appmusics.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.merryservices.appmusics.model.Music;

import java.util.ArrayList;

public class FavoriteRepository implements IFavoriteRepository{

    private DatabaseManager dbm;
    private static FavoriteRepository instance;

    private FavoriteRepository(Context context){
        this.dbm= DatabaseManager.getInstance(context);
    }

    public static FavoriteRepository getInstance(Context context){
        if(instance==null){
            instance= new FavoriteRepository(context);
        }
        return instance;
    }

    @Override
    public boolean add(Music m) {
        if(isFavorite(m)) return false;
        ContentValues values = new ContentValues();
        values.put("id", m.getId());
        values.put("title", m.getTitle());
        values.put("artist", m.getArtist());
        values.put("album", m.getAlbum());
        values.put("sampleUrl", m.getPreview());
        values.put("link", m.getLink());
        values.put("coverUrl", m.getImage());
        long line= dbm.getWritableDatabase().insert("favorite", null, values);
        return line != 0;
    }

    @Override
    public boolean remove(Music m) {
        String[] identifier = {String.valueOf(m.getId())};
        long line= dbm.getWritableDatabase().delete("favorite", "id=?", identifier);
        return line != 0;
    }

    @Override
    public boolean isFavorite(Music m) {
        String[] identifier = {String.valueOf(m.getId())};
        Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite where id=?", identifier);
        return c.getCount()>0;
    }

    @Override
    public ArrayList<Music> getAll() {
        ArrayList<Music> musics = new ArrayList<Music>();
        Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite ", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Music m= new Music();
            m.setId(c.getInt(0));
            m.setTitle(c.getString(1));
            m.setArtist(c.getString(2));
            m.setAlbum(c.getString(3));
            m.setPreview(c.getString(4));
            m.setLink(c.getString(5));
            m.setImage(c.getString(6));
            musics.add(m);
            c.moveToNext();
        }
        c.close();
        return musics;
    }
}
