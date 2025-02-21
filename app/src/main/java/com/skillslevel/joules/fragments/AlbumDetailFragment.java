package com.skillslevel.joules.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Transition;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.appthemeengine.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.skillslevel.joules.MusicPlayer;
import com.skillslevel.joules.R;
import com.skillslevel.joules.adapters.AlbumSongsAdapter;
import com.skillslevel.joules.dataloaders.AlbumLoader;
import com.skillslevel.joules.dataloaders.AlbumSongLoader;
import com.skillslevel.joules.listeners.SimpleTransitionListener;
import com.skillslevel.joules.models.Album;
import com.skillslevel.joules.models.Song;
import com.skillslevel.joules.utils.ATEUtils;
import com.skillslevel.joules.utils.Constants;
import com.skillslevel.joules.utils.FabAnimationUtils;
import com.skillslevel.joules.utils.Helpers;
import com.skillslevel.joules.utils.JoulesUtil;
import com.skillslevel.joules.utils.NavigationUtils;
import com.skillslevel.joules.utils.PreferencesUtil;
import com.skillslevel.joules.utils.SortOrder;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.List;

public class AlbumDetailFragment extends Fragment {
    long albumID = -1;

    ImageView albumArt, artistArt;
    TextView albumTitle, albumDetails;

    RecyclerView recyclerView;
    AlbumSongsAdapter mAdapter;

    Toolbar toolbar;

    Album album;

    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    FloatingActionButton fab;

    private boolean loadFailed = false;

    private PreferencesUtil mPreferences;
    private Context context;
    private int primaryColor = -1;

