package com.skillslevel.joules.models;

public class Artist {
    public final int albumCount;
    public final long id;
    public final String name;
    public final int songCount;

    //overloaded constructors

    public Artist() {
        this.id = -1;
        this.name = "";
        this.songCount = -1;
        this.albumCount = -1;
    }

    public Artist(long _id, String _name, int _albumCount, int _songCount) {
        this.id = _id;
        this.name = _name;
        this.songCount = _songCount;
        this.albumCount = _albumCount;
    }
}
