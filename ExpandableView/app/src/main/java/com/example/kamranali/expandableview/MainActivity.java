package com.example.kamranali.expandableview;

import android.app.ExpandableListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandableAdapter adapter;
    HashMap<String, List<Double>> version_category;
    List<String> version_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView = (ExpandableListView) findViewById(R.id.expandaleView);

        version_category = Version_List.getInfo();
        version_list = new ArrayList<>(version_category.keySet());

        adapter = new ExpandableAdapter(this, version_category, version_list);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                
                Toast.makeText(MainActivity.this, "Child clicked " + childPosition, Toast.LENGTH_SHORT).show();

                return false;
            }
        });

    }
}
