package com.example.kamranali.expandableviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ExpandableListView exp_ListView;
    private HashMap<String, List<String>> movies_Category;
    private List<String> moviesList;
    private ExpAdapter adapter;
    DisplayMetrics metrics = new DisplayMetrics();


    private int width = metrics.widthPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exp_ListView = (ExpandableListView) findViewById(R.id.exap_listView);
        movies_Category = DataProvider.getInfo();
        moviesList = new ArrayList<>(movies_Category.keySet());
        adapter = new ExpAdapter(this, movies_Category, moviesList);
        exp_ListView.setAdapter(adapter);

    }
}
