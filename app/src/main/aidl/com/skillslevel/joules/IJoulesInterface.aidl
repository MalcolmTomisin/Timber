// IJoulesInterface.aidl
package com.skillslevel.joules;

// Declare any non-default types here with import statements

import com.skillslevel.joules.helpers.MusicPlaybackTrack;

interface IJoulesInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void openFile(String path);
        void open(in long [] list, int position, long sourceId, int sourceType);
        void stop();
        void pause();
        void play();
        void prev(boolean forcePrevious);
        void next();
        void enqueue(in long [] list, int action, long sourceId, int sourceType);
        void setQueuePosition(int index);
        void setShuffleMode(int shufflemode);
        void setRepeatMode(int repeatmode);
        void moveQueueItem(int from, int to);
        void refresh();
        void playlistChanged();
        boolean isPlaying();
        long [] getQueue();
        long getQueueItemAtPosition(int position);
        int getQueueSize();
        int getQueuePosition();
        int getQueueHistoryPosition(int position);
        int getQueueHistorySize();
        int[] getQueueHistoryList();
        long duration();
        long position();
        long seek(long pos);
        void seekRelative(long deltaInMs);
        long getAudioId();
        MusicPlaybackTrack getCurrentTrack();
        MusicPlaybackTrack getTrack(int index);
        long getNextAudioId();
        long getPreviousAudioId();
        long getArtistId();
        long getAlbumId();
        String getArtistName();
        String getTrackName();
        String getAlbumName();
        String getPath();
        int getShuffleMode();
        int removeTracks(int first, int last);
        int removeTrack(long id);
        boolean removeTrackAtPosition(long id, int position);
        int getRepeatMode();
        int getMediaMountedCount();
        int getAudioSessionId();
        void setLockscreenAlbumArt(boolean enabled);
}
