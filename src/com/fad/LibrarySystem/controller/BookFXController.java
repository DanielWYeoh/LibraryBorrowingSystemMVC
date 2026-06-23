package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.Book;
import com.fad.LibrarySystem.model.LibraryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class BookFXController {

    @FXML private TextField                  genreSearchField;
    @FXML private TableView<Book>            bookTable;
    @FXML private TableColumn<Book, String>  colBookId;
    @FXML private TableColumn<Book, String>  colTitle;
    @FXML private TableColumn<Book, String>  colAuthor;
    @FXML private TableColumn<Book, String>  colGenre;
    @FXML private TableColumn<Book, String>  colStatus;

    private LibraryService service;

    // Called by App.java after FXML is loaded to inject the shared service
    public void setService(LibraryService service) {
        this.service = service;
        loadBooks();
    }

    @FXML
    public void initialize() {
        colBookId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBookId()));
        colTitle .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        colAuthor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAuthor()));
        colGenre .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGenre()));
        colStatus.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().isAvailable() ? "Available" : "Borrowed"));
    }

    private void loadBooks() {
        bookTable.setItems(FXCollections.observableArrayList(service.getCatalog()));
    }

    @FXML
    private void handleSearch() {
        String genre = genreSearchField.getText().trim();
        if (genre.isEmpty()) {
            loadBooks();
        } else {
            bookTable.setItems(FXCollections.observableArrayList(service.searchByGenre(genre)));
        }
    }

    @FXML
    private void handleAddBook() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add Book");

        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        TextField tfId     = new TextField(); tfId.setPromptText("Book ID (e.g. B010)");
        TextField tfTitle  = new TextField(); tfTitle.setPromptText("Title");
        TextField tfAuthor = new TextField(); tfAuthor.setPromptText("Author");
        TextField tfGenre  = new TextField(); tfGenre.setPromptText("Genre");

        VBox box = new VBox(6,
            new Label("Book ID:"), tfId,
            new Label("Title:"),   tfTitle,
            new Label("Author:"),  tfAuthor,
            new Label("Genre:"),   tfGenre);
        box.setPadding(new Insets(12));
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(btn -> {
            if (btn != addBtn) return null;
            String id     = tfId.getText().trim();
            String title  = tfTitle.getText().trim();
            String author = tfAuthor.getText().trim();
            String genre  = tfGenre.getText().trim();
            if (id.isEmpty() || title.isEmpty() || author.isEmpty()) return null;
            return service.addBook(id, title, author, genre.isEmpty() ? "General" : genre);
        });

        dialog.showAndWait().ifPresent(book -> {
            if (book == null) showAlert("Could not add book. ID may be duplicate or required fields are empty.");
            else loadBooks();
        });
    }

    @FXML
    private void handleDeleteBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a book from the table first.");
            return;
        }
        boolean ok = service.removeBook(selected.getBookId());
        if (ok) loadBooks();
        else showAlert("Cannot delete \"" + selected.getTitle() + "\" — it is currently borrowed.");
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }
}
