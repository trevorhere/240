package com.fms.trevor.familymapserver;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

// NOTES: Supports map after Event Click from Person Activity
public class MapActivity extends AppCompatActivity {

    String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(savedInstanceState == null)
        {
            Intent intent = getIntent();
            if(intent.hasExtra("eventID"))
            {
                this.eventID = intent.getStringExtra("eventID");
            }
        }
        else
        {
            this.eventID = savedInstanceState.getString("eventID");
        }

        runMapFragment(eventID);
    }

    public void runMapFragment(String eventID)
    {
        Bundle bundle = new Bundle();
        bundle.putString("eventID", eventID);

        Fragment frag = new MapFragment();
        frag.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_map, frag).commit();

    }
}
