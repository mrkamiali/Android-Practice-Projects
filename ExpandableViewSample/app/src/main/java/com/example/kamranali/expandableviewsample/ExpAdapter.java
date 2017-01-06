package com.example.kamranali.expandableviewsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kamranali on 21/12/2016.
 */

public class ExpAdapter extends BaseExpandableListAdapter {
    private HashMap<String, List<String>> moviesCategoryMap;
    private List<String> moviesList;
    private Context context;

    public ExpAdapter(Context context, HashMap<String, List<String>> moviesCategory, List<String> moviesList) {
        this.context = context;
        this.moviesCategoryMap = moviesCategory;
        this.moviesList = moviesList;
    }

    public ExpAdapter() {
    }

    @Override
    public int getGroupCount() {
        return moviesCategoryMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return moviesCategoryMap.get(moviesList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return moviesList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return moviesCategoryMap.get(moviesList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {
        String groupTitle = (String) getGroup(parent);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_layout, parentView, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.parent_textView);
        parentTextView.setText(groupTitle);

        return convertView;

    }

    @Override
    public View getChildView(int parent, int child, boolean isLastChild, View convertView, ViewGroup parentView) {
        String childTitle = (String) getChild(parent, child);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, parentView, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.childTextView);
        parentTextView.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
