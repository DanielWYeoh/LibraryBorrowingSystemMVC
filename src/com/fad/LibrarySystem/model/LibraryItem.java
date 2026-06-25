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
 * Abstract base class representing any item that can be borrowed from the library.
 *
 * LibraryItem is the root of the item hierarchy. It is extended by Book
 * (a physical book) and Multimedia (DVDs, CDs, audiobooks, etc.).
 * Both subclasses inherit the core identity and availability tracking
 * defined here.
 *
 * Inheritance:
 *   LibraryItem
 *   └── Book
 *   └── Multimedia
 *
 * Attributes:
 *   - itemId    : unique identifier for this item (e.g. "B001", "MM001")
 *   - title     : display title of the item
 *   - available : true if the item is on the shelf; false if currently borrowed
 */
public class LibraryItem {

    protected String  itemId;
    protected String  title;
    protected boolean available;

    public LibraryItem(String itemId, String title) {
        this.itemId    = itemId;
        this.title     = title;
        this.available = true;
    }

    public String getInfo() {
        String status = available ? "Available" : "Borrowed";
        return "[" + itemId + "] \"" + title + "\" (" + status + ")";
    }

    public String  getItemId()   { return itemId; }
    public String  getTitle()    { return title; }
    public boolean isAvailable() { return available; }

    public void setItemId(String itemId)        { this.itemId    = itemId; }
    public void setTitle(String title)          { this.title     = title; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() { return getInfo(); }
}
