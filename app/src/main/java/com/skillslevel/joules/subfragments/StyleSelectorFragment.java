package com.skillslevel.joules.subfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skillslevel.joules.R;
import com.skillslevel.joules.utils.Constants;
import com.skillslevel.joules.widgets.MultiViewPager;


public class StyleSelectorFragment extends Fragment {
    public String ACTION = "action";
    FragmentStatePagerAdapter adapter;
    MultiViewPager pager;
    private SubStyleSelectorFragment selectorFragment;

    public static StyleSelectorFragment newInstance(String what) {
        StyleSelectorFragment fragment = new StyleSelectorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SETTINGS_STYLE_SELECTOR_WHAT, what);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ACTION = getArguments().getString(Constants.SETTINGS_STYLE_SELECTOR_WHAT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_style_selector, container, false);

        if (ACTION.equals(Constants.SETTINGS_STYLE_SELECTOR_NOWPLAYING)) {

        }
        pager = (MultiViewPager) rootView.findViewById(R.id.pager);

        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Fragment getItem(int position) {
                selectorFragment = SubStyleSelectorFragment.newInstance(position, ACTION);
                return selectorFragment;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        pager.setAdapter(adapter);

        return rootView;
    }

    public void updateCurrentStyle() {
        if (selectorFragment != null)
            adapter.notifyDataSetChanged();

    }

    public void scrollToCurrentStyle(int page) {
        pager.setCurrentItem(page);
    }
}
