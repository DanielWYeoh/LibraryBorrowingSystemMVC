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

> **Note:** VS Code does not have a "Mark as Resources Root" concept. Maven uses the `<resources>` block you added in Step 3 to tell the compiler where to look — that is all that is needed.

---

## SceneBuilder — Understanding the Interface

Before starting Part 3, take a moment to understand the SceneBuilder layout. Every step in Parts 3–6 refers to specific panels by name.

```
┌──────────────────┬──────────────────────────────┬────────────────────┐
│  LEFT PANEL      │        CENTER (Canvas)        │   RIGHT PANEL      │
│                  │                               │                    │
│  ┌────────────┐  │   This is where you see and   │  ┌──────────────┐  │
│  │  Library   │  │   drag components to build    │  │  Properties  │  │
│  │  (top)     │  │   your layout visually.       │  │  Layout      │  │
│  │            │  │                               │  │  Code        │  │
│  │  Search    │  │                               │  │  (3 tabs)    │  │
│  │  Controls  │  │                               │  └──────────────┘  │
│  │  Containers│  │                               │                    │
│  │  etc.      │  │                               │  This is called    │
│  └────────────┘  │                               │  the Inspector     │
│                  │                               │  panel.            │
│  ┌────────────┐  │                               │                    │
│  │  Document  │  │                               │                    │
│  │  (bottom)  │  │                               │                    │
│  │            │  │                               │                    │
│  │  Hierarchy │  │                               │                    │
│  │  Controller│  │                               │                    │
│  └────────────┘  │                               │                    │
└──────────────────┴──────────────────────────────┴────────────────────┘
```

### Left panel — Library section (top half)
This is where all controls live. It is organized into categories:
- **Containers** — AnchorPane, VBox, HBox, GridPane, etc.
- **Controls** — Button, TextField, Label, TableView, etc.
- **Miscellaneous** — Separator, etc.

You drag items from here onto the canvas.

### Left panel — Document section (bottom half)
This has two sub-tabs:
- **Hierarchy** — shows the tree structure of everything on the canvas
- **Controller** — where you type the controller class name

### Right panel — Inspector (3 tabs)
Select any component on the canvas, then use these tabs:
- **Properties** tab — sets visible text like `text`, `promptText`, `orientation`
- **Layout** tab — sets size and position like `prefWidth`, `VBox.vgrow`, anchor constraints
- **Code** tab — sets `fx:id` and `onAction` — **this is the most important tab**

> **Rule:** Whenever a step says "in the **Code** panel", it means click the **Code** tab on the right Inspector panel. When it says "in the **Properties** panel", click the **Properties** tab. When it says "in the **Layout** panel", click the **Layout** tab.

---

## PART 3 — SceneBuilder: Design `BookTab.fxml`

> If you do not have SceneBuilder, download and install it from: https://gluonhq.com/products/scene-builder/

### Step 6 — Open SceneBuilder and create a new empty document

1. Open SceneBuilder (double-click the application icon, or find it in your Applications / Start Menu)
2. When SceneBuilder opens, you will see a **welcome screen** or it may open an empty canvas directly
3. Go to the top menu: **File → New**
4. A dialog appears asking you to choose a template. Select **Empty** (do NOT choose Phone, Basic Application, or any other template)
5. Click **OK** — you will see a completely blank canvas in the center

### Step 7 — Set the controller class

Before placing any components, tell SceneBuilder which Java class will control this screen:

1. Look at the **bottom-left** of the SceneBuilder window — this is the **Document** panel
2. Inside the Document panel, click the **Controller** sub-tab (it appears as a small tab at the bottom of that panel)
3. You will see a field labeled **Controller class**
4. Click inside that field and type exactly:
   ```
   com.fad.LibrarySystem.controller.BookFXController
   ```
5. Press `Enter` to confirm

### Step 8 — Add the root AnchorPane

1. In the **left panel**, look at the **Library** section (top half)
2. Find the **Containers** category — click it to expand if it is collapsed
3. Find **AnchorPane** in the list
4. **Drag** AnchorPane onto the center canvas and **drop** it — it will become the root container and fill the canvas area
5. You will now see `AnchorPane` appear in the **Document → Hierarchy** tab (bottom-left)

