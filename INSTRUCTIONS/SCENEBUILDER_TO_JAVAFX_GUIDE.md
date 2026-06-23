# SceneBuilder → JavaFX Implementation Guide
### Library Borrowing System

---

## VS Code Setup — Recommended Extensions

Install these from the Extensions panel (`Ctrl+Shift+X`) before starting:

| Extension | Publisher | Why |
|---|---|---|
| Extension Pack for Java | Microsoft | Java language support, Maven panel, run/debug |
| Maven for Java | Microsoft | Included in the pack — gives you the Maven sidebar |

After installing, VS Code will show a **Maven** icon in the left sidebar (looks like `M`). You will use this to run the app.

---

## Overview

This guide walks you through migrating the existing console-based Library Borrowing System into a JavaFX GUI application using SceneBuilder. The model layer (`LibraryService`, `DatabaseManager`, all model classes) stays completely unchanged.

```
WHAT CHANGES          WHAT STAYS THE SAME
─────────────────     ──────────────────────────────────
Main.java             model/Book.java
  → App.java          model/Member.java
                      model/BorrowRecord.java
view/BookView.java    model/Fine.java
view/MemberView.java  model/Reservation.java
view/BorrowView.java  model/LibraryService.java
view/FineView.java    database/DatabaseManager.java
view/MenuView.java
  → replaced by .fxml files
```

---

## Files You Will Create

```
src/
  com/fad/LibrarySystem/
    App.java                               ← JavaFX entry point
    controller/
      BookFXController.java
      MemberFXController.java
      BorrowFXController.java
      FineFXController.java
  resources/
    com/fad/LibrarySystem/view/
      BookTab.fxml
      MemberTab.fxml
      BorrowTab.fxml
      FineTab.fxml
```

---

## PART 1 — Update `pom.xml`

Open `pom.xml` and make two changes.

### Step 1 — Add JavaFX dependencies

Inside the `<dependencies>` block, add below the existing sqlite dependency:

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.4</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.4</version>
</dependency>
```

### Step 2 — Replace the jar plugin with the JavaFX plugin

Remove the entire `maven-jar-plugin` block and replace it with:

```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.fad.LibrarySystem.App</mainClass>
    </configuration>
</plugin>
```

### Step 3 — Add the resources source directory

Inside `<build>`, after `<sourceDirectory>src</sourceDirectory>`, add:

```xml
<resources>
    <resource>
        <directory>src/resources</directory>
    </resource>
</resources>
```

### Step 4 — Reload Maven

In the **VS Code terminal** (`Ctrl+` `` ` ``):

```bash
mvn dependency:resolve
```

You should see `BUILD SUCCESS` and lines mentioning `javafx-controls` and `javafx-fxml`.

Alternatively, open the **Maven** sidebar panel → click the **Refresh** icon (↻) at the top of the panel. The dependencies will appear under **Dependencies** in the tree.

---

## PART 2 — Create the Resources Folder

### Step 5 — Create the FXML directory

In the VS Code **terminal**, run this single command from the project root:

```bash
mkdir -p src/resources/com/fad/LibrarySystem/view
```

Or do it via the Explorer panel:
1. Click the **Explorer** icon in the left sidebar (`Ctrl+Shift+E`)
2. Right-click the `src` folder → **New Folder**
3. Type `resources/com/fad/LibrarySystem/view` (VS Code creates nested folders automatically)

The result should look like:

```
src/
  resources/
    com/
      fad/
        LibrarySystem/
          view/          ← your .fxml files go here
```

> **Note:** VS Code does not have an "Mark as Resources Root" concept. Maven uses the `<resources>` block you added in Step 3 to tell the compiler where to look — that is all that is needed.

---

## PART 3 — SceneBuilder: Design `BookTab.fxml`

> Open SceneBuilder. If you do not have it, download from: https://gluonhq.com/products/scene-builder/

### Step 6 — Set the controller class

