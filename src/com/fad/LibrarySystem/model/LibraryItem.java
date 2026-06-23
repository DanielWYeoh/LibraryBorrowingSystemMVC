package com.fad.LibrarySystem.model;

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

    public String  getItemId()    { return itemId; }
    public String  getTitle()     { return title; }
    public boolean isAvailable()  { return available; }

    public void setItemId(String itemId)         { this.itemId    = itemId; }
    public void setTitle(String title)           { this.title     = title; }
    public void setAvailable(boolean available)  { this.available = available; }

    @Override
    public String toString() { return getInfo(); }
}
