package com.skillslevel.joules.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.skillslevel.joules.activities.MainActivity;
import com.skillslevel.joules.R;
import com.skillslevel.joules.activities.NowPlayingActivity;
import com.skillslevel.joules.activities.PlaylistDetailActivity;
import com.skillslevel.joules.activities.SearchActivity;
import com.skillslevel.joules.activities.SettingsActivity;
import com.skillslevel.joules.fragments.AlbumDetailFragment;
import com.skillslevel.joules.fragments.ArtistDetailFragment;
import com.skillslevel.joules.nowplaying.Joules1;
import com.skillslevel.joules.nowplaying.Joules2;
import com.skillslevel.joules.nowplaying.Joules3;
import com.skillslevel.joules.nowplaying.Joules4;

import java.util.ArrayList;

public class NavigationUtils {
    @TargetApi(21)
    public static void navigateToAlbum(Activity context, long albumID, Pair<View, String> transitionViews) {

        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (JoulesUtil.isLollipop() && transitionViews != null && PreferencesUtil.getInstance(context).getAnimations()) {
            Transition changeImage = TransitionInflater.from(context).inflateTransition(R.transition.image_transform);
            transaction.addSharedElement(transitionViews.first, transitionViews.second);
            fragment = AlbumDetailFragment.newInstance(albumID, true, transitionViews.second);
            fragment.setSharedElementEnterTransition(changeImage);
        } else {
            transaction.setCustomAnimations(R.anim.activity_fade_in,
                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = AlbumDetailFragment.newInstance(albumID, false, null);
        }
        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null).commit();

    }

    @TargetApi(21)
    public static void navigateToArtist(Activity context, long artistID, Pair<View, String> transitionViews) {

        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (JoulesUtil.isLollipop() && transitionViews != null && PreferencesUtil.getInstance(context).getAnimations()) {
            Transition changeImage = TransitionInflater.from(context).inflateTransition(R.transition.image_transform);
            transaction.addSharedElement(transitionViews.first, transitionViews.second);
            fragment = ArtistDetailFragment.newInstance(artistID, true, transitionViews.second);
            fragment.setSharedElementEnterTransition(changeImage);
        } else {
            transaction.setCustomAnimations(R.anim.activity_fade_in,
                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            fragment = ArtistDetailFragment.newInstance(artistID, false, null);
        }
        transaction.hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null).commit();

    }

    public static void goToArtist(Context context, long artistId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_ARTIST);
        intent.putExtra(Constants.ARTIST_ID, artistId);
        context.startActivity(intent);
    }

    public static void goToAlbum(Context context, long albumId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_ALBUM);
        intent.putExtra(Constants.ALBUM_ID, albumId);
        context.startActivity(intent);
    }

    public static void navigateToNowplaying(Activity context, boolean withAnimations) {

        final Intent intent = new Intent(context, NowPlayingActivity.class);
        if (!PreferencesUtil.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        context.startActivity(intent);
    }

    public static Intent getNowPlayingIntent(Context context) {

        final Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_NOWPLAYING);
        return intent;
    }

    public static void navigateToSettings(Activity context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        if (!PreferencesUtil.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.NAVIGATE_SETTINGS);
        context.startActivity(intent);
    }

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(Constants.NAVIGATE_SEARCH);
        context.startActivity(intent);
    }


    @TargetApi(21)
    public static void navigateToPlaylistDetail(Activity context, String action, long firstAlbumID, String playlistName, int foregroundcolor, long playlistID, ArrayList<Pair> transitionViews) {
        final Intent intent = new Intent(context, PlaylistDetailActivity.class);
        if (!PreferencesUtil.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(action);
        intent.putExtra(Constants.PLAYLIST_ID, playlistID);
        intent.putExtra(Constants.PLAYLIST_FOREGROUND_COLOR, foregroundcolor);
        intent.putExtra(Constants.ALBUM_ID, firstAlbumID);
        intent.putExtra(Constants.PLAYLIST_NAME, playlistName);

        if (JoulesUtil.isLollipop() && PreferencesUtil.getInstance(context).getAnimations()) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.getInstance(), transitionViews.get(0), transitionViews.get(1), transitionViews.get(2));
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    public static void navigateToEqualizer(Activity context) {
        try {
            // The google MusicFX apps need to be started using startActivityForResult
            context.startActivityForResult(JoulesUtil.createEffectsIntent(), 666);
        } catch (final ActivityNotFoundException notFound) {
            Toast.makeText(context, "Equalizer not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent getNavigateToStyleSelectorIntent(Activity context, String what) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        if (!PreferencesUtil.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.SETTINGS_STYLE_SELECTOR);
        intent.putExtra(Constants.SETTINGS_STYLE_SELECTOR_WHAT, what);
        return intent;
    }

    public static Fragment getFragmentForNowplayingID(String fragmentID) {
        switch (fragmentID) {
            case Constants.JOULES1:
                return new Joules1();
            case Constants.JOULES2:
                return new Joules2();
            case Constants.JOULES3:
                return new Joules3();
            case Constants.JOULES4:
                return new Joules4();
            default:
                return new Joules1();
        }

    }
}
