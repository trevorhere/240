package com.fms.trevor.familymapserver;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;




// NOTES: This file is an adapter for Search Activity List.
public class CustomAdapter extends ArrayAdapter<SearchActivity.listItem> {
    public CustomAdapter(Context context, ArrayList<SearchActivity.listItem> searchResults) {
        super(context, 0, searchResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchActivity.listItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        TextView text = (TextView) convertView.findViewById(R.id.elItem);
        ImageView icon = (ImageView) convertView.findViewById(R.id.ivIcon);


        text.setText(item.title);
        icon.setImageDrawable(item.icon);
        return convertView;
    }
}