### Step 9 — Add a VBox inside the AnchorPane

1. In the **Library** section, under **Containers**, find **VBox**
2. **Drag** VBox and **drop it onto the AnchorPane** on the canvas (drop it inside the AnchorPane border)
3. The hierarchy should now show: `AnchorPane → VBox`
4. Click the **VBox** to select it (it will be highlighted)
5. In the **right panel**, click the **Layout** tab
6. Scroll down to find the **AnchorPane Constraints** section — you will see four fields: Top, Right, Bottom, Left
7. Set all four fields to `0` — this makes the VBox fill the entire AnchorPane:
   - **Top:** `0`
   - **Right:** `0`
   - **Bottom:** `0`
   - **Left:** `0`

### Step 10 — Add a toolbar HBox inside the VBox

1. In the **Library** section, under **Containers**, find **HBox**
2. **Drag** HBox and **drop it onto the VBox** on the canvas (or drag it onto `VBox` in the Hierarchy panel)
3. Click the **HBox** to select it
4. In the **right panel**, click the **Properties** tab:
   - Find the **Spacing** field and type `10`
5. In the **right panel**, click the **Layout** tab:
   - Find the **Padding** section — set all four sides (Top, Right, Bottom, Left) to `10`

### Step 11 — Add a TextField inside the HBox

1. In the **Library** section, under **Controls**, find **TextField**
2. **Drag** TextField and **drop it onto the HBox** (either onto the HBox in the canvas or onto `HBox` in the Hierarchy)
3. Click the **TextField** to select it
4. In the **right panel**, click the **Properties** tab:
   - Find the **Prompt Text** field and type: `Search by genre...`
5. In the **right panel**, click the **Code** tab:
   - Find the **fx:id** field and type: `genreSearchField`
   - Press `Enter` to confirm

### Step 12 — Add a Search button inside the HBox

1. In the **Library** section, under **Controls**, find **Button**
2. **Drag** Button and **drop it onto the HBox** (to the right of the TextField)
3. Click the **Button** to select it
4. In the **right panel**, click the **Properties** tab:
   - Find the **Text** field and type: `Search`
5. In the **right panel**, click the **Code** tab:
   - Find the **On Action** field and type: `#handleSearch`
   - Press `Enter` to confirm

### Step 13 — Add a vertical Separator inside the HBox

1. In the **Library** section, under **Miscellaneous**, find **Separator**
2. **Drag** Separator and **drop it onto the HBox** (to the right of the Search button)
3. Click the **Separator** to select it
4. In the **right panel**, click the **Properties** tab:
   - Find the **Orientation** dropdown and select **VERTICAL**

### Step 14 — Add the remaining buttons inside the HBox

Add **Add Book** button:
1. In the **Library** section, under **Controls**, find **Button**
2. **Drag** Button and **drop it onto the HBox** (to the right of the Separator)
3. Click the **Button** to select it
4. In the **right panel**, **Properties** tab → **Text** field → type: `Add Book`
5. In the **right panel**, **Code** tab → **On Action** field → type: `#handleAddBook` → press `Enter`

Add **Delete Book** button:
1. **Drag** another **Button** and **drop it onto the HBox** (to the right of Add Book)
2. Click the **Button** to select it
3. In the **right panel**, **Properties** tab → **Text** field → type: `Delete Book`
4. In the **right panel**, **Code** tab → **On Action** field → type: `#handleDeleteBook` → press `Enter`

### Step 15 — Add a TableView inside the VBox (below the HBox)

1. In the **Library** section, under **Controls**, find **TableView**
2. **Drag** TableView and **drop it onto the VBox** — place it below the HBox (you can see the position in the Hierarchy panel: it should appear after `HBox` as a sibling, both inside `VBox`)
3. Click the **TableView** to select it
4. In the **right panel**, click the **Code** tab:
   - Find the **fx:id** field and type: `bookTable`
   - Press `Enter` to confirm
5. In the **right panel**, click the **Layout** tab:
   - Find the **VBox Constraints** section
   - Find the **Vgrow** dropdown and select **ALWAYS** — this makes the table expand to fill remaining space

### Step 16 — Configure the TableView columns

