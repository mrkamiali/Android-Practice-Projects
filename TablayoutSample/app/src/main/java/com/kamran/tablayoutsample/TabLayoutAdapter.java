package com.kamran.tablayoutsample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Kamran ALi on 2/25/2017.
 */
public class TabLayoutAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentArraylist;
    private Context context;

    public TabLayoutAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArraylist, Context context) {
        super(fm);
        this.fragmentArraylist = fragmentArraylist;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArraylist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArraylist.size();
    }
}
