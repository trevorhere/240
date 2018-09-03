package com.fms.trevor.familymapserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;


public class FilterActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    ArrayList<FilterItem> filterList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterList = new ArrayList<>();

        generateFilterItems();

        listView = (ListView) findViewById(R.id.lvFilters);
        listAdapter = new FilterAdapter(this, filterList);
        listView.setAdapter(listAdapter);
    }



    private void generateFilterItems() {


        //Dynamically generate filter items based on event Types
        for (String eventType : MainActivity.mMain.eventTypes) {
            FilterItem fi = new FilterItem();
            // String eTypeCaps = formatString(eventType);
            fi.title = (eventType.toUpperCase() + " EVENTS");
            fi.subTitle = ("FILTER BY " + eventType.toUpperCase() + " EVENTS");
            filterList.add(fi);
        }

        FilterItem Mom = new FilterItem();
        // String eTypeCaps = formatString(eventType);
        Mom.title = ("MOTHERS SIDE");
        Mom.subTitle = ("FILTER BY MOTHERS SIDE OF FAMILY");
        filterList.add(Mom);

        FilterItem Dad = new FilterItem();
        // String eTypeCaps = formatString(eventType);
        Dad.title = ("FATHERS SIDE");
        Dad.subTitle = ("FILTER BY FATHERS SIDE OF FAMILY");
        filterList.add(Dad);

        FilterItem Male = new FilterItem();
        // String eTypeCaps = formatString(eventType);
        Male.title = ("MALE EVENTS");
        Male.subTitle = ("FILTER BY EVENTS BASED ON GENDER");
        filterList.add(Male);

        FilterItem Female = new FilterItem();
        // String eTypeCaps = formatString(eventType);
        Female.title = ("FEMALE EVENTS");
        Female.subTitle = ("FILTER BY EVENTS BASED ON GENDER");
        filterList.add(Female);

    }

    // Filter List Item. Switch is added in the adapter (Filter Adapter)
    public class FilterItem
    {
        String title;
        String subTitle;
    }
}