When you dropped the TableView, SceneBuilder automatically created two default columns. You need to configure them and add three more.

**Configure Column 1 (already exists):**
1. In the **Hierarchy** panel (bottom-left, Document section), expand `TableView` — you will see `TableColumn` and `TableColumn`
2. Click the **first** `TableColumn` to select it
3. In the **right panel**, **Properties** tab → **Text** field → type: `ID`
4. In the **right panel**, **Properties** tab → **Pref Width** field → type: `80`
5. In the **right panel**, **Code** tab → **fx:id** field → type: `colBookId` → press `Enter`

**Configure Column 2 (already exists):**
1. Click the **second** `TableColumn` in the Hierarchy to select it
2. In the **right panel**, **Properties** tab → **Text** field → type: `Title`
3. In the **right panel**, **Properties** tab → **Pref Width** field → type: `200`
4. In the **right panel**, **Code** tab → **fx:id** field → type: `colTitle` → press `Enter`

**Add Column 3 — Author:**
1. Right-click the **TableView** in the canvas (or right-click `TableView` in the Hierarchy panel)
2. In the context menu that appears, select **Add Column**
3. A new `TableColumn` appears — click it to select it
4. In the **right panel**, **Properties** tab → **Text** field → type: `Author`
5. In the **right panel**, **Properties** tab → **Pref Width** field → type: `150`
6. In the **right panel**, **Code** tab → **fx:id** field → type: `colAuthor` → press `Enter`

**Add Column 4 — Genre:**
1. Right-click the **TableView** again → **Add Column**
2. Click the new column to select it
3. **Properties** tab → **Text** → type: `Genre`
4. **Properties** tab → **Pref Width** → type: `100`
5. **Code** tab → **fx:id** → type: `colGenre` → press `Enter`

**Add Column 5 — Status:**
1. Right-click the **TableView** again → **Add Column**
2. Click the new column to select it
3. **Properties** tab → **Text** → type: `Status`
4. **Properties** tab → **Pref Width** → type: `90`
5. **Code** tab → **fx:id** → type: `colStatus` → press `Enter`

### Step 17 — Export the controller skeleton

SceneBuilder can generate a Java skeleton with all the `@FXML` annotations already filled in:

1. In the SceneBuilder **top menu bar**, click **View**
2. In the View dropdown menu, click **Show Sample Controller Skeleton**
3. A popup window appears showing auto-generated Java code — it lists every `@FXML` field and every empty handler method, matching all the `fx:id` values and `onAction` values you just set
4. Click the **Copy to Clipboard** button in that popup
5. Close the popup
6. Keep this code — you will paste it into `BookFXController.java` in Part 7

### Step 18 — Save the FXML file

1. In the SceneBuilder **top menu bar**, click **File**
2. Click **Save As...**
3. A file browser dialog opens — navigate to your project folder:
   - Go to: `src/resources/com/fad/LibrarySystem/view/`
4. In the **File name** field at the bottom, type: `BookTab.fxml`
5. Click **Save**

---

## PART 4 — SceneBuilder: Design `MemberTab.fxml`

### Step 19 — Create a new empty document

1. In SceneBuilder, go to the top menu: **File → New**
2. In the template dialog, select **Empty** (not any other template)
3. Click **OK** — the canvas clears and becomes blank

### Step 20 — Set the controller class

1. In the **bottom-left Document panel**, click the **Controller** sub-tab
2. In the **Controller class** field, type exactly:
   ```
   com.fad.LibrarySystem.controller.MemberFXController
   ```
3. Press `Enter`

### Step 21 — Build the layout

**Add AnchorPane (root):**
1. **Library** panel (left) → **Containers** category → find **AnchorPane**
2. Drag and drop **AnchorPane** onto the center canvas — it becomes the root

**Add VBox inside AnchorPane:**
1. **Library** panel → **Containers** → find **VBox**
2. Drag and drop **VBox** onto the **AnchorPane** in the canvas
3. Click **VBox** to select it
4. **Right panel → Layout** tab → **AnchorPane Constraints** section:
   - Top: `0`, Right: `0`, Bottom: `0`, Left: `0`

