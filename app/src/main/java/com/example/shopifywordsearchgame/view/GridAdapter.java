package com.example.shopifywordsearchgame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.shopifywordsearchgame.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private char[][] chars;
    LayoutInflater inflater;

    public GridAdapter(Context context, char[][] chars) {
        this.context = context;
        this.chars = chars;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chars.length * chars[0].length;
    }

    @Override
    public Object getItem(int i) {
        int row = i / chars[0].length;
        int col = i % chars[0].length;
        return chars[row][col];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null)
            view = inflater.inflate(R.layout.grid_cell, null);
        int size = ((GridView) parent).getColumnWidth();

        TextView textView = view.findViewById(R.id.grid_cell_item);
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(size, size));
        textView.setText(Character.toString((char) getItem(i)));

        return view;
    }
}