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
 * Represents a non-book media item that can be borrowed from the library.
 * Inherits from LibraryItem — gains itemId, title, and available.
 *
 * A Multimedia item covers physical media formats such as DVDs, CDs,
 * and audiobooks. It extends LibraryItem with two extra attributes
 * that describe what kind of media it is and how long it runs.
 *
 * Inheritance:
 *   Multimedia extends LibraryItem
 *   - Inherits : itemId, title, available (and their getters/setters)
 *   - Adds     : type, duration
 *   - Overrides: getInfo(), toString()
 *
 * Attributes:
 *   - type     : the format of the item (e.g. "DVD", "CD", "Audiobook")
 *   - duration : the running time of the item (e.g. "148 min", "15 hrs")
 */
public class Multimedia extends LibraryItem {

    private String type;
    private String duration;

    public Multimedia(String itemId, String title, String type, String duration) {
        super(itemId, title);
        this.type     = type;
        this.duration = duration;
    }

    /**
     * Returns the seed multimedia items inserted into the database on first startup.
     *
     * @return array of Multimedia objects covering DVD, CD, and Audiobook formats
     */
    public static Multimedia[] getInitialMultimedia() {
        Multimedia[] initial = new Multimedia[3];
        initial[0] = new Multimedia("MM001", "Inception",         "DVD",       "148 min");
        initial[1] = new Multimedia("MM002", "Dark Side of Moon", "CD",        "43 min");
        initial[2] = new Multimedia("MM003", "Sapiens Audiobook", "Audiobook", "15 hrs");
        return initial;
    }

    @Override
    public String getInfo() {
        String status = available ? "Available" : "Borrowed";
        return "[" + itemId + "] \"" + title + "\""
                + " | Type: " + type
                + " | Duration: " + duration
                + " (" + status + ")";
    }

    @Override
    public String toString() { return getInfo(); }

    public String getType()     { return type; }
    public String getDuration() { return duration; }

    public void setType(String type)         { this.type     = type; }
    public void setDuration(String duration) { this.duration = duration; }
}
