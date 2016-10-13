package com.example.kamranali.expandableview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kamranali on 07/10/2016.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, List<Double>> version_Category;
    private List<String> version_List;

    public ExpandableAdapter(Context context, HashMap<String, List<Double>> version_Category, List<String> version_List) {
        this.context = context;
        this.version_Category = version_Category;
        this.version_List = version_List;
    }

    @Override
    public int getGroupCount() {
        return version_List.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return version_Category.get(version_List.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return version_List.get(groupPosition);
    }

    @Override
    public Object getChild(int parent, int child) {
        return version_Category.get(version_List.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int parent) {
        return parent;
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {
        String group_title = (String) getGroup(parent);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_layout, parentView, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.parent_textView);
        parentTextView.setTypeface(null, Typeface.BOLD);
        parentTextView.setText(group_title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Double child_title = (Double) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, parent, false);

        }
        TextView childText = (TextView) convertView.findViewById(R.id.child_textView);
        childText.setText(String.valueOf(child_title));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
