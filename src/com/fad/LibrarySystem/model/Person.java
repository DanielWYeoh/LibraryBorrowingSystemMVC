/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Development
 */
package com.fad.LibrarySystem.model;

/**
 * Abstract base class representing any person in the library system.
 *
 * Person is the root of the person hierarchy. It is extended by both
 * Member (a library patron who can borrow items) and Librarian (the
 * staff account that manages the library).
 *
 * Inheritance:
 *   Person
 *   └── Member
 *   └── Librarian
 *
 * Attributes:
 *   - id   : unique identifier for this person (e.g. "M001", "L001")
 *   - name : full display name of the person
 */
public class Person {

    protected String id;
    protected String name;

    public Person(String id, String name) {
        this.id   = id;
        this.name = name;
    }

    public String getInfo() {
        return "Person[" + id + "] " + name;
    }

    public String getId()   { return id; }
    public String getName() { return name; }

    public void setId(String id)     { this.id = id; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return getInfo(); }
}