**Add HBox inside VBox (toolbar row):**
1. **Library** panel → **Containers** → find **HBox**
2. Drag and drop **HBox** onto the **VBox** in the canvas
3. Click **HBox** to select it
4. **Right panel → Properties** tab → **Spacing** field → type: `10`
5. **Right panel → Layout** tab → **Padding** section → set all sides to: `10`

**Add Register Member button inside HBox:**
1. **Library** panel → **Controls** → find **Button**
2. Drag and drop **Button** onto the **HBox**
3. Click the **Button** to select it
4. **Right panel → Properties** tab → **Text** field → type: `Register Member`
5. **Right panel → Code** tab → **On Action** field → type: `#handleRegister` → press `Enter`

**Add Remove Member button inside HBox:**
1. Drag another **Button** onto the **HBox** (to the right of Register Member)
2. Click the **Button** to select it
3. **Right panel → Properties** tab → **Text** field → type: `Remove Member`
4. **Right panel → Code** tab → **On Action** field → type: `#handleRemove` → press `Enter`

**Add TableView inside VBox (below HBox):**
1. **Library** panel → **Controls** → find **TableView**
2. Drag and drop **TableView** onto the **VBox**, below the HBox
3. Click **TableView** to select it
4. **Right panel → Code** tab → **fx:id** field → type: `memberTable` → press `Enter`
5. **Right panel → Layout** tab → **VBox Constraints** → **Vgrow** dropdown → select **ALWAYS**

**Configure the two default columns:**

Column 1 — Member ID:
1. In the **Hierarchy** panel, expand `TableView` → click the **first** `TableColumn`
2. **Properties** tab → **Text** → type: `Member ID`
3. **Properties** tab → **Pref Width** → type: `120`
4. **Code** tab → **fx:id** → type: `colMemberId` → press `Enter`

Column 2 — Name:
1. Click the **second** `TableColumn` in the Hierarchy
2. **Properties** tab → **Text** → type: `Name`
3. **Properties** tab → **Pref Width** → type: `200`
4. **Code** tab → **fx:id** → type: `colName` → press `Enter`

**Add Column 3 — Items Borrowed:**
1. Right-click the **TableView** on the canvas → **Add Column**
2. Click the new column to select it
3. **Properties** tab → **Text** → type: `Items Borrowed`
4. **Properties** tab → **Pref Width** → type: `120`
5. **Code** tab → **fx:id** → type: `colBorrowCount` → press `Enter`

### Step 22 — Export skeleton and save

1. Top menu → **View** → **Show Sample Controller Skeleton** → click **Copy to Clipboard** → close popup
2. Top menu → **File** → **Save As...**
3. Navigate to: `src/resources/com/fad/LibrarySystem/view/`
4. File name: `MemberTab.fxml` → click **Save**

---

## PART 5 — SceneBuilder: Design `BorrowTab.fxml`

### Step 23 — Create a new empty document

1. Top menu → **File → New**
2. In the template dialog, select **Empty** → click **OK**

### Step 24 — Set the controller class

1. **Bottom-left Document panel** → **Controller** sub-tab
2. **Controller class** field → type exactly:
   ```
   com.fad.LibrarySystem.controller.BorrowFXController
   ```
3. Press `Enter`

### Step 25 — Build the layout

**Add AnchorPane (root):**
1. **Library** panel → **Containers** → **AnchorPane** → drag onto canvas

**Add VBox inside AnchorPane:**
1. **Library** panel → **Containers** → **VBox** → drag onto **AnchorPane**
2. Click **VBox** to select it
3. **Right panel → Layout** tab → **AnchorPane Constraints**: Top `0`, Right `0`, Bottom `0`, Left `0`
4. **Right panel → Properties** tab → **Spacing** field → type: `8`

**Add HBox inside VBox (the form row):**
1. **Library** panel → **Containers** → **HBox** → drag onto **VBox**
2. Click **HBox** to select it
3. **Right panel → Properties** tab → **Spacing** field → type: `10`
4. **Right panel → Layout** tab → **Padding**: all sides → `10`

**Add components inside the HBox — in this order:**

Label "Member ID:":
1. **Library** panel → **Controls** → **Label** → drag onto **HBox**
2. Click the **Label** to select it
3. **Right panel → Properties** tab → **Text** field → type: `Member ID:`