1. Open SceneBuilder → **New Document**
2. In the bottom-left **Document** panel → **Controller** section
3. In the **Controller class** field type:
   ```
   com.fad.LibrarySystem.controller.BookFXController
   ```

### Step 7 — Build the layout

Drag components from the left **Library** panel in this order:

1. Drag **AnchorPane** onto the canvas — this is the root
2. Drag a **VBox** inside the AnchorPane
   - In **Layout** panel: set all anchor constraints to `0` (top, right, bottom, left)
3. Inside the **VBox**, drag an **HBox** (this is the toolbar row)
   - Spacing: `10`
   - Padding: `10` on all sides
4. Inside the **HBox**, add these components in order:
   - **TextField** → in **Code** panel set `fx:id` = `genreSearchField`, set `promptText` = `Search by genre...`
   - **Button** → text = `Search`, in **Code** panel set `onAction` = `#handleSearch`
   - **Separator** → set orientation to Vertical
   - **Button** → text = `Add Book`, `onAction` = `#handleAddBook`
   - **Button** → text = `Delete Book`, `onAction` = `#handleDeleteBook`
5. Back in the **VBox**, drag a **TableView** below the HBox
   - In **Layout** panel: set `VBox.vgrow` = `ALWAYS`
   - In **Code** panel: set `fx:id` = `bookTable`
6. In the **TableView**, you will see two default columns. Configure them:
   - Column 1: text = `ID`, `fx:id` = `colBookId`, prefWidth = `80`
   - Column 2: text = `Title`, `fx:id` = `colTitle`, prefWidth = `200`
7. Add 3 more columns by right-clicking the TableView header area → **Add Column**:
   - Column 3: text = `Author`, `fx:id` = `colAuthor`, prefWidth = `150`
   - Column 4: text = `Genre`, `fx:id` = `colGenre`, prefWidth = `100`
   - Column 5: text = `Status`, `fx:id` = `colStatus`, prefWidth = `90`

### Step 8 — Export the controller skeleton

1. In SceneBuilder menu: **View → Show Sample Controller Skeleton**
2. A popup appears with auto-generated Java code showing all `@FXML` fields and empty handler methods
3. Click **Copy to Clipboard**
4. Keep this — you will paste it into `BookFXController.java` in Part 4

### Step 9 — Save the FXML

1. **File → Save As**
2. Navigate to: `src/resources/com/fad/LibrarySystem/view/`
3. Save as `BookTab.fxml`

---

## PART 4 — SceneBuilder: Design `MemberTab.fxml`

### Step 10 — New document, set controller

1. SceneBuilder → **New Document**
2. Controller class: `com.fad.LibrarySystem.controller.MemberFXController`

### Step 11 — Build the layout

1. Root: **AnchorPane**
2. Inside: **VBox** anchored all sides (top=0, right=0, bottom=0, left=0)
3. Inside VBox — **HBox** (spacing=10, padding=10):
   - **Button** text = `Register Member`, `onAction` = `#handleRegister`
   - **Button** text = `Remove Member`, `onAction` = `#handleRemove`
4. Inside VBox below HBox — **TableView**, `fx:id` = `memberTable`, VBox.vgrow = ALWAYS
   - Column 1: text = `Member ID`, `fx:id` = `colMemberId`, prefWidth = `120`
   - Column 2: text = `Name`, `fx:id` = `colName`, prefWidth = `200`
   - Column 3: text = `Items Borrowed`, `fx:id` = `colBorrowCount`, prefWidth = `120`

### Step 12 — Export skeleton and save

1. **View → Show Sample Controller Skeleton** → Copy to Clipboard
2. **File → Save As** → `src/resources/com/fad/LibrarySystem/view/MemberTab.fxml`

---

## PART 5 — SceneBuilder: Design `BorrowTab.fxml`

### Step 13 — New document, set controller

