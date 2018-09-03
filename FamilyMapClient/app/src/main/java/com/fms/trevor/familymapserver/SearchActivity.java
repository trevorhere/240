package com.fms.trevor.familymapserver;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.fms.trevor.familymapserver.model.Event;
import com.fms.trevor.familymapserver.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;


import java.util.ArrayList;
import java.util.HashMap;



//NOTES: Custom Adapter forms list for this Activity in conjunction with
// Adapter helper.


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    SearchView svSearch;
    ListView lvSearch;
    ListAdapter listAdapter;
    ArrayList<listItem> searchResults;
    HashMap<String, Event> eventsList;
    HashMap<String, Person> peopleList;
    public static Drawable MapMarkerIcon;
    public static Drawable MaleIcon;
    public static Drawable FemaleIcon;


    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

         MapMarkerIcon = new IconDrawable(this,
                 FontAwesomeIcons.fa_map_marker).sizeDp(40);

         MaleIcon = new IconDrawable(this,
                 FontAwesomeIcons.fa_male).sizeDp(40);

        FemaleIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_female).sizeDp(40);


        lvSearch = (ListView) findViewById(R.id.lvSearch);
        svSearch = (SearchView) findViewById(R.id.svSearch);

        searchResults = new ArrayList<>();

      // listAdapter = new ArrayAdapter<listItem>(this,  , searchResults);

        listAdapter = new CustomAdapter(this, searchResults);
        lvSearch.setAdapter(listAdapter);


         lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                  final String result = String.valueOf(parent.getItemAtPosition(position));

                  listItem li = searchResults.get(position);
                  String[] filter = li.title.split(" ");

                  if(filter.length > 2)
                  {
                      startMapActivity(getEventID(li.title));
                  }
                  else
                  {
                      startPersonActivity(getPersonID(li.title));
                  }

          //       Toast.makeText(getBaseContext() , result, Toast.LENGTH_LONG ).show();
                }
             });


         svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 runSearch(query);

                 new AdapterHelper().update((ArrayAdapter)listAdapter, new ArrayList<Object>(searchResults));
         //        listAdapter.notifyDataSetChanged();

                 return true;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 return false;
             }
         });

     }

    public void startMapActivity(String eventID)
    {

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("eventID", eventID);
        startActivity(intent);

    }
    public void startPersonActivity(String pID)
    {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("personID", pID);
        startActivity(intent);
    }
    private String getPersonID(String selected)
    {
        String[] description = selected.split(" ");
        String firstName = description[0];
        String lastName = description[1];

        for(String key : peopleList.keySet())
        {
            Person p = peopleList.get(key);

            if(p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
            {
                return p.getPersonID();
            }
        }
        Log.d(TAG, "getPersonID: " + "issue here");
        return "test";
    }


    private String getEventID(String selected)
    {
        String[] description = selected.split(" ");
        String eventType = description[0];

        for(String e : eventsList.keySet())
        {
            String eType = eventsList.get(e).getEventType().split(" ")[0];

            if(eType.equals(eventType))
            {
                return eventsList.get(e).getEventID();
            }
        }

        Log.d(TAG, "getEventID: " + "issue here");
        return "test";


    }

    public void runSearch(CharSequence query){

         searchResults.clear();

         String search = query.toString().toLowerCase();

         searchPeople(search);
         searchEvents(search);

//         Log.d(TAG, "runSearch: " + query);
//         Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG)
//                       .show();

     }

    private void searchPeople(String search) {
         ArrayList<Person> person = new ArrayList<>();
         peopleList = MainActivity.mMain.personsMap;

         for(String key : peopleList.keySet())
         {
             Person p = peopleList.get(key);

             if(p.getFirstName().toLowerCase().contains(search) ||
                p.getLastName().toLowerCase().contains(search))
             {
                 person.add(p);
             }
         }
        Drawable temp;

        for(Person e : person)
        {
            String description = e.getFirstName() +  " " + e.getLastName();

            if(e.getGender().equals("m"))
            {
                temp = MaleIcon;
            }
            else
            {
                temp = FemaleIcon;
            }

            listItem li = new listItem(temp,description);
            searchResults.add(li);
        }
    }

    private void searchEvents(String search) {

        ArrayList<Event> events = new ArrayList<>();
        eventsList = MainActivity.mMain.eventsMap;

        for(String key : eventsList.keySet())
        {
            Event e = eventsList.get(key);

            if(e.getCountry().toLowerCase().contains(search)   ||
               e.getCity().toLowerCase().contains(search)      ||
               e.getEventType().toLowerCase().contains(search) ||
               e.getYear().toLowerCase().contains(search))
            {
                if(e.isShowOnMap())
                {
                    events.add(e);

                }
            }
        }

        for(Event e: events)
        {
            Person person = MainActivity.mMain.getPerson(e.getPersonID());
            String description =
                    e.getEventType() +
                    " : " +
                    e.getCity() +
                    ", " +
                    e.getCountry() +
                            "( " +
                            e.getYear() +
                            " ) \n" +
                    person.getFirstName().toString() +
                    " " +
                    person.getLastName();


            listItem li = new listItem(MapMarkerIcon,description);
            searchResults.add(li);
        }
    }

    class listItem{

        public Drawable icon;
        public String title;

        public listItem(Drawable icon, String title) {
            super();
            this.icon = icon;
            this.title = title;
        }
    }

    public class AdapterHelper {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void update(ArrayAdapter arrayAdapter, ArrayList<Object> listOfObject){
            arrayAdapter.clear();
            for (Object object : listOfObject){
                arrayAdapter.add(object);
            }
        }
    }
}
