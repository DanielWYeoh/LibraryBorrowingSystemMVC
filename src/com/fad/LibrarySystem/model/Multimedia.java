package com.fad.LibrarySystem.model;

public class Multimedia extends LibraryItem {

    private String type;
    private String duration;

    public Multimedia(String itemId, String title, String type, String duration) {
        super(itemId, title);
        this.type     = type;
        this.duration = duration;
    }

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
