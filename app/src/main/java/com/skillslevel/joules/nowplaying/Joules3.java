package com.skillslevel.joules.nowplaying;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skillslevel.joules.R;

public class Joules3 extends BaseNowplayingFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_timber3, container, false);

        setMusicStateListener();
        setSongDetails(rootView);

        return rootView;
    }
}
