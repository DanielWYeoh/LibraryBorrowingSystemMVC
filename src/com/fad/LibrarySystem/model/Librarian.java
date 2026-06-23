package com.fad.LibrarySystem.model;

public class Librarian extends Person {

    public Librarian(String librarianId, String name) {
        super(librarianId, name);
    }

    @Override
    public String getInfo() { return "Librarian[" + id + "] " + name; }

    @Override
    public String toString() { return getInfo(); }

    public String getLibrarianId() { return id; }
}
