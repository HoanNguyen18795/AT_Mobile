package com.testing.hoan.at_mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hoan on 12/2/2016.
 */
public class TabFragment extends Fragment {
    public static TabLayout mTabLayout;
    public static ViewPager mViewPager;
    public static int items_count=2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=(View) inflater.inflate(R.layout.tab_layout,null);
        mTabLayout=(TabLayout) root.findViewById(R.id.tabs);
        mViewPager=(ViewPager) root.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setupWithViewPager(mViewPager);
            }
        });
        return root;
    }
    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new RecyclerViewFrag();
                case 1: return new HotNewsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return items_count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "News";
                case 1: return "Hot News";
            }
            return null;
        }
    }
}
