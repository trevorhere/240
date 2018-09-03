package com.fms.trevor.familymapserver;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fms.trevor.familymapserver.model.Event;
import com.fms.trevor.familymapserver.model.Main;
import com.fms.trevor.familymapserver.model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;


import java.util.ArrayList;


//NOTES: Leave comments when editing this file, it breaks easily
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = "MapFragment";

    static GoogleMap mMap;
    public static  View mView;
    public static  TextView mEventText;
    public static String personID;
    public static  ImageView ivGender;
    static ArrayList<String> eventTypes;
    static Drawable femaleIcon;
    static Drawable maleIcon;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = (RelativeLayout) inflater.inflate(R.layout.map_fragment, container ,false);
        mEventText = (TextView)   mView.findViewById(R.id.event_text);
        ivGender = (ImageView) mView.findViewById(R.id.ivGender);

        ivGender.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                startPersonActivity();
            }
        });
        mEventText.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                startPersonActivity();
            }
        });

        femaleIcon = new IconDrawable(getActivity(),
                FontAwesomeIcons.fa_female).sizeDp(40);

        maleIcon = new IconDrawable(getActivity(),
                FontAwesomeIcons.fa_male).sizeDp(40);


        return mView;
    }

    public void startPersonActivity()
    {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("personID", personID);
        startActivity(intent);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addEventMarkers();
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            String eventID = bundle.getString("eventID");
            displayEventInfo(eventID);
            Event e = MainActivity.mMain.getEvent(eventID);
            Double Lat = Double.parseDouble(e.getLat());
            Double Lng = Double.parseDouble(e.getLng());
            LatLng position = new LatLng(Lat, Lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            setHasOptionsMenu(false);


        }

        addMarkerClick();


        //TEST DATA
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        mMap.addMarker(new MarkerOptions()
//          .position(sydney)
//          .snippet("nice")
//           .title("marker"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    public static void addMarkerClick()
    {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String eventID = marker.getSnippet();
                displayEventInfo(eventID);

                return false;
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            Boolean showMenu = bundle.getBoolean("fromMain");
            if(showMenu)
            {
                setHasOptionsMenu(true);

            }
        }



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem filter = menu.findItem(R.id.filter);
        MenuItem settings = menu.findItem(R.id.settings);

        search.setIcon(MainActivity.searchIcon);
        filter.setIcon(MainActivity.filterIcon);
        settings.setIcon(MainActivity.settingsIcon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch(id)
        {
            case R.id.search :
            {
                intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.filter :
            {
                intent = new Intent(getActivity().getApplicationContext(), FilterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.settings :
            {
                intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public static void displayEventInfo(String eventID)
    {
        Event event = Main.getEvent(eventID);
        personID = event.getPersonID();
        Person person = Main.getPerson(personID);


        String event_Text = person.getFirstName() +
                " " +
                person.getLastName() +
                "\n" +
                event.getEventType() +
                ": " +
                event.getCity() +

                ", " +
                event.getCountry() +
                "( " +
                event.getYear() +
                " )"
                ;
        mEventText.setText(event_Text);
        String gender =  person.getGender().toLowerCase();
        if(gender.equals("f"))
        {
            ivGender.setImageDrawable(femaleIcon);
        }
        else if(gender.equals("m"))
        {
            ivGender.setImageDrawable(maleIcon);
        }
    }

    public static void addMapMarkers(ArrayList<MarkerOptions> addresses)
    {

       for(MarkerOptions option: addresses)
       {
           mMap.addMarker(option);
       }

    }

    public static void addEventMarkers()
    {

        eventTypes = new ArrayList<>();

                ArrayList<MarkerOptions> optionsArray = new ArrayList<>();

        for(String key : MainActivity.mMain.eventsMap.keySet())
        {
            Event e = new Event();

            e = (MainActivity.mMain.eventsMap.get(key));

        float color = getMarkerColor(e.getEventType());


        if(color < 0 || color > 360)
        {
            color = 5;
        }

        if(e.isShowOnMap())
        {
            double lat  = Double.parseDouble( e.getLat());
            double lng  = Double.parseDouble( e.getLng());
            String title = e.getEventType();
            String id =  e.getEventID();

            MarkerOptions option = new MarkerOptions();
            LatLng marker = new LatLng(lat, lng);
            option.position(marker);
            option.title(title);
            option.snippet(id);
            option.icon(BitmapDescriptorFactory.defaultMarker(color));

            optionsArray.add(option);

           }
        }

        addMapMarkers(optionsArray);
    }


    private static float getMarkerColor(String eventType) {

        boolean match = false;
        float result = 9000;

        if(eventTypes.size() >= 360)
        {
            return 1;
        }
        for(int i = 0; i < eventTypes.size(); i++)
        {
            if(eventTypes.get(i).equals(eventType))
            {
                match = true;
                result =  i;
            }
        }

        if(!match)
        {
            eventTypes.add(eventType);
            result =  eventTypes.size() - 1;
        }

        if(eventTypes.size() < 30) result *= 5;

        return result;
    }

    public static void reSync() {
        mMap.clear();
        addEventMarkers();
        addMarkerClick();
    }
}
