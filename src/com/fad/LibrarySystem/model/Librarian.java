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
 * Represents the identity of the librarian who manages the system.
 * Inherits from Person — gains id and name.
 *
 * Librarian is a simple identity class. All business logic (managing
 * books, members, borrow records, etc.) lives in {@link LibraryService},
 * which was extracted from this class to keep it focused on a single
 * responsibility.
 *
 * Inheritance:
 *   Librarian extends Person
 *   - Inherits : id (used as librarianId), name
 *   - Overrides: getInfo(), toString()
 */
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
