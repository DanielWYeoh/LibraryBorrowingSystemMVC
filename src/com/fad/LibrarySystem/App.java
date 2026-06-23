package com.fad.LibrarySystem;

import com.fad.LibrarySystem.controller.BookFXController;
import com.fad.LibrarySystem.database.DatabaseManager;
import com.fad.LibrarySystem.model.LibraryService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App extends Application {

    private final LibraryService service = new LibraryService();

    @Override
    public void start(Stage stage) throws Exception {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().add(loadBookTab());
        // MemberTab, BorrowTab, FineTab will be added here as we build them

        stage.setScene(new Scene(tabPane, 1000, 650));
        stage.setTitle("Library Borrowing System");
        stage.show();
    }

    private Tab loadBookTab() throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/fad/LibrarySystem/view/BookTab.fxml"));
        Tab tab = new Tab("Books", loader.load());
        loader.<BookFXController>getController().setService(service);
        return tab;
    }

    @Override
    public void stop() {
        DatabaseManager.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
