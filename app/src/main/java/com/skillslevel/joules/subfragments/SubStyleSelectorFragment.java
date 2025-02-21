package com.skillslevel.joules.subfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skillslevel.joules.R;
import com.skillslevel.joules.utils.Constants;
import com.skillslevel.joules.utils.PreferencesUtil;

public class SubStyleSelectorFragment extends Fragment {
    private static final String ARG_PAGE_NUMBER = "pageNumber";
    private static final String WHAT = "what";
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    LinearLayout currentStyle;
    View foreground;
    ImageView styleImage;

    public static SubStyleSelectorFragment newInstance(int pageNumber, String what) {
        SubStyleSelectorFragment fragment = new SubStyleSelectorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
        bundle.putString(WHAT, what);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_style_selector_pager, container, false);

        TextView styleName = (TextView) rootView.findViewById(R.id.style_name);
        styleName.setText(String.valueOf(getArguments().getInt(ARG_PAGE_NUMBER) + 1));

        styleImage = (ImageView) rootView.findViewById(R.id.style_image);
        styleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreferences();
            }
        });

        switch (getArguments().getInt(ARG_PAGE_NUMBER)) {
            case 0:
                styleImage.setImageResource(R.drawable.timber_1_nowplaying_x);
                break;
            case 1:
                styleImage.setImageResource(R.drawable.timber_2_nowplaying_x);
                break;
            case 2:
                styleImage.setImageResource(R.drawable.timber_3_nowplaying_x);
                break;
            case 3:
                styleImage.setImageResource(R.drawable.timber_4_nowplaying_x);
                break;
        }

        currentStyle = (LinearLayout) rootView.findViewById(R.id.currentStyle);
        foreground = rootView.findViewById(R.id.foreground);

        setCurrentStyle();

        return rootView;
    }

    public void setCurrentStyle() {
        preferences = getActivity().getSharedPreferences(Constants.FRAGMENT_ID, Context.MODE_PRIVATE);
        String fragmentID = preferences.getString(Constants.NOWPLAYING_FRAGMENT_ID, Constants.JOULES3);

        ((StyleSelectorFragment) getParentFragment()).scrollToCurrentStyle(getIntForCurrentNowplaying(fragmentID));

        if (getArguments().getInt(ARG_PAGE_NUMBER) == getIntForCurrentNowplaying(fragmentID)) {
            currentStyle.setVisibility(View.VISIBLE);
            foreground.setVisibility(View.VISIBLE);
        } else {
            currentStyle.setVisibility(View.GONE);
            foreground.setVisibility(View.GONE);
        }

    }

    private void setPreferences() {

        if (getArguments().getString(WHAT).equals(Constants.SETTINGS_STYLE_SELECTOR_NOWPLAYING)) {
            editor = getActivity().getSharedPreferences(Constants.FRAGMENT_ID, Context.MODE_PRIVATE).edit();
            editor.putString(Constants.NOWPLAYING_FRAGMENT_ID, getStyleForPageNumber());
            editor.apply();
            if (getActivity() != null)
                PreferencesUtil.getInstance(getActivity()).setNowPlayingThemeChanged(true);
            setCurrentStyle();
            ((StyleSelectorFragment) getParentFragment()).updateCurrentStyle();
        }
    }

    private String getStyleForPageNumber() {
        switch (getArguments().getInt(ARG_PAGE_NUMBER)) {
            case 0:
                return Constants.JOULES1;
            case 1:
                return Constants.JOULES2;
            case 2:
                return Constants.JOULES3;
            case 3:
                return Constants.JOULES4;
            default:
                return Constants.JOULES3;
        }
    }

    private int getIntForCurrentNowplaying(String nowPlaying) {
        switch (nowPlaying) {
            case Constants.JOULES1:
                return 0;
            case Constants.JOULES2:
                return 1;
            case Constants.JOULES3:
                return 2;
            case Constants.JOULES4:
                return 3;
            default:
                return 2;
        }

    }

}
