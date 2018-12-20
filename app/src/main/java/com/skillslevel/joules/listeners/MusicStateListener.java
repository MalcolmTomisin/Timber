package com.skillslevel.joules.listeners;

public interface MusicStateListener {
    /**
     * Listens for playback changes to send the the fragments bound to this activity
     */
    /**
     * Called when {@link com.skillslevel.joules.MusicService#REFRESH} is invoked
     */
    void restartLoader();

    /**
     * Called when {@link com.skillslevel.joules.MusicService#PLAYLIST_CHANGED} is invoked
     */
    void onPlaylistChanged();

    /**
     * Called when {@link com.skillslevel.joules.MusicService#META_CHANGED} is invoked
     */
    void onMetaChanged();
}
