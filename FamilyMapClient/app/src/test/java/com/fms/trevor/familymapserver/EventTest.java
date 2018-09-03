package com.fms.trevor.familymapserver;

import com.fms.trevor.familymapserver.model.Event;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EventTest {

    public EventTest(){
        runTests();
    }

   @Test
    public void runTests()
    {

        Event e = new Event();

        e.setYear("test");
        e.setPersonID("test");
        e.setEventType("test");
        e.setDescendant("test");
        e.setCity("test");
        e.setCountry("test");
        e.setLat("test");
        e.setLng("test");


        assertEquals("test", e.getPersonID());
        assertEquals("test", e.getEventType());
        assertEquals( "test", e.getDescendant());
        assertEquals("test", e.getYear());
        assertEquals("test", e.getCity());
        assertEquals("test", e.getCountry());
        assertEquals("test", e.getLat());
        assertEquals("test", e.getLng());
    }

}
