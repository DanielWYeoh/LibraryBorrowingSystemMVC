package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.Books;
import com.fad.LibrarySystem.model.Librarian;
import com.fad.LibrarySystem.view.BookView;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookController {

    private Librarian librarian;
    private BookView  bookView;
    private Scanner   scanner;

    public BookController(Librarian librarian, Scanner scanner) {
        this.librarian = librarian;
        this.bookView  = new BookView();
        this.scanner   = scanner;
    }

    public void handleMenu() {
        int choice = -1;
        while (choice != 0) {
            bookView.showBookMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                continue;
            }
            switch (choice) {
                case 1  -> addBook();
                case 2  -> viewAllBooks();
                case 3  -> searchByGenre();
                case 0  -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addBook() {
        System.out.print("Book ID : "); String id     = scanner.nextLine();
        System.out.print("Title   : "); String title  = scanner.nextLine();
        System.out.print("Author  : "); String author = scanner.nextLine();
        System.out.print("Genre   : "); String genre  = scanner.nextLine();

        Books book = librarian.addBook(id, title, author, genre);   // Model
        if (book != null) bookView.showBookAdded(title);            // View
        else System.out.println("Failed to add book (duplicate ID or catalog full).");
    }

    private void viewAllBooks() {
        List<Books> books = new ArrayList<>();
        for (int i = 0; i < librarian.getCatalogSize(); i++) {
            books.add(librarian.getCatalog()[i]);
        }
        bookView.showAllBooks(books);                               // View
    }

    private void searchByGenre() {
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        librarian.searchByGenre(librarian.getCatalog(), librarian.getCatalogSize(), genre); // Model
    }
}
