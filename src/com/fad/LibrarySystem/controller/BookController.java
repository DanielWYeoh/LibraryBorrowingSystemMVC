package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.Book;
import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.view.BookView;
import java.util.List;
import java.util.Scanner;

public class BookController {

    private final LibraryService service;
    private final BookView       bookView;
    private final Scanner        scanner;

    public BookController(LibraryService service, Scanner scanner) {
        this.service  = service;
        this.bookView = new BookView();
        this.scanner  = scanner;
    }

    public void handleMenu() {
        int choice = -1;
        while (choice != 0) {
            bookView.showBookMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                bookView.showError("Please enter a valid number.");
                continue;
            }
            switch (choice) {
                case 1  -> addBook();
                case 2  -> viewAllBooks();
                case 3  -> searchByGenre();
                case 0  -> { return; }
                default -> bookView.showError("Invalid option. Please try again.");
            }
        }
    }

    private void addBook() {
        System.out.print("Book ID : "); String id     = scanner.nextLine().trim();
        System.out.print("Title   : "); String title  = scanner.nextLine().trim();
        System.out.print("Author  : "); String author = scanner.nextLine().trim();
        System.out.print("Genre   : "); String genre  = scanner.nextLine().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            bookView.showError("Book ID, Title, and Author cannot be empty.");
            return;
        }
        try {
            Book book = service.addBook(id, title, author, genre.isEmpty() ? "General" : genre);
            if (book != null) bookView.showBookAdded(title);
            else bookView.showError("Failed to add book (duplicate ID or catalog full).");
        } catch (RuntimeException e) {
            bookView.showError("Could not save book: " + e.getMessage());
        }
    }

    private void viewAllBooks() {
        bookView.showAllBooks(service.getCatalog());
    }

    private void searchByGenre() {
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        List<Book> results = service.searchByGenre(genre);
        bookView.showGenreResults(results, genre);
    }
}
