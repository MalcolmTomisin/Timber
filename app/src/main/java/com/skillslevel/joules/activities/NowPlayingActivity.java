package com.skillslevel.joules.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.afollestad.appthemeengine.Config;
import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeengine.customizers.ATEToolbarCustomizer;
import com.skillslevel.joules.R;
import com.skillslevel.joules.utils.Constants;
import com.skillslevel.joules.utils.NavigationUtils;
import com.skillslevel.joules.utils.PreferencesUtil;

public class NowPlayingActivity extends BaseActivity implements ATEActivityThemeCustomizer, ATEToolbarCustomizer {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        SharedPreferences prefs = getSharedPreferences(Constants.FRAGMENT_ID, Context.MODE_PRIVATE);
        String fragmentID = prefs.getString(Constants.NOWPLAYING_FRAGMENT_ID, Constants.JOULES3);

        Fragment fragment = NavigationUtils.getFragmentForNowplayingID(fragmentID);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment).commit();

    }

    @Override
    public int getActivityTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ? R.style.AppTheme_FullScreen_Dark : R.style.AppTheme_FullScreen_Light;
    }

    @Override
    public int getLightToolbarMode(@Nullable Toolbar toolbar) {
        return Config.LIGHT_TOOLBAR_AUTO;
    }

    @Override
    public int getToolbarColor(@Nullable Toolbar toolbar) {
        return Color.TRANSPARENT;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferencesUtil.getInstance(this).didNowplayingThemeChanged()) {
            PreferencesUtil.getInstance(this).setNowPlayingThemeChanged(false);
            recreate();
        }
    }
}
