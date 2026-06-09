package com.fad.LibrarySystem.model;

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

    public String toString() { return getInfo(); }
}