TextField for member ID:
1. **Library** panel → **Controls** → **TextField** → drag onto **HBox** (to the right of the label)
2. Click **TextField** to select it
3. **Right panel → Properties** tab → **Prompt Text** field → type: `e.g. M001`
4. **Right panel → Code** tab → **fx:id** field → type: `memberIdField` → press `Enter`

Label "Item ID:":
1. **Library** panel → **Controls** → **Label** → drag onto **HBox**
2. **Right panel → Properties** tab → **Text** field → type: `Item ID:`

TextField for item ID:
1. **Library** panel → **Controls** → **TextField** → drag onto **HBox**
2. **Right panel → Properties** tab → **Prompt Text** field → type: `e.g. B001`
3. **Right panel → Code** tab → **fx:id** field → type: `itemIdField` → press `Enter`

Button "Borrow":
1. **Library** panel → **Controls** → **Button** → drag onto **HBox**
2. **Right panel → Properties** tab → **Text** field → type: `Borrow`
3. **Right panel → Code** tab → **On Action** field → type: `#handleBorrow` → press `Enter`

Button "Return":
1. **Library** panel → **Controls** → **Button** → drag onto **HBox**
2. **Right panel → Properties** tab → **Text** field → type: `Return`
3. **Right panel → Code** tab → **On Action** field → type: `#handleReturn` → press `Enter`

**Add a status Label inside VBox (below the HBox):**
1. **Library** panel → **Controls** → **Label** → drag onto **VBox** (below the HBox, not inside it — watch the Hierarchy panel to confirm it appears as a sibling of HBox inside VBox)
2. Click the **Label** to select it
3. **Right panel → Properties** tab → **Text** field → clear it completely (leave it empty)
4. **Right panel → Code** tab → **fx:id** field → type: `statusLabel` → press `Enter`
5. **Right panel → Layout** tab → **Padding** section → **Left** field → type: `10`

**Add TableView inside VBox (below the status Label):**
1. **Library** panel → **Controls** → **TableView** → drag onto **VBox** (below `statusLabel` in the hierarchy)
2. Click **TableView** to select it
3. **Right panel → Code** tab → **fx:id** field → type: `recordTable` → press `Enter`
4. **Right panel → Layout** tab → **VBox Constraints** → **Vgrow** dropdown → select **ALWAYS**

**Configure the two default columns:**

Column 1 — Record ID:
1. In **Hierarchy** panel, expand `TableView` → click **first** `TableColumn`
2. **Properties** tab → **Text** → `Record ID`
3. **Properties** tab → **Pref Width** → `120`
4. **Code** tab → **fx:id** → `colRecordId` → press `Enter`

Column 2 — Member:
1. Click **second** `TableColumn` in Hierarchy
2. **Properties** tab → **Text** → `Member`
3. **Properties** tab → **Pref Width** → `120`
4. **Code** tab → **fx:id** → `colMember` → press `Enter`

**Add 4 more columns** (right-click **TableView** on canvas → **Add Column** each time):

Column 3 — Item:
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Item` | **Pref Width** → `180`
3. **Code** → **fx:id** → `colItem` → press `Enter`

Column 4 — Borrow Date:
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Borrow Date` | **Pref Width** → `110`
3. **Code** → **fx:id** → `colBorrowDate` → press `Enter`

Column 5 — Return Date:
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Return Date` | **Pref Width** → `110`
3. **Code** → **fx:id** → `colReturnDate` → press `Enter`

Column 6 — Status:
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Status` | **Pref Width** → `80`
3. **Code** → **fx:id** → `colReturnStatus` → press `Enter`

### Step 26 — Export skeleton and save

1. Top menu → **View** → **Show Sample Controller Skeleton** → click **Copy to Clipboard** → close popup
2. Top menu → **File** → **Save As...**
3. Navigate to: `src/resources/com/fad/LibrarySystem/view/`
4. File name: `BorrowTab.fxml` → click **Save**

---

## PART 6 — SceneBuilder: Design `FineTab.fxml`

### Step 27 — Create a new empty document

