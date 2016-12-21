package com.example.kamranali.recyclerviewtask;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kamranali on 10/10/2016.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList countries;

    public DataAdapter(ArrayList countries) {
        this.countries = countries;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_country.setText(String.valueOf(countries.get(i)));

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void addItem(String country) {
        countries.add(country);
        notifyItemInserted(countries.size());
    }

    public void removeItem(int position) {
        countries.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, countries.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_country, undo_text, undo_button;
        LinearLayout layoutUndo;

        public ViewHolder(View view) {
            super(view);
            layoutUndo = (LinearLayout) view.findViewById(R.id.undo);
            undo_button = (TextView) view.findViewById(R.id.undo_button);
            undo_text = (TextView) view.findViewById(R.id.undo_text);
            tv_country = (TextView) view.findViewById(R.id.tv_country);
        }
    }

}