1. SceneBuilder → **New Document**
2. Controller class: `com.fad.LibrarySystem.controller.BorrowFXController`

### Step 14 — Build the layout

1. Root: **AnchorPane**
2. Inside: **VBox** anchored all sides, spacing = `8`
3. Inside VBox — **HBox** (spacing=10, padding=10) for the form:
   - **Label** text = `Member ID:`
   - **TextField** `fx:id` = `memberIdField`, promptText = `e.g. M001`
   - **Label** text = `Item ID:`
   - **TextField** `fx:id` = `itemIdField`, promptText = `e.g. B001`
   - **Button** text = `Borrow`, `onAction` = `#handleBorrow`
   - **Button** text = `Return`, `onAction` = `#handleReturn`
4. Inside VBox — **Label** for feedback:
   - `fx:id` = `statusLabel`
   - text = `` (empty)
   - padding left = `10`
5. Inside VBox — **TableView**, `fx:id` = `recordTable`, VBox.vgrow = ALWAYS
   - Column 1: text = `Record ID`, `fx:id` = `colRecordId`, prefWidth = `120`
   - Column 2: text = `Member`, `fx:id` = `colMember`, prefWidth = `120`
   - Column 3: text = `Item`, `fx:id` = `colItem`, prefWidth = `180`
   - Column 4: text = `Borrow Date`, `fx:id` = `colBorrowDate`, prefWidth = `110`
   - Column 5: text = `Return Date`, `fx:id` = `colReturnDate`, prefWidth = `110`
   - Column 6: text = `Status`, `fx:id` = `colReturnStatus`, prefWidth = `80`

### Step 15 — Export skeleton and save

1. **View → Show Sample Controller Skeleton** → Copy to Clipboard
2. **File → Save As** → `src/resources/com/fad/LibrarySystem/view/BorrowTab.fxml`

---

## PART 6 — SceneBuilder: Design `FineTab.fxml`

### Step 16 — New document, set controller

1. SceneBuilder → **New Document**
2. Controller class: `com.fad.LibrarySystem.controller.FineFXController`

### Step 17 — Build the layout

1. Root: **VBox** (set prefWidth=980, prefHeight=600)
2. Inside VBox — **TableView**, `fx:id` = `fineTable`, VBox.vgrow = ALWAYS
   - Column 1: text = `Fine ID`, `fx:id` = `colFineId`, prefWidth = `140`
   - Column 2: text = `Member`, `fx:id` = `colFineMember`, prefWidth = `150`
   - Column 3: text = `Item`, `fx:id` = `colFineItem`, prefWidth = `200`
   - Column 4: text = `Days Late`, `fx:id` = `colDaysLate`, prefWidth = `100`
   - Column 5: text = `Amount (Rp)`, `fx:id` = `colAmount`, prefWidth = `120`

### Step 18 — Export skeleton and save

1. **View → Show Sample Controller Skeleton** → Copy to Clipboard
2. **File → Save As** → `src/resources/com/fad/LibrarySystem/view/FineTab.fxml`

---

## PART 7 — Write the Java Controllers

For each controller, create a new Java file in `src/com/fad/LibrarySystem/controller/`.

> The `@FXML` field names must exactly match the `fx:id` values you set in SceneBuilder.

### Step 19 — Create `BookFXController.java`

```java
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.Book;
import com.fad.LibrarySystem.model.LibraryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class BookFXController {

    @FXML private TextField              genreSearchField;
    @FXML private TableView<Book>        bookTable;
    @FXML private TableColumn<Book, String> colBookId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colGenre;
    @FXML private TableColumn<Book, String> colStatus;

    private LibraryService service;

    // Called by App.java after FXML is loaded to inject the shared service
    public void setService(LibraryService service) {
        this.service = service;
        loadBooks();
    }

    @FXML
    public void initialize() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colGenre .setCellValueFactory(new PropertyValueFactory<>("genre"));
        colStatus.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().isAvailable() ? "Available" : "Borrowed"));
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
            if (btn == addBtn) {
                String genre = tfGenre.getText().trim();
                return service.addBook(
                    tfId.getText().trim(),
                    tfTitle.getText().trim(),
                    tfAuthor.getText().trim(),
                    genre.isEmpty() ? "General" : genre);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            if (book == null) showAlert("Could not add book. Check for duplicate ID.");
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
```

