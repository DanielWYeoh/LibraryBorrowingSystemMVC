package com.fad.LibrarySystem.view;

import com.fad.LibrarySystem.model.Books;
import java.util.List;

public class BookView {

    public void showBook(Books book) {
        System.out.println(book.getInfo());
    }

    public void showAllBooks(List<Books> books) {
        if (books.isEmpty()) {
            System.out.println("No books in catalog.");
            return;
        }
        System.out.println("----- Book Catalog -----");
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i).getInfo());
        }
    }

    public void showBookAdded(String title) {
        System.out.println("Book added: " + title);
    }

    public void showBookNotFound() {
        System.out.println("Book not found.");
    }

    public void showBookMenu() {
        System.out.println("\n--- Book Menu ---");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search by Genre");
        System.out.println("0. Back");
        System.out.print("Choose: ");
    }
}