1. Top menu → **File → New**
2. In the template dialog, select **Empty** → click **OK**

### Step 28 — Set the controller class

1. **Bottom-left Document panel** → **Controller** sub-tab
2. **Controller class** field → type exactly:
   ```
   com.fad.LibrarySystem.controller.FineFXController
   ```
3. Press `Enter`

### Step 29 — Build the layout

This tab uses a VBox directly as the root (no AnchorPane needed):

**Add VBox as the root:**
1. **Library** panel → **Containers** → **VBox** → drag onto the center canvas
2. Click **VBox** to select it
3. **Right panel → Layout** tab:
   - **Pref Width** field → type: `980`
   - **Pref Height** field → type: `600`

**Add TableView inside VBox:**
1. **Library** panel → **Controls** → **TableView** → drag onto **VBox**
2. Click **TableView** to select it
3. **Right panel → Code** tab → **fx:id** field → type: `fineTable` → press `Enter`
4. **Right panel → Layout** tab → **VBox Constraints** → **Vgrow** dropdown → select **ALWAYS**

**Configure the two default columns:**

Column 1 — Fine ID:
1. In **Hierarchy** panel, expand `TableView` → click **first** `TableColumn`
2. **Properties** tab → **Text** → `Fine ID`
3. **Properties** tab → **Pref Width** → `140`
4. **Code** tab → **fx:id** → `colFineId` → press `Enter`

Column 2 — Member:
1. Click **second** `TableColumn` in Hierarchy
2. **Properties** tab → **Text** → `Member`
3. **Properties** tab → **Pref Width** → `150`
4. **Code** tab → **fx:id** → `colFineMember` → press `Enter`

**Add 3 more columns** (right-click **TableView** → **Add Column** each time):

Column 3 — Item:
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Item` | **Pref Width** → `200`
3. **Code** → **fx:id** → `colFineItem` → press `Enter`

Column 4 — Days Late:
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Days Late` | **Pref Width** → `100`
3. **Code** → **fx:id** → `colDaysLate` → press `Enter`

Column 5 — Amount (Rp):
1. Right-click **TableView** → **Add Column** → click the new column
2. **Properties** → **Text** → `Amount (Rp)` | **Pref Width** → `120`
3. **Code** → **fx:id** → `colAmount` → press `Enter`

### Step 30 — Export skeleton and save

1. Top menu → **View** → **Show Sample Controller Skeleton** → click **Copy to Clipboard** → close popup
2. Top menu → **File** → **Save As...**
3. Navigate to: `src/resources/com/fad/LibrarySystem/view/`
4. File name: `FineTab.fxml` → click **Save**

---

## PART 7 — Write the Java Controllers

For each controller, create a new Java file in `src/com/fad/LibrarySystem/controller/`.

> The `@FXML` field names must exactly match the `fx:id` values you set in SceneBuilder.

### Step 31 — Create `BookFXController.java`

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

### Step 32 — Create `MemberFXController.java`

```java
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.model.Member;
import javafx.beans.property.SimpleIntegerProperty;
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

### Step 33 — Create `BorrowFXController.java`

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

### Step 34 — Create `FineFXController.java`

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

### Step 35 — Create `App.java`

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

### Step 36 — Run via Maven

**Option A — Terminal** (fastest):

Open the VS Code terminal (`Ctrl+` `` ` ``) and run:

```bash
mvn javafx:run
```

**Option B — Maven sidebar**:

1. Click the **Maven** icon in the left sidebar (the `M` icon)
2. In the Maven panel that opens, expand your project name
3. Expand **Plugins**
4. Expand **javafx**
5. Double-click **javafx:run** (or click the ▶ play button next to it)

**Option C — VS Code run button** (requires Extension Pack for Java):

1. Open `App.java` in the editor
2. Scroll to the `main` method at the bottom of the file
3. Click the **▶ Run** button that appears directly above the `main` method
4. If it fails with "JavaFX runtime components are missing", use Option A instead — the Maven plugin handles the module path automatically

### Step 37 — Verify each tab works

