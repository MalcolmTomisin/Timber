package com.skillslevel.joules.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.Config;
import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.skillslevel.joules.R;
import com.skillslevel.joules.fragments.SettingsFragment;
import com.skillslevel.joules.subfragments.StyleSelectorFragment;
import com.skillslevel.joules.utils.Constants;
import com.skillslevel.joules.utils.PreferencesUtil;

public class SettingsActivity extends BaseThemedActivity implements ColorChooserDialog.ColorCallback, ATEActivityThemeCustomizer {
    String action;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (PreferencesUtil.getInstance(this).getTheme().equals("dark"))
            setTheme(R.style.AppThemeNormalDark);
        else if (PreferencesUtil.getInstance(this).getTheme().equals("black"))
            setTheme(R.style.AppThemeNormalBlack);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        action = getIntent().getAction();

        if (action.equals(Constants.SETTINGS_STYLE_SELECTOR)) {
            getSupportActionBar().setTitle(R.string.now_playing);
            String what = getIntent().getExtras().getString(Constants.SETTINGS_STYLE_SELECTOR_WHAT);
            Fragment fragment = StyleSelectorFragment.newInstance(what);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
       /* #// TODO: 1/16/2019  */
        else {
            getSupportActionBar().setTitle(R.string.settings);
            PreferenceFragment fragment = new SettingsFragment();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @StyleRes
    @Override
    public int getActivityTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark : R.style.AppThemeLight;
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        final Config config = ATE.config(this, getATEKey());
        switch (dialog.getTitle()) {
            case R.string.primary_color:
                config.primaryColor(selectedColor);
                break;
            case R.string.accent_color:
                config.accentColor(selectedColor);
                break;
        }
        config.commit();
        recreate(); // recreation needed to reach the checkboxes in the preferences layout
    }
}
