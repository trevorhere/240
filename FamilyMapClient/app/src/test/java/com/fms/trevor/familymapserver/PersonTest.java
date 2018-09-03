package com.fms.trevor.familymapserver;

import com.fms.trevor.familymapserver.model.Person;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PersonTest {


    @Test
    public void runTest()
    {

        Person p = new Person(

                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test"

        );

        assertEquals(p.getFather(), "test");
        assertEquals(p.getDescendant(), "test");
        assertEquals(p.getFirstName(), "test");
        assertEquals(p.getGender(), "test");
        assertEquals(p.getLastName(), "test");
        assertEquals(p.getMother(), "test");
        assertEquals(p.getPersonID(), "test");
        assertEquals(p.getSpouse(), "test");

    }
}
