package com.masjohncook.library.model;

public class Books extends LibraryItem {

    private String author;
    private String genre;

    public Books(String bookId, String title, String author, String genre) {
        super(bookId, title);
        this.author = author;
        this.genre  = genre;
    }

    public static Books[] getInitialBooks() {
        Books[] initial = new Books[5];
        initial[0] = new Books("B001", "The Great Gatsby",       "F. Scott Fitzgerald", "Classic");
        initial[1] = new Books("B002", "To Kill a Mockingbird",  "Harper Lee",          "Fiction");
        initial[2] = new Books("B003", "1984",                   "George Orwell",       "Dystopian");
        initial[3] = new Books("B004", "Brave New World",        "Aldous Huxley",       "Sci-Fi");
        initial[4] = new Books("B005", "The Catcher in the Rye", "J.D. Salinger",       "Fiction");
        return initial;
    }

    @Override
    public String getInfo() {
        String status = available ? "Available" : "Borrowed";
        return "[" + itemId + "] \"" + title + "\" by " + author
                + " | Genre: " + genre + " (" + status + ")";
    }

    @Override
    public String toString() { return getInfo(); }

    public String getBookId() { return itemId; }
    public String getAuthor() { return author; }
    public String getGenre()  { return genre; }

    public void setBookId(String bookId) { this.itemId = bookId; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre)   { this.genre  = genre; }
}
