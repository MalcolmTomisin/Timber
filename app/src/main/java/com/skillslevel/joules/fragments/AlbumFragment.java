package com.skillslevel.joules.fragments;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.skillslevel.joules.R;
import com.skillslevel.joules.adapters.AlbumAdapter;
import com.skillslevel.joules.dataloaders.AlbumLoader;
import com.skillslevel.joules.models.Album;
import com.skillslevel.joules.utils.PreferencesUtil;
import com.skillslevel.joules.utils.SortOrder;
import com.skillslevel.joules.widgets.FastScroller;

import java.util.List;

public class AlbumFragment extends Fragment {
    private AlbumAdapter mAdapter;
    private RecyclerView recyclerView;
    private FastScroller fastScroller;
    private GridLayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private PreferencesUtil mPreferences;
    private boolean isGrid;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtil.getInstance(getActivity());
        isGrid = mPreferences.isAlbumsInGrid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_recyclerview, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        fastScroller = (FastScroller) rootView.findViewById(R.id.fastscroller);

        setLayoutManager();

        if (getActivity() != null)
            new loadAlbums().execute("");
        return rootView;
    }

    private void setLayoutManager() {
        if (isGrid) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
            fastScroller.setVisibility(View.GONE);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 1);
            fastScroller.setVisibility(View.VISIBLE);
            fastScroller.setRecyclerView(recyclerView);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setItemDecoration() {
        if (isGrid) {
            int spacingInPixels = getActivity().getResources().getDimensionPixelSize(R.dimen.spacing_card_album_grid);
            itemDecoration = new SpacesItemDecoration(spacingInPixels);
        } else {
            itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        }
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void updateLayoutManager(int column) {
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.setAdapter(new AlbumAdapter(getActivity(), AlbumLoader.getAllAlbums(getActivity())));
        layoutManager.setSpanCount(column);
        layoutManager.requestLayout();
        setItemDecoration();
    }

    private void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                List<Album> albumList = AlbumLoader.getAllAlbums(getActivity());
                mAdapter.updateDataSet(albumList);
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
        inflater.inflate(R.menu.album_sort_by, menu);
        inflater.inflate(R.menu.menu_show_as, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_az:
                mPreferences.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_A_Z);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_za:
                mPreferences.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_Z_A);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_year:
                mPreferences.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_YEAR);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_artist:
                mPreferences.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_ARTIST);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_number_of_songs:
                mPreferences.setAlbumSortOrder(SortOrder.AlbumSortOrder.ALBUM_NUMBER_OF_SONGS);
                reloadAdapter();
                return true;
            case R.id.menu_show_as_list:
                mPreferences.setAlbumsInGrid(false);
                isGrid = false;
                updateLayoutManager(1);
                return true;
            case R.id.menu_show_as_grid:
                mPreferences.setAlbumsInGrid(true);
                isGrid = true;
                updateLayoutManager(2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {


            outRect.left = space;
            outRect.top = space;
            outRect.right = space;
            outRect.bottom = space;

        }
    }

    private class loadAlbums extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null)
                mAdapter = new AlbumAdapter(getActivity(), AlbumLoader.getAllAlbums(getActivity()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            recyclerView.setAdapter(mAdapter);
            //to add spacing between cards
            if (getActivity() != null) {
                setItemDecoration();
            }

        }

        @Override
        protected void onPreExecute() {
        }
    }
}
