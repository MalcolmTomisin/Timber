package com.skillslevel.joules.models;

public class Favourites extends Song {
    public final int fav;

    public Favourites(){
        this.fav = -1;
    }

    public Favourites ( long _id, long _albumId, long _artistId,
                        String _title, String _artistName, String _albumName, int _duration, int _trackNumber, int _fav){
    super(_id, _albumId, _artistId, _title, _artistName,_albumName,_duration, _trackNumber);
        this.fav = _fav;
    }
}