### Step 20 — Create `MemberFXController.java`

```java
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.model.Member;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class MemberFXController {

    @FXML private TableView<Member>           memberTable;
    @FXML private TableColumn<Member, String> colMemberId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, Number> colBorrowCount;

    private LibraryService service;

    public void setService(LibraryService service) {
        this.service = service;
        loadMembers();
    }

    @FXML
    public void initialize() {
        colMemberId  .setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName      .setCellValueFactory(new PropertyValueFactory<>("name"));
        colBorrowCount.setCellValueFactory(data ->
            new SimpleIntegerProperty(data.getValue().getBorrowCount()));
    }

    private void loadMembers() {
        memberTable.setItems(FXCollections.observableArrayList(service.getMembers()));
    }

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
            if (btn == registerBtn)
                return service.registerMember(tfId.getText().trim(), tfName.getText().trim());
            return null;
        });

        dialog.showAndWait().ifPresent(member -> {
            if (member == null) showAlert("Could not register member. Check for duplicate ID.");
            else loadMembers();
        });
    }

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

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }
}
```

### Step 21 — Create `BorrowFXController.java`

```java
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BorrowFXController {

    @FXML private TextField                        memberIdField;
    @FXML private TextField                        itemIdField;
    @FXML private Label                            statusLabel;
    @FXML private TableView<BorrowRecord>          recordTable;
    @FXML private TableColumn<BorrowRecord, String> colRecordId;
    @FXML private TableColumn<BorrowRecord, String> colMember;
    @FXML private TableColumn<BorrowRecord, String> colItem;
    @FXML private TableColumn<BorrowRecord, String> colBorrowDate;
    @FXML private TableColumn<BorrowRecord, String> colReturnDate;
    @FXML private TableColumn<BorrowRecord, String> colReturnStatus;

    private LibraryService service;

    public void setService(LibraryService service) {
        this.service = service;
        loadRecords();
    }

    @FXML
    public void initialize() {
        colRecordId   .setCellValueFactory(new PropertyValueFactory<>("recordId"));
        colMember     .setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getMember().getName()));
        colItem       .setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getItem().getTitle()));
        colBorrowDate .setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colReturnDate .setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colReturnStatus.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().isReturned() ? "Returned" : "Active"));
    }

    @FXML
    private void handleBorrow() {
        String memberId = memberIdField.getText().trim();
        String itemId   = itemIdField.getText().trim();

        if (memberId.isEmpty() || itemId.isEmpty()) {
            setStatus("Member ID and Item ID are required.", true);
            return;
        }

        Member      member = service.findMemberById(memberId);
        LibraryItem item   = service.findItemById(itemId);

        if (member == null) { setStatus("Member not found: " + memberId, true); return; }
        if (item == null)   { setStatus("Item not found: " + itemId, true);     return; }
        if (!item.isAvailable()) { setStatus("\"" + item.getTitle() + "\" is not available.", true); return; }

        boolean ok = member.borrowItem(item);
        if (ok) {
            service.recordBorrow(member, item);
            setStatus("Borrowed: \"" + item.getTitle() + "\" → " + member.getName(), false);
            loadRecords();
            clearFields();
        } else {
            setStatus("Borrow limit reached for " + member.getName() + ".", true);
        }
    }

    @FXML
    private void handleReturn() {
        String memberId = memberIdField.getText().trim();
        String itemId   = itemIdField.getText().trim();

        if (memberId.isEmpty() || itemId.isEmpty()) {
            setStatus("Member ID and Item ID are required.", true);
            return;
        }

        Member      member = service.findMemberById(memberId);
        LibraryItem item   = service.findItemById(itemId);

        if (member == null) { setStatus("Member not found: " + memberId, true); return; }
        if (item == null)   { setStatus("Item not found: " + itemId, true);     return; }

        boolean ok = member.returnItem(item);
        if (ok) {
            service.recordReturn(member, item);
            setStatus("Returned: \"" + item.getTitle() + "\" from " + member.getName(), false);
            loadRecords();
            clearFields();
        } else {
            setStatus("Item not found in " + member.getName() + "'s borrowed list.", true);
        }
    }

    private void loadRecords() {
        recordTable.setItems(FXCollections.observableArrayList(service.getBorrowRecords()));
    }

    private void clearFields() {
        memberIdField.clear();
        itemIdField.clear();
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError
            ? "-fx-text-fill: #cc0000; -fx-font-weight: bold;"
            : "-fx-text-fill: #007700; -fx-font-weight: bold;");
    }
}
```

