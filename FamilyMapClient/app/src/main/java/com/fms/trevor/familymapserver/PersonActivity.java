package com.fms.trevor.familymapserver;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fms.trevor.familymapserver.model.Event;
import com.fms.trevor.familymapserver.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// NOTES: expListAdapter forms list for this activity
public class PersonActivity extends AppCompatActivity {
    private static final String TAG = "PersonActivity";

    private ExpandableListView listView;
    private expListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private String personID;
    private Person person;
    TextView tvFirstName;
    TextView tvLastName;
    TextView tvGender;
    ArrayList<Person> famArray;
    ArrayList<Event> events;
    public static Drawable MapMarkerIcon;
    public static Drawable MaleIcon;
    public static Drawable FemaleIcon;
    public static List<Integer> femalePos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        if(savedInstanceState == null)
        {
            Intent intent = getIntent();
            if(intent.hasExtra("personID"))
            {
                this.personID = intent.getStringExtra("personID");
            }
        }
        else
        {
            this.personID = savedInstanceState.getString("personID");
        }

        MapMarkerIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_map_marker).sizeDp(40);

        MaleIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_male).sizeDp(40);

        FemaleIcon = new IconDrawable(this,
                FontAwesomeIcons.fa_female).sizeDp(40);

        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvGender = (TextView) findViewById(R.id.tvGender);

        initData();

        listView = (ExpandableListView)findViewById(R.id.eListView);
        listAdapter = new expListAdapter(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);


        tvFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSearchActivity();
         //          startFilterActivity();
            }
        });


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) listAdapter.getChild( groupPosition, childPosition);

                if(groupPosition == 0)
                {
                    startMapActivity(getEventID(selected));
                }
                else
                {
                     startPersonActivity(getPersonID(selected));
                }

//                Log.d(TAG, "onChildClick: " + selected );
//                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
//                        .show();

                return true;
            }
        });

    }

    private String getEventID(String selected)
    {
        String[] description = selected.split(" ");
        String eventType = description[0];

        for(Event e : events)
        {
            String eType = e.getEventType().split(" ")[0];

            if(eType.equals(eventType))
            {
                return e.getEventID();
            }
        }

        Log.d(TAG, "getEventID: " + "issue here");
        return "test";


    }

    public void startSearchActivity()
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
    public void startFilterActivity()
    {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
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

        for(Person p : famArray)
        {
            if(p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
            {
                return p.getPersonID();
            }
        }
        Log.d(TAG, "getPersonID: " + "issue here");
        return "test";
    }

//    public void startPersonActivity(String personID)
//    {
//        Intent intent = new Intent(getActivity(), PersonActivity.class);
//        intent.putExtra("personID", personID);
//        startActivity(intent);
//    }

    private void initData()
    {

        this.person = MainActivity.mMain.getPerson(this.personID);

        tvFirstName.setText(person.getFirstName());
        tvLastName.setText(person.getLastName());

        Log.d(TAG, "initData: " + person.getGender());

        if(person.getGender().toLowerCase().equals("m"))
        {
            tvGender.setText("Male");
        }
        else if(person.getGender().toLowerCase().equals("f"))
        {
            tvGender.setText("Female");
        }
        else
        {
            tvGender.setText("Unidentified");
        }


        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        listDataHeader.add("LIFE EVENTS");
        listDataHeader.add("FAMILY");

        List<String>events = eventStringBuilder(MainActivity.mMain.getEventsFromPersonID(personID));
        List<String>family = familyStringBuilder(MainActivity.mMain.getFamilyFromPersonID(personID));



        listHashMap.put(listDataHeader.get(0), events);
        listHashMap.put(listDataHeader.get(1), family);



    }

    private List<String> familyStringBuilder(ArrayList<Person> familyArray) {


        famArray = familyArray;
        Person person = MainActivity.mMain.getPerson(personID);

        femalePos = new ArrayList<>();
        int counter = -1;
        List<String> familyDescription = new ArrayList<>();
        for(Person p : familyArray)
        {
            if(p.getGender().toLowerCase().equals("f"))
            {
                counter++;
                if(counter != -1 ) femalePos.add(counter);

            }

            String description = p.getFirstName() + " " + p.getLastName();
            if(p.getPersonID().equals(person.getMother()))
            {
                description += " \n Mother ";
            }
            else if (p.getPersonID().equals(person.getFather()))
            {
                description += " \n Father";
            }
            else if (p.getPersonID().equals(person.getSpouse()))
            {
                description += " \n Spouse";
            }
            else
            {
                description += " \n Child";
            }

            familyDescription.add(description);
        }
        return familyDescription;
    }

    private List<String> eventStringBuilder(ArrayList<Event> eventsArray) {

        events = eventsArray;

        String Death = null;
        String Birth = null;
        boolean birth = false;
        boolean death = false;


        ArrayList<Event> toBeSorted = new ArrayList<>();
        List<String> yearlessEvents = new ArrayList<>();
        List<String> eventDescription = new ArrayList<>();
        for(Event e: eventsArray)
        {
            if(e.getEventType().equals("Death"))
            {
                death = true;
                Death = e.getEventType() +
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
            }
            else if(e.getEventType().equals("Birth"))
            {
                birth = true;
                Birth = e.getEventType() +
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

            }
            else if(e.getYear() == null || e.getYear().length() < 1)
            {

                yearlessEvents.add(e.getEventType() +
                        " : " +
                        e.getCity() +
                        ", " +
                        e.getCountry() +
                        " \n" +
                        person.getFirstName().toString() +
                        " " +
                        person.getLastName()
                );
            }
            else
            {
                toBeSorted.add(e);
            }
        }
        eventDescription.addAll(sortArraysByYeay(toBeSorted));
        eventDescription.addAll(yearlessEvents);
        if(birth) eventDescription.add(0, Birth);
        if(death) eventDescription.add(eventDescription.size(), Death);

        return  eventDescription;
    }

    private ArrayList<String> sortArraysByYeay(ArrayList<Event> eventsArray) {


        for (int i = 0; i < eventsArray.size(); i++)
        {
            for (int j = i + 1; j < eventsArray.size(); j++)
            {
                if (Integer.parseInt(eventsArray.get(i).getYear()) > Integer.parseInt(eventsArray.get(j).getYear()))
                {
                    Event temp = eventsArray.get(i);
                    eventsArray.set(i, eventsArray.get(j));
                    eventsArray.set(j, temp);
                }
            }
        }

        ArrayList<String> eventDescription = new ArrayList<>();
        for(Event e : eventsArray)
        {
            eventDescription.add(e.getEventType() +
                    " : " +
                    e.getCity() +
                    ", " +
                    e.getCountry() +
                    "( " +
                    e.getYear() +
                    " ) \n" +
                    person.getFirstName().toString() +
                    " " +
                    person.getLastName()
            );
        }
        return  eventDescription;
    }

}
















































