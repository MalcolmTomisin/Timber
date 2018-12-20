package com.skillslevel.joules.dataloaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.skillslevel.joules.models.Album;
import com.skillslevel.joules.utils.PreferencesUtil;


import java.util.ArrayList;
import java.util.List;

public class AlbumLoader {

    public static Album getAlbum (Cursor cursor){
        Album album = new Album();
        if (cursor != null){
            if (cursor.moveToFirst())
                album = new Album(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getLong(3),
                        cursor.getInt(4), cursor.getInt(5));
        }
        if (cursor != null)
            cursor.close();
        return album;
    }


    public static List <Album> getAlbumsForCursor (Cursor cursor){
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new Album(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getLong(3),
                        cursor.getInt(4), cursor.getInt(5)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static Cursor makeAlbumCursor(Context context, String selection, String[] paramArrayOfString){
        final String albumSortOrder = PreferencesUtil.getInstance(context).getAlbumSortOrder();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"},
                selection, paramArrayOfString, albumSortOrder);
        return cursor;
    }

}
