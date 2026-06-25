/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Development
 */
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.model.Member;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * JavaFX controller for the Members tab (MemberTab.fxml).
 *
 * MemberFXController connects the FXML layout to the application's data layer.
 * It is instantiated automatically by JavaFX when FXMLLoader loads MemberTab.fxml,
 * then App.java calls setService() to inject the shared LibraryService.
 *
 * Responsibilities:
 *   - Bind each TableColumn to the correct Member field (initialize)
 *   - Load and refresh the member table (loadMembers)
 *   - Handle the Register Member button — show an input dialog, call the service (handleRegister)
 *   - Handle the Remove Member button — remove the selected row (handleRemove)
 *
 * fx:id ↔ @FXML contract (must match MemberTab.fxml exactly):
 *   memberTable    → TableView holding all Member rows
 *   colMemberID    → "Member ID" column
 *   colName        → "Name" column
 *   colBorrowCount → "Items Borrowed" column
 *
 * MVC role: Controller (JavaFX)
 * FXML file: src/resources/com/fad/LibrarySystem/view/MemberTab.fxml
 */
public class MemberFXController {

    // ── FXML field bindings ───────────────────────────────────────────────────
    // Names must match the fx:id values in MemberTab.fxml exactly.
    // Note: colMemberID uses capital "ID" — matches the FXML fx:id precisely.

    @FXML private TableView<Member>           memberTable;
    @FXML private TableColumn<Member, String> colMemberID;    // "Member ID" column
    @FXML private TableColumn<Member, String> colName;        // "Name" column
    @FXML private TableColumn<Member, Number> colBorrowCount; // "Items Borrowed" column

    // ── Shared service ────────────────────────────────────────────────────────
    // Created once in App.java and shared with all tab controllers so they
    // all read/write the same in-memory data and database connection.

    private LibraryService service;

    // ── Service injection ─────────────────────────────────────────────────────
    // Called by App.java after the FXML is loaded. Data loading goes here,
    // not in initialize(), because the service is null when initialize() runs.

    /**
     * Injects the shared LibraryService and populates the table for the first time.
     * Called by App.java immediately after FXMLLoader loads this controller.
     *
     * @param service the application's single LibraryService instance
     */
    public void setService(LibraryService service) {
        this.service = service;
        loadMembers();
    }

    // ── initialize() ─────────────────────────────────────────────────────────
    // Called automatically by JavaFX after the FXML is loaded.
    // Safe to configure columns here; do NOT load data (service is null).
    //
    // colBorrowCount uses SimpleIntegerProperty (not SimpleStringProperty)
    // because the value is a number — JavaFX uses this for correct sorting.

    /**
     * Configures the table columns.
     * Called automatically by JavaFX after the FXML is loaded — before setService().
     */
    @FXML
    public void initialize() {
        colMemberID   .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMemberId()));
        colName       .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        // getBorrowCount() returns an int — wrap in SimpleIntegerProperty so the
        // column sorts numerically rather than lexicographically
        colBorrowCount.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getBorrowCount()));
    }

    // ── loadMembers() ─────────────────────────────────────────────────────────
    // Refreshes the table from the service's in-memory member list.
    // Called after any register or remove operation to reflect the latest state.

    /**
     * Reloads the full member list into the table.
     * Call this after any register or remove to reflect the latest state.
     */
    private void loadMembers() {
        memberTable.setItems(FXCollections.observableArrayList(service.getMembers()));
    }

    // ── handleRegister() ──────────────────────────────────────────────────────
    // Triggered by the Register Member button (onAction="#handleRegister" in MemberTab.fxml).
    // Opens a Dialog with two text fields for Member ID and Name.
    // setResultConverter calls service.registerMember() when the user clicks Register.
    // Returns null if the user cancels or leaves a required field empty.

    /**
     * Opens a Register Member dialog, collects input, and saves the new member.
     * Shows a warning if the ID is a duplicate or required fields are empty.
     */
    @FXML
    private void handleRegister() {
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle("Register Member");

        ButtonType registerBtn = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerBtn, ButtonType.CANCEL);

        TextField tfId   = new TextField(); tfId.setPromptText("Member ID (e.g. M010)");
        TextField tfName = new TextField(); tfName.setPromptText("Full Name");

        VBox box = new VBox(6, new Label("Member ID:"), tfId, new Label("Name:"), tfName);
        box.setPadding(new Insets(12));
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(btn -> {
            if (btn != registerBtn) return null;
            String id   = tfId.getText().trim();
            String name = tfName.getText().trim();
            if (id.isEmpty() || name.isEmpty()) return null;
            return service.registerMember(id, name);
        });

        // showAndWait blocks until the dialog closes.
        // A null result means cancel was clicked or a required field was empty.
        dialog.showAndWait().ifPresent(member -> {
            if (member == null) showAlert("Could not register member. ID may be duplicate or required fields are empty.");
            else loadMembers();
        });
    }

    // ── handleRemove() ────────────────────────────────────────────────────────
    // Triggered by the Remove Member button (onAction="#handleRemove" in MemberTab.fxml).
    // Reads the selected row from the table.
    // service.removeMember() returns false if the member still has borrowed items,
    // in which case we show an alert instead of removing.

    /**
     * Removes the currently selected member.
     * Shows a warning if no row is selected or if the member still has borrowed items.
     */
    @FXML
    private void handleRemove() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a member from the table first.");
            return;
        }
        boolean ok = service.removeMember(selected.getMemberId());
        if (ok) loadMembers();
        else showAlert("Cannot remove \"" + selected.getName() + "\" — they still have borrowed items.");
    }

    // ── showAlert() ───────────────────────────────────────────────────────────
    // Convenience method to display a warning popup with a single OK button.

    /**
     * Shows a warning alert dialog with the given message and an OK button.
     *
     * @param message the message to display
     */
    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }
}