| Tab | What to test |
|---|---|
| Books | Table loads 5 seeded books. Add a new book. Delete one that is available. Try deleting a borrowed one — should show warning. |
| Members | Table loads Alice, Bob, Charlie. Register a new member. |
| Borrow/Return | Type `M001` and `B001` → click Borrow. Status label turns green. Row appears in table. Click Return with same IDs — status updates. |
| Fines | Empty at first. (Fines are created via `service.recordFine()` — you can test via code or add a button later.) |

---

## PART 10 — Critical Rules for SceneBuilder ↔ Java

### The `fx:id` ↔ `@FXML` contract

Every `fx:id` set in SceneBuilder (in the **Code** tab of the Inspector panel) **must exactly match** a `@FXML` field name in the controller — same spelling, same capitalisation:

```
SceneBuilder (Code tab)               Java Controller
────────────────────────────────      ────────────────────────────────────
fx:id="bookTable"           →         @FXML private TableView<Book> bookTable;
fx:id="genreSearchField"    →         @FXML private TextField genreSearchField;
fx:id="colTitle"            →         @FXML private TableColumn<Book,String> colTitle;
```

### The `onAction` ↔ method contract

Every `On Action` value set in SceneBuilder (in the **Code** tab) **must exactly match** a `@FXML` method in the controller — including the `#` prefix in SceneBuilder, but without it in Java:

```
SceneBuilder (Code tab)               Java Controller
────────────────────────────────      ────────────────────────────────────
On Action: #handleAddBook   →         @FXML private void handleAddBook() { }
On Action: #handleBorrow    →         @FXML private void handleBorrow() { }
```

### The controller class contract

The controller class name you typed in SceneBuilder's **Document → Controller** panel is saved into the FXML file as the `fx:controller` attribute. It **must match** the fully-qualified Java class name exactly:

```xml
<!-- What SceneBuilder saves into BookTab.fxml -->
<AnchorPane fx:controller="com.fad.LibrarySystem.controller.BookFXController" ...>
```

### `initialize()` vs `setService()`

- `initialize()` runs **automatically** when FXML is loaded — the `service` field is still `null` at this point, so only set up column bindings here, not data loading.
- `setService()` is called **by `App.java`** after loading, so load table data there.

---

## PART 11 — Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| `NullPointerException` on `@FXML` field | `fx:id` in FXML does not match field name in controller | In SceneBuilder, select the component, go to **Code** tab on the right, verify the `fx:id` value matches exactly |
| `Location is not set` on FXMLLoader | Wrong resource path | Verify the path in `App.java` starts with `/` and matches the exact folder structure in `src/resources/` |
| `IllegalAccessException` on controller | Controller class or method is not public | Make sure the controller class declaration says `public class BookFXController` |
| `ClassNotFoundException` for controller | Typo in the controller class field in SceneBuilder | In SceneBuilder, go to **Document panel → Controller tab**, re-check the class name for typos |
| Table shows blank rows | `PropertyValueFactory` string does not match getter name | `new PropertyValueFactory<>("bookId")` requires a `getBookId()` method on the model class |
| JavaFX classes not found at runtime | Missing JavaFX dependency in Maven | Re-run `mvn dependency:resolve`, check `pom.xml` has both `javafx-controls` and `javafx-fxml` |

---

## Quick Reference: SceneBuilder Workflow Per Screen

```
For each new screen:
  1. SceneBuilder → File → New → choose Empty (not any template) → OK
  2. Bottom-left Document panel → Controller tab → type the full class name
  3. Library panel (left) → drag Containers (VBox, HBox, AnchorPane) onto canvas
  4. Library panel (left) → drag Controls (TableView, TextField, Button, Label) into containers
  5. For each control you need in Java → select it → right Inspector panel → Code tab → set fx:id
  6. For each button → right Inspector panel → Code tab → set On Action (e.g. #handleAddBook)
  7. For each column text and size → right Inspector panel → Properties tab → Text and Pref Width
  8. For AnchorPane constraints and VBox vgrow → right Inspector panel → Layout tab
  9. Top menu → View → Show Sample Controller Skeleton → Copy to Clipboard
 10. Top menu → File → Save As → navigate to src/resources/com/fad/LibrarySystem/view/ → save as XxxTab.fxml
 11. In VS Code → create XxxFXController.java → paste skeleton → implement the handler methods
```
