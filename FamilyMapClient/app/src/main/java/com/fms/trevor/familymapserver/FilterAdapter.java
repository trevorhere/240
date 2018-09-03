package com.fms.trevor.familymapserver;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;


//NOTES: Adapter for filter list
public class FilterAdapter extends ArrayAdapter<FilterActivity.FilterItem> {


    public FilterAdapter(Context context, ArrayList<FilterActivity.FilterItem>
            searchResults) {
        super(context, 0, searchResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterActivity.FilterItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filter_item, parent, false);
        }


        TextView subTitle = (TextView) convertView.findViewById(R.id.tvFilterSub);
        Switch   filterSwitch = (Switch)  convertView.findViewById(R.id.sFilter);


        subTitle.setText(item.subTitle);
        filterSwitch.setText(item.title);

        if(checked(item.title))
        {
            filterSwitch.setChecked(true);
        }
        else
        {
            filterSwitch.setChecked(false);
        }

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               MainActivity.mMain.filterEvents(formatString(buttonView.getText().toString()), isChecked);

            }
        });

        return convertView;
    }

    private boolean checked(String title)
    {
        String fin = formatString(title);
        return (MainActivity.mMain.checkFilter(fin));
    }

    public String formatString(String event)
    {
        String fin = "";
        String[]  e = event.split(" ");
        for(int i = 0; i < e.length -1; i++)
        {
            fin += e[i];
            if(e.length > 2 && i != e.length - 2) {
                fin += " ";
            }
        }
        return fin;
    }
}



