package com.fad.LibrarySystem.model;

/** @deprecated Use {@link Book} instead. */
@Deprecated
public class Books extends Book {
    public Books(String bookId, String title, String author, String genre) {
        super(bookId, title, author, genre);
    }
}
