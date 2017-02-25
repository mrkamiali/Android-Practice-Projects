package com.kamran.tablayoutsample;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kamran.tablayoutsample.fragments.FirstFragment;
import com.kamran.tablayoutsample.fragments.SecondFragment;
import com.kamran.tablayoutsample.fragments.ThirdFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragmentsArrayList;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayoutAdapter tabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager_View);
        fragmentsArrayList = new ArrayList<>();

        //named to tabs
        tabLayout.addTab(tabLayout.newTab().setText("TAb1"));
        tabLayout.addTab(tabLayout.newTab().setText("TAb2"));
        tabLayout.addTab(tabLayout.newTab().setText("TAb3"));

        //AddingFargments to arraylist
        fragmentsArrayList.add(new FirstFragment());
        fragmentsArrayList.add(new SecondFragment());
        fragmentsArrayList.add(new ThirdFragment());

        //initilizingTabAdapter
        tabAdapter = new TabLayoutAdapter(this.getSupportFragmentManager(), fragmentsArrayList, MainActivity.this);
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }
}