    public static AlbumDetailFragment newInstance(long id, boolean useTransition, String transitionName) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ALBUM_ID, id);
        args.putBoolean("transition", useTransition);
        if (useTransition)
            args.putString("transition_name", transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumID = getArguments().getLong(Constants.ALBUM_ID);
        }
        context = getActivity();
        mPreferences = PreferencesUtil.getInstance(context);
    }

    @TargetApi(21)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(
                R.layout.fragment_album_detail, container, false);

        albumArt = (ImageView) rootView.findViewById(R.id.album_art);
        artistArt = (ImageView) rootView.findViewById(R.id.artist_art);
        albumTitle = (TextView) rootView.findViewById(R.id.album_title);
        albumDetails = (TextView) rootView.findViewById(R.id.album_details);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (getArguments().getBoolean("transition")) {
            albumArt.setTransitionName(getArguments().getString("transition_name"));
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar);
        recyclerView.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        album = AlbumLoader.getAlbum(getActivity(), albumID);

        setAlbumart();

        setUpEverything();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlbumSongsAdapter adapter = (AlbumSongsAdapter) recyclerView.getAdapter();
                        MusicPlayer.playAll(getActivity(), adapter.getSongIds(), 0, albumID, JoulesUtil.IdType.Album, true);
                        NavigationUtils.navigateToNowplaying(getActivity(), false);
                    }
                }, 150);
            }
        });

        return rootView;
    }

    private void setupToolbar() {

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(album.title);

    }

    private void setAlbumart() {
        ImageLoader.getInstance().displayImage(JoulesUtil.getAlbumArtUri(albumID).toString(), albumArt,
                new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnFail(R.drawable.ic_empty_music2)
                        .resetViewBeforeLoading(true)
                        .build(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        loadFailed = true;
                        MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(context)
                                .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
                                .setColor(JoulesUtil.getBlackWhiteColor(Config.accentColor(context, Helpers.getATEKey(context))));
                        ATEUtils.setFabBackgroundTint(fab, Config.accentColor(context, Helpers.getATEKey(context)));
                        fab.setImageDrawable(builder.build());

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        try {
                            new Palette.Builder(loadedImage).generate(new Palette.PaletteAsyncListener() {
                                                                          @Override
                                                                          public void onGenerated(Palette palette) {
                                                                              Palette.Swatch swatch = palette.getVibrantSwatch();
                                                                              if (swatch != null) {
                                                                                  primaryColor = swatch.getRgb();
                                                                                  collapsingToolbarLayout.setContentScrimColor(primaryColor);
                                                                                  if (getActivity() != null)
                                                                                      ATEUtils.setStatusBarColor(getActivity(), Helpers.getATEKey(getActivity()), primaryColor);
                                                                              } else {
                                                                                  Palette.Swatch swatchMuted = palette.getMutedSwatch();
                                                                                  if (swatchMuted != null) {
                                                                                      primaryColor = swatchMuted.getRgb();
                                                                                      collapsingToolbarLayout.setContentScrimColor(primaryColor);
                                                                                      if (getActivity() != null)
                                                                                          ATEUtils.setStatusBarColor(getActivity(), Helpers.getATEKey(getActivity()), primaryColor);
                                                                                  }
                                                                              }

                                                                              MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                                                                                      .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
                                                                                      .setSizeDp(30);
                                                                              if (primaryColor != -1) {
                                                                                  builder.setColor(JoulesUtil.getBlackWhiteColor(primaryColor));
                                                                                  ATEUtils.setFabBackgroundTint(fab, primaryColor);
                                                                                  fab.setImageDrawable(builder.build());
                                                                              } else {
                                                                                  if (context != null) {
                                                                                      ATEUtils.setFabBackgroundTint(fab, Config.accentColor(context, Helpers.getATEKey(context)));
                                                                                      builder.setColor(JoulesUtil.getBlackWhiteColor(Config.accentColor(context, Helpers.getATEKey(context))));
                                                                                      fab.setImageDrawable(builder.build());
                                                                                  }
                                                                              }
                                                                          }
                                                                      }

                            );
                        } catch (
                                Exception ignored
                                )

                        {

                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }

                }

        );
    }

    private void setAlbumDetails() {

        String songCount = JoulesUtil.makeLabel(getActivity(), R.plurals.Nsongs, album.songCount);

        String year = (album.year != 0) ? (" - " + String.valueOf(album.year)) : "";

        albumTitle.setText(album.title);
        albumDetails.setText(album.artistName + " - " + songCount + year);


    }

    private void setUpAlbumSongs() {

        List<Song> songList = AlbumSongLoader.getSongsForAlbum(getActivity(), albumID);
        mAdapter = new AlbumSongsAdapter(getActivity(), songList, albumID);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

    }

    private void setUpEverything() {
        setupToolbar();
        setAlbumDetails();
        setUpAlbumSongs();
    }

    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                List<Song> songList = AlbumSongLoader.getSongsForAlbum(getActivity(), albumID);
                mAdapter.updateDataSet(songList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.album_song_sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_az:
                mPreferences.setAlbumSongSortOrder(SortOrder.AlbumSongSortOrder.SONG_A_Z);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_za:
                mPreferences.setAlbumSongSortOrder(SortOrder.AlbumSongSortOrder.SONG_Z_A);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_year:
                mPreferences.setAlbumSongSortOrder(SortOrder.AlbumSongSortOrder.SONG_YEAR);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_duration:
                mPreferences.setAlbumSongSortOrder(SortOrder.AlbumSongSortOrder.SONG_DURATION);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_track_number:
                mPreferences.setAlbumSongSortOrder(SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST);
                reloadAdapter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        if (primaryColor != -1 && getActivity() != null) {
            collapsingToolbarLayout.setContentScrimColor(primaryColor);
            ATEUtils.setFabBackgroundTint(fab, primaryColor);
            String ateKey = Helpers.getATEKey(getActivity());
            ATEUtils.setStatusBarColor(getActivity(), ateKey, primaryColor);
        }

    }

    private class EnterTransitionListener extends SimpleTransitionListener {

        @TargetApi(21)
        public void onTransitionEnd(Transition paramTransition) {
            FabAnimationUtils.scaleIn(fab);
        }

        public void onTransitionStart(Transition paramTransition) {
            FabAnimationUtils.scaleOut(fab, 0, null);
        }

    }
}
