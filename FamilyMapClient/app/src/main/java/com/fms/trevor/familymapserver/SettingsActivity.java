package com.fms.trevor.familymapserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fms.trevor.familymapserver.model.Main;

import org.w3c.dom.Text;

import java.net.URL;


// NOTES: Only incomplete part of the app besides map event lines,
//  to do:
//  build spinners
//  build map selection functionality
//  log out works part way, need to test it still
//  resync functionality


public class SettingsActivity extends AppCompatActivity {

    Switch lifeLines;
    Switch familyTreeLines;
    Switch spouseLines;

    TextView mapType;
    TextView  reData;
    TextView logout;

    Spinner lifeLinesSpinner;
    Spinner familyTreeLinesSpinner;
    Spinner spouseLinesSpinner;
    Spinner mapTypeSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");



        logout = (TextView) findViewById(R.id.logout);
        reData = (TextView) findViewById(R.id.reData);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mMain.clear();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        reData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSync re = new ReSync();
                re.execute();
            }
        });

    }

    class ReSync extends AsyncTask<URL, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(URL... urls) {
            return MainActivity.mMain.reSync();
        }


        @Override
        protected void onPostExecute(Boolean b) {
            if(b)
            {
                onSyncGood();
            }
            else
            {
                onSyncBad();
            }
        }
    }


    public void onSyncGood()
    {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("showMap", true);
        startActivity(intent);

    }
    public void onSyncBad()
    {
        Toast.makeText(getBaseContext(), "ReSync failed", Toast.LENGTH_SHORT);
    }


}