### Step 22 — Create `FineFXController.java`

```java
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.Fine;
import com.fad.LibrarySystem.model.LibraryService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FineFXController {

    @FXML private TableView<Fine>           fineTable;
    @FXML private TableColumn<Fine, String> colFineId;
    @FXML private TableColumn<Fine, String> colFineMember;
    @FXML private TableColumn<Fine, String> colFineItem;
    @FXML private TableColumn<Fine, Number> colDaysLate;
    @FXML private TableColumn<Fine, Number> colAmount;

    private LibraryService service;

    public void setService(LibraryService service) {
        this.service = service;
        loadFines();
    }

    @FXML
    public void initialize() {
        colFineId    .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFineId()));
        colFineMember.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMember().getName()));
        colFineItem  .setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getItem().getTitle()));
        colDaysLate  .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getDaysLate()));
        colAmount    .setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getFineAmount()));
    }

    private void loadFines() {
        fineTable.setItems(FXCollections.observableArrayList(service.getFines()));
    }
}
```

---

## PART 8 — Create `App.java` (JavaFX Entry Point)

### Step 23 — Create `App.java`

Create `src/com/fad/LibrarySystem/App.java`:

```java
package com.fad.LibrarySystem;

import com.fad.LibrarySystem.controller.*;
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

        tabPane.getTabs().add(loadTab("Books",         "/com/fad/LibrarySystem/view/BookTab.fxml",   BookFXController.class));
        tabPane.getTabs().add(loadTab("Members",       "/com/fad/LibrarySystem/view/MemberTab.fxml", MemberFXController.class));
        tabPane.getTabs().add(loadTab("Borrow/Return", "/com/fad/LibrarySystem/view/BorrowTab.fxml", BorrowFXController.class));
        tabPane.getTabs().add(loadTab("Fines",         "/com/fad/LibrarySystem/view/FineTab.fxml",   FineFXController.class));

        stage.setScene(new Scene(tabPane, 1000, 650));
        stage.setTitle("Library Borrowing System");
        stage.show();
    }

    private <T> Tab loadTab(String label, String fxmlPath, Class<T> controllerType) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Tab tab = new Tab(label, loader.load());

        // Inject the shared LibraryService into the controller
        Object ctrl = loader.getController();
        if (ctrl instanceof BookFXController   c) c.setService(service);
        if (ctrl instanceof MemberFXController c) c.setService(service);
        if (ctrl instanceof BorrowFXController c) c.setService(service);
        if (ctrl instanceof FineFXController   c) c.setService(service);

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
```

---

## PART 9 — Run the Application

### Step 24 — Run via Maven

**Option A — Terminal** (fastest):

