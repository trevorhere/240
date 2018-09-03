package com.fms.trevor.familymapserver;


import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import com.fms.trevor.familymapserver.model.Main;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static FragmentManager sFragmentManager;
    public boolean showMap = true;
    public static Main mMain;
    public static  Drawable searchIcon;
    public static  Drawable filterIcon;
    public static  Drawable settingsIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sFragmentManager = getSupportFragmentManager();
        Iconify.with(new FontAwesomeModule());
        mMain = new Main();

        searchIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_search).sizeDp(40);
        filterIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_filter).sizeDp(40);
        settingsIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_gear).sizeDp(40);

        if(savedInstanceState == null)
        {
            runLoginFragment();
        }
        else
        {
            if(savedInstanceState.containsKey("showMap"))
            {
                showMap = savedInstanceState.getBoolean("showMap");
                if(showMap)
                {
                    runMapFragment();
                }
                else
                {
                    runLoginFragment();
                }
            }


        }
    }

    public void runLoginFragment()
    {
       getSupportFragmentManager().beginTransaction().add(R.id.activity_main, new LoginFragment()).commit();
    }

    public void runMapFragment()
    {
        Log.d(TAG, "runMapFragment: ");

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromMain", true);

        Fragment frag = new MapFragment();
        frag.setArguments(bundle);

       getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, new MapFragment()).commit();

    }
}
