package com.skillslevel.joules.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.Config;
import com.afollestad.appthemeengine.prefs.ATECheckBoxPreference;
import com.afollestad.appthemeengine.prefs.ATEColorPreference;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.skillslevel.joules.R;
import com.skillslevel.joules.activities.SettingsActivity;
import com.skillslevel.joules.utils.Constants;
import com.skillslevel.joules.utils.NavigationUtils;
import com.skillslevel.joules.utils.PreferencesUtil;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String NOW_PLAYING_SELECTOR = "now_playing_selector";
    private static final String KEY_ABOUT = "preference_about";
    private static final String KEY_SOURCE = "preference_source";
    private static final String KEY_THEME = "theme_preference";
    private static final String TOGGLE_ANIMATIONS = "toggle_animations";
    private static final String TOGGLE_SYSTEM_ANIMATIONS = "toggle_system_animations";
    private static final String KEY_START_PAGE = "start_page_preference";
    Preference nowPlayingSelector;
    SwitchPreference toggleAnimations;
    ListPreference themePreference, startPagePreference;
    PreferencesUtil mPreferences;
    private String mAteKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        mPreferences = PreferencesUtil.getInstance(getActivity());

        nowPlayingSelector = findPreference(NOW_PLAYING_SELECTOR);
//        themePreference = (ListPreference) findPreference(KEY_THEME);
        startPagePreference = (ListPreference) findPreference(KEY_START_PAGE);

        nowPlayingSelector.setIntent(NavigationUtils.getNavigateToStyleSelectorIntent(getActivity(), Constants.SETTINGS_STYLE_SELECTOR_NOWPLAYING));

        PreferencesUtil.getInstance(getActivity()).setOnSharedPreferenceChangeListener(this);
        setPreferenceClickListeners();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
    }

    private void setPreferenceClickListeners() {

//        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                return true;
//            }
//        });

        startPagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                switch ((String) newValue) {
                    case "last_opened":
                        mPreferences.setLastOpenedAsStartPagePreference(true);
                        break;
                    case "songs":
                        mPreferences.setLastOpenedAsStartPagePreference(false);
                        mPreferences.setStartPageIndex(0);
                        break;
                    case "albums":
                        mPreferences.setLastOpenedAsStartPagePreference(false);
                        mPreferences.setStartPageIndex(1);
                        break;
                    case "artists":
                        mPreferences.setLastOpenedAsStartPagePreference(false);
                        mPreferences.setStartPageIndex(2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        invalidateSettings();
        ATE.postApply(this.getActivity(), mAteKey);
    }

    public void invalidateSettings() {
        mAteKey = ((SettingsActivity) getActivity()).getATEKey();

        ATEColorPreference primaryColorPref = (ATEColorPreference) findPreference("primary_color");
        primaryColorPref.setColor(Config.primaryColor(getActivity(), mAteKey), Color.BLACK);
        primaryColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.primary_color)
                        .preselect(Config.primaryColor(getActivity(), mAteKey))
                        .show();
                return true;
            }
        });

        ATEColorPreference accentColorPref = (ATEColorPreference) findPreference("accent_color");
        accentColorPref.setColor(Config.accentColor(getActivity(), mAteKey), Color.BLACK);
        accentColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.accent_color)
                        .preselect(Config.accentColor(getActivity(), mAteKey))
                        .show();
                return true;
            }
        });


        findPreference("dark_theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Marks both theme configs as changed so MainActivity restarts itself on return
                Config.markChanged(getActivity(), "light_theme");
                Config.markChanged(getActivity(), "dark_theme");
                // The dark_theme preference value gets saved by Android in the default PreferenceManager.
                // It's used in getATEKey() of both the Activities.
                getActivity().recreate();
                return true;
            }
        });

        final ATECheckBoxPreference statusBarPref = (ATECheckBoxPreference) findPreference("colored_status_bar");
        final ATECheckBoxPreference navBarPref = (ATECheckBoxPreference) findPreference("colored_nav_bar");

        statusBarPref.setChecked(Config.coloredStatusBar(getActivity(), mAteKey));
        statusBarPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ATE.config(getActivity(), mAteKey)
                        .coloredStatusBar((Boolean) newValue)
                        .apply(getActivity());
                return true;
            }
        });


        navBarPref.setChecked(Config.coloredNavigationBar(getActivity(), mAteKey));
        navBarPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ATE.config(getActivity(), mAteKey)
                        .coloredNavigationBar((Boolean) newValue)
                        .apply(getActivity());
                return true;
            }
        });

    }
}
