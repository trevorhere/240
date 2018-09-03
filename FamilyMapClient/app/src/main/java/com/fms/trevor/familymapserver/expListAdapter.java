package com.fms.trevor.familymapserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;


// NOTES: This file is an adapter for Person Activity Expandable List.

public class expListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    public expListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap)
    {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }
            TextView elHeader = (TextView) view.findViewById(R.id.elHeader);
            elHeader.setText(headerTitle);
        return view;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition, childPosition);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        ImageView ivItem = (ImageView)view.findViewById(R.id.ivIcon);
        TextView elItem = (TextView)view.findViewById(R.id.elItem);

        if(groupPosition == 1)
        {
            if(PersonActivity.femalePos != null)
            {
                if(PersonActivity.femalePos.contains(childPosition))
                {
                    ivItem.setImageDrawable(PersonActivity.FemaleIcon);
                }
                else
                {
                    ivItem.setImageDrawable(PersonActivity.MaleIcon);
                }
            }
            else
            {
                ivItem.setImageDrawable(PersonActivity.MaleIcon);
            }

        }
        else
        {
            ivItem.setImageDrawable(PersonActivity.MapMarkerIcon);
        }

        elItem.setText(childText);
        return view;
    }

    @Override
    public Object getChild(int groupItem, int childItem) {
        return listHashMap.get(listDataHeader.get(groupItem)).get(childItem);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }




}