Open the VS Code terminal (`Ctrl+` `` ` ``) and run:

```bash
mvn javafx:run
```

**Option B — Maven sidebar**:

1. Click the **Maven** icon in the left sidebar
2. Expand your project → **Plugins** → **javafx**
3. Click the ▶ button next to `javafx:run`

**Option C — VS Code run button** (requires Extension Pack for Java):

1. Open `App.java`
2. Click the **▶ Run** button that appears above the `main` method
3. If it fails with "JavaFX runtime components are missing", use Option A instead — the Maven plugin handles the module path automatically

### Step 25 — Verify each tab works

| Tab | What to test |
|---|---|
| Books | Table loads 5 seeded books. Add a new book. Delete one that is available. Try deleting a borrowed one — should show warning. |
| Members | Table loads Alice, Bob, Charlie. Register a new member. |
| Borrow/Return | Type `M001` and `B001` → click Borrow. Status label turns green. Row appears in table. Click Return with same IDs — status updates. |
| Fines | Empty at first. (Fines are created via `service.recordFine()` — you can test via code or add a button later.) |

---

## PART 10 — Critical Rules for SceneBuilder ↔ Java

### The `fx:id` ↔ `@FXML` contract

Every `fx:id` set in SceneBuilder **must exactly match** a `@FXML` field name in the controller:

```
SceneBuilder                          Java Controller
────────────────────────────────      ────────────────────────────────────
fx:id="bookTable"           →         @FXML private TableView<Book> bookTable;
fx:id="genreSearchField"    →         @FXML private TextField genreSearchField;
fx:id="colTitle"            →         @FXML private TableColumn<Book,String> colTitle;
```

### The `onAction` ↔ method contract

Every `onAction="#handleAddBook"` in SceneBuilder **must exactly match** a `@FXML` method in the controller:

```
SceneBuilder                          Java Controller
────────────────────────────────      ────────────────────────────────────
onAction="#handleAddBook"   →         @FXML private void handleAddBook() { }
onAction="#handleBorrow"    →         @FXML private void handleBorrow() { }
```

### The controller class contract

The `fx:controller` attribute at the top of the FXML **must match** the fully-qualified class name:

```xml
<!-- In BookTab.fxml (set via SceneBuilder's Document > Controller field) -->
<AnchorPane fx:controller="com.fad.LibrarySystem.controller.BookFXController" ...>
```

### `initialize()` vs `setService()`

- `initialize()` runs **automatically** when FXML is loaded — the `service` field is still `null` at this point, so only set up column bindings here, not data loading.
- `setService()` is called **by `App.java`** after loading, so load table data there.

---

## PART 11 — Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| `NullPointerException` on `@FXML` field | `fx:id` in FXML does not match field name in controller | Open FXML in SceneBuilder, check **Code** panel for each component |
| `Location is not set` on FXMLLoader | Wrong resource path | Verify path starts with `/` and matches exact folder structure in `src/resources/` |
| `IllegalAccessException` on controller | Controller class or method is not public | Make sure the controller class is `public` |
| `ClassNotFoundException` for controller | Typo in `fx:controller` inside FXML | Open the FXML file and check the `fx:controller` attribute at the top |
| Table shows blank rows | `PropertyValueFactory` string does not match getter name | `new PropertyValueFactory<>("bookId")` requires a `getBookId()` method on the model |
| JavaFX classes not found | Missing JavaFX dependency | Re-run `mvn dependency:resolve`, check `pom.xml` has `javafx-controls` and `javafx-fxml` |

---

## Quick Reference: SceneBuilder Workflow Per Screen

```
For each new screen:
  1. SceneBuilder → New Document
  2. Set Controller class in Document > Controller panel
  3. Drag layout containers (VBox / HBox / AnchorPane)
  4. Drag controls (TableView, TextField, Button, Label)
  5. Set fx:id on every control you need to reference in Java
  6. Set onAction on every button
  7. View → Show Sample Controller Skeleton → Copy
  8. File → Save As → src/resources/com/fad/LibrarySystem/view/XxxTab.fxml
  9. Create XxxFXController.java, paste skeleton, implement methods
```
