package com.fad.LibrarySystem.model;

public class Book extends LibraryItem {

    private String author;
    private String genre;

    public Book(String bookId, String title, String author, String genre) {
        super(bookId, title);
        this.author = author;
        this.genre  = genre;
    }

    public static Book[] getInitialBooks() {
        return new Book[]{
            new Book("B001", "The Great Gatsby",       "F. Scott Fitzgerald", "Classic"),
            new Book("B002", "To Kill a Mockingbird",  "Harper Lee",          "Fiction"),
            new Book("B003", "1984",                   "George Orwell",       "Dystopian"),
            new Book("B004", "Brave New World",        "Aldous Huxley",       "Sci-Fi"),
            new Book("B005", "The Catcher in the Rye", "J.D. Salinger",       "Fiction"),
        };
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
