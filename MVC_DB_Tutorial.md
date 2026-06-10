# Library Borrowing System — Full Tutorial
### MVC Architecture + SQLite Database with Java & Maven

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Prerequisites](#2-prerequisites)
3. [Installing Java JDK](#3-installing-java-jdk)
4. [Installing Maven](#4-installing-maven)
5. [Installing VS Code & Java Extensions](#5-installing-vs-code--java-extensions)
6. [Getting the Project](#6-getting-the-project)
7. [Project Structure](#7-project-structure)
8. [Understanding MVC Pattern](#8-understanding-mvc-pattern)
9. [Understanding the Database Layer](#9-understanding-the-database-layer)
10. [Code Walkthrough](#10-code-walkthrough)
11. [How Data Flows](#11-how-data-flows)
12. [Running the Project](#12-running-the-project)
13. [Troubleshooting](#13-troubleshooting)

---

## 1. Introduction

This project is a **console-based Library Borrowing System** written in Java.  
It demonstrates two important concepts:

| Concept | What it means in this project |
|---------|-------------------------------|
| **MVC (Model-View-Controller)** | Code is split into 3 clear layers — data, display, and logic |
| **SQLite Database** | Data is saved to a real database file so it survives when the app restarts |

Before this version, all data was stored in **arrays in memory** — meaning every time you closed the app, all books, members, and borrow records were lost. Now they are saved to a file called `library.db`.

---

## 2. Prerequisites

Before you start, make sure you have:

- [ ] Java JDK 17 or higher
- [ ] Apache Maven 3.6 or higher
- [ ] Visual Studio Code
- [ ] VS Code Java Extension Pack
- [ ] Git (to clone the project)

---

## 3. Installing Maven

Maven is a **build tool** for Java. It handles downloading libraries (like the SQLite driver) automatically so you never need to download `.jar` files manually.

### Windows

**Step 1** — Download Maven  
Go to [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) and download the **Binary zip archive** (e.g., `apache-maven-3.9.x-bin.zip`)

**Step 2** — Extract it  
Extract the zip to a folder, for example:
```
C:\Program Files\Apache\maven
```

**Step 3** — Set Environment Variables  
1. Press `Win + S`, search for **"Environment Variables"**, click **"Edit the system environment variables"**
2. Click **"Environment Variables..."**
3. Under **System variables**, click **New**:
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven`
4. Find the **`Path`** variable, click **Edit**, then **New**, and add:
   ```
   C:\Program Files\Apache\maven\bin
   ```
5. Click **OK** on all windows

**Step 4** — Verify in a new Command Prompt window:

```cmd
mvn -version
```

Expected output:
```
Apache Maven 3.9.x ...
Java version: 21.x.x ...
```

---

### macOS

```bash
brew install maven
```

Verify:

```bash
mvn -version
```

---

### Linux (Ubuntu / Debian)

```bash
sudo apt update
sudo apt install maven -y
```

Verify:

```bash
mvn -version
```

### Linux (Fedora / RHEL / CentOS)

```bash
sudo dnf install maven -y
```

Verify:

```bash
mvn -version
```

> **Important:** Make sure the `Java version` shown in `mvn -version` is **17 or higher**. If it shows an older version, your `JAVA_HOME` environment variable may be pointing to an old JDK.

---

## 4. Installing VS Code & Java Extensions

**Step 1** — Download VS Code from [https://code.visualstudio.com](https://code.visualstudio.com) and install it.

**Step 2** — Open VS Code, press `Ctrl+Shift+X` (Windows/Linux) or `Cmd+Shift+X` (macOS) to open Extensions.

**Step 3** — Search for and install **"Extension Pack for Java"** by Microsoft.  
This single pack includes everything you need:
- Language Support for Java
- Maven for Java
- Java Debugger
- Java Test Runner

**Step 4** — Restart VS Code after installation.

---

## 5. Getting the Project

### Clone via Git

```bash
git clone https://github.com/masjohncook/LibraryBorrowingSystem.git
cd LibraryBorrowingSystem
```

### Switch to the database branch

```bash
git checkout m_data
```

### Open in VS Code

```bash
code .
```

VS Code will detect the `pom.xml` automatically and recognize it as a Maven project. It may prompt you to **"Import Java Projects"** — click **Yes**.

---

## 6. Project Structure

```
LibraryBorrowingSystem/
│
├── pom.xml                          ← Maven config (declares SQLite dependency)
├── library.db                       ← SQLite database (auto-created on first run)
│
└── src/
    └── com/fad/LibrarySystem/
        │
        ├── Main.java                ← Entry point
        │
        ├── model/                   ← M: Data classes & business logic
        │   ├── Person.java
        │   ├── LibraryItem.java
        │   ├── Books.java
        │   ├── Multimedia.java
        │   ├── Member.java
        │   ├── Librarian.java       ← Central data hub (uses DatabaseManager)
        │   ├── BorrowRecord.java
        │   ├── Reservation.java
        │   └── Fine.java
        │
        ├── view/                    ← V: Display only (System.out.println)
        │   ├── MenuView.java
        │   ├── BookView.java
        │   ├── MemberView.java
        │   ├── BorrowView.java
        │   └── FineView.java
        │
        ├── controller/              ← C: User input & coordinates Model + View
        │   ├── LibraryController.java
        │   ├── BookController.java
        │   ├── MemberController.java
        │   └── BorrowController.java
        │
        └── database/                ← DB: All SQL operations
            └── DatabaseManager.java
```

---

## 7. Understanding MVC Pattern

MVC stands for **Model — View — Controller**. It is a way of organizing code so that each layer has exactly one job.

```
User Input
    │
    ▼
┌─────────────┐     calls      ┌─────────────┐     reads/writes    ┌─────────────┐
│  Controller │ ─────────────► │    Model    │ ──────────────────► │  Database   │
│             │                │             │                      │ (SQLite)    │
│ Reads input │ ◄───────────── │ Returns data│ ◄────────────────── │             │
└─────────────┘     data       └─────────────┘     loads data       └─────────────┘
       │
       │ passes data to
       ▼
┌─────────────┐
│    View     │
│             │
│  Displays   │
│  to screen  │
└─────────────┘
```

### Rule of MVC

| Layer | Allowed to | NOT allowed to |
|-------|-----------|----------------|
| **Model** | Hold data, business rules, talk to DB | Print to screen |
| **View** | Print to screen | Make business decisions |
| **Controller** | Read input, call Model, pass result to View | Hold data directly |

### Example in this project

When a member borrows a book:

```
BorrowController        Member (Model)          DatabaseManager
      │                      │                        │
      │── reads input ──────►│                        │
      │    (memberId,        │                        │
      │     itemId)          │                        │
      │                      │                        │
      │── member.borrowItem()►│                       │
      │                      │── item.setAvailable(false)
      │                      │                        │
      │── librarian           │                       │
      │   .recordBorrow() ──►│── INSERT borrow_record►│
      │                      │── INSERT member_borrowed►│
      │                      │── UPDATE item available►│
      │                      │                        │
      │── borrowView          │                       │
      │   .showSuccess() ────►(prints to screen)
```

---

## 8. Understanding the Database Layer

### Why SQLite?

SQLite is a **file-based** database — the entire database is stored in a single file (`library.db`). It requires no separate server installation, making it perfect for learning and small applications.

### The `pom.xml` Dependency

Open `pom.xml`. This is where Maven is told which libraries to download:

```xml
<dependencies>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.46.1.3</version>
    </dependency>
</dependencies>
```

When you run `mvn compile`, Maven automatically downloads `sqlite-jdbc-3.46.1.3.jar` from the internet and includes it in the build. You never need to download it manually.

### Database Schema (Tables)

The database has **6 tables**:

```sql
-- Stores all books
books (
    book_id   TEXT PRIMARY KEY,
    title     TEXT,
    author    TEXT,
    genre     TEXT,
    available INTEGER   -- 1 = available, 0 = borrowed
)

-- Stores all multimedia items (DVD, CD, Audiobook)
multimedia (
    item_id   TEXT PRIMARY KEY,
    title     TEXT,
    type      TEXT,
    duration  TEXT,
    available INTEGER
)

-- Stores all registered members
members (
    member_id TEXT PRIMARY KEY,
    name      TEXT
)

-- Tracks which items each member currently has borrowed
member_borrowed_items (
    member_id TEXT,
    item_id   TEXT,
    PRIMARY KEY (member_id, item_id)
)

-- Full history of all borrow and return transactions
borrow_records (
    record_id   TEXT PRIMARY KEY,
    member_id   TEXT,
    item_id     TEXT,
    borrow_date TEXT,
    return_date TEXT,   -- '-' if not yet returned
    returned    INTEGER -- 0 = not returned, 1 = returned
)

-- Reservations placed by members for unavailable items
reservations (
    reservation_id   TEXT PRIMARY KEY,
    member_id        TEXT,
    item_id          TEXT,
    reservation_date TEXT,
    active           INTEGER -- 1 = active, 0 = cancelled
)
```

### How the DB file is created

You do **not** need to create `library.db` manually. When the app starts for the first time, `DatabaseManager.initSchema()` runs and creates the file and all tables automatically.

If the `books` table is empty (first run), the app seeds the database with 5 books, 3 multimedia items, and 3 members.

---

## 9. Code Walkthrough

### `Main.java` — Entry Point

```java
public class Main {
    public static void main(String[] args) {
        // Register a shutdown hook so the DB connection closes cleanly
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));

        // Start the application
        LibraryController controller = new LibraryController();
        controller.start();
    }
}
```

`addShutdownHook` ensures the database connection is properly closed whenever the program exits — whether normally (choosing option 0) or forcefully (Ctrl+C).

---

### `DatabaseManager.java` — All SQL in One Place

This class handles every interaction with the database. No other class writes SQL directly.

**Getting a connection:**
```java
public static Connection getConnection() {
    if (conn == null || conn.isClosed()) {
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");
        // Enable WAL mode for better performance
        // Enable foreign keys for data integrity
    }
    return conn;
}
```

**Inserting a book:**
```java
public static void insertBook(Books book) {
    String sql = "INSERT OR IGNORE INTO books " +
                 "(book_id, title, author, genre, available) VALUES (?,?,?,?,?)";
    PreparedStatement ps = getConnection().prepareStatement(sql);
    ps.setString(1, book.getBookId());
    ps.setString(2, book.getTitle());
    // ...
    ps.executeUpdate();
}
```

> **Why `?` placeholders?**  
> Using `?` instead of putting values directly in the string prevents **SQL Injection** — a common security vulnerability. Never concatenate user input directly into SQL strings.

**Checking if the table has data (to decide whether to seed):**
```java
public static boolean isTableEmpty(String table) {
    ResultSet rs = getConnection().createStatement()
                       .executeQuery("SELECT COUNT(*) FROM " + table);
    return !rs.next() || rs.getInt(1) == 0;
}
```

---

### `Librarian.java` — Model & Central Data Hub

`Librarian` is the most important class. It holds all data in memory (using arrays) AND keeps the database in sync.

**Startup — load from DB or seed:**
```java
public Librarian(String librarianId, String name) {
    super(librarianId, name);
    // ... initialize arrays ...
    DatabaseManager.initSchema();   // Create tables if they don't exist
    loadFromDatabase();             // Load existing data (or seed if empty)
}

private void loadFromDatabase() {
    if (DatabaseManager.isTableEmpty("books")) seedInitialData();

    // Load books from DB into the in-memory catalog[] array
    for (Books b : DatabaseManager.loadBooks())
        catalog[catalogCount++] = b;

    // Load members and restore which items they have borrowed
    for (Member m : DatabaseManager.loadMembers()) {
        members[memberCount++] = m;
        for (String itemId : DatabaseManager.loadBorrowedItemIds(m.getMemberId())) {
            LibraryItem item = findItemById(itemId);
            if (item != null) m.addBorrowedItem(item); // restore state
        }
    }
    // ... load multimedia, borrow records, reservations ...
}
```

**Adding a book — writes to memory AND database:**
```java
public Books addBook(String bookId, String title, String author, String genre) {
    if (catalogCount >= MAX_BOOKS)    return null;  // array full
    if (findBookById(bookId) != null) return null;  // duplicate ID
    Books book = new Books(bookId, title, author, genre);
    catalog[catalogCount++] = book;           // 1. add to memory
    DatabaseManager.insertBook(book);         // 2. save to DB
    return book;
}
```

**Recording a borrow — syncs 3 tables:**
```java
public void recordBorrow(Member member, LibraryItem item) {
    String today    = LocalDate.now().toString();
    String recordId = "REC" + String.format("%03d", recordCount + 1);
    BorrowRecord record = new BorrowRecord(recordId, member, item, today);
    borrowRecords[recordCount++] = record;

    DatabaseManager.insertBorrowRecord(record);                          // borrow_records table
    DatabaseManager.insertMemberBorrowedItem(member.getMemberId(),       // member_borrowed_items
                                              item.getItemId());
    DatabaseManager.updateItemAvailability(item.getItemId(), false);     // books / multimedia table
}
```

---

### Model Classes

**`Person.java`** — base class for Librarian and Member

```
Person
  ├── id   (String)
  └── name (String)
```

**`LibraryItem.java`** — base class for Books and Multimedia

```
LibraryItem
  ├── itemId    (String)
  ├── title     (String)
  └── available (boolean)
```

**`Books.java`** — extends LibraryItem

```
Books extends LibraryItem
  ├── author (String)
  └── genre  (String)
```

**`Member.java`** — extends Person

```
Member extends Person
  ├── borrowedItems[] (LibraryItem array, max 5)
  └── borrowCount     (int)
```

Key methods:
- `borrowItem(item)` — checks availability and limit, then marks item as borrowed
- `returnItem(item)` — removes item from the member's list, marks it available
- `addBorrowedItem(item)` — used **only during DB loading** to restore state without checks

---

### View Classes

Views only print to the screen. They receive data from the controller and display it.

```java
// BorrowView.java
public void showBorrowSuccess(String memberName, String itemTitle) {
    System.out.println(memberName + " successfully borrowed: " + itemTitle);
}

public void showAllRecords(List<BorrowRecord> records) {
    for (BorrowRecord r : records) {
        System.out.println(r.getInfo());
    }
}
```

Views never touch the database or make decisions.

---

### Controller Classes

**`LibraryController.java`** — creates all objects and runs the main menu loop:

```java
public LibraryController() {
    this.librarian       = new Librarian("L001", "Admin Librarian"); // loads DB
    this.bookController  = new BookController(librarian, scanner);
    this.memberController= new MemberController(librarian, scanner);
    this.borrowController= new BorrowController(librarian, scanner);
}

public void start() {
    while (choice != 0) {
        menuView.showMainMenu();           // View: show options
        choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1 -> bookController.handleMenu();
            case 2 -> memberController.handleMenu();
            case 3 -> borrowController.handleMenu();
        }
    }
}
```

**`BorrowController.java`** — handles borrow and return:

```java
private void borrowItem() {
    System.out.print("Member ID : "); String memberId = scanner.nextLine();
    System.out.print("Item ID   : "); String itemId   = scanner.nextLine();

    Member      member = librarian.findMemberById(memberId);  // Model lookup
    LibraryItem item   = librarian.findItemById(itemId);      // Model lookup

    boolean success = member.borrowItem(item);                // Model logic
    if (success) {
        librarian.recordBorrow(member, item);                 // Model + DB sync
        borrowView.showBorrowSuccess(member.getName(),        // View
                                     item.getTitle());
    } else {
        borrowView.showBorrowLimitReached();                  // View
    }
}
```

---

## 10. How Data Flows

### First Run (empty database)

```
App starts
    │
    ▼
DatabaseManager.initSchema()
    └── Creates: books, multimedia, members,
                 member_borrowed_items, borrow_records, reservations tables
    │
    ▼
isTableEmpty("books") == true
    │
    ▼
seedInitialData()
    ├── INSERT 5 books   → books table
    ├── INSERT 3 multimedia → multimedia table
    └── INSERT 3 members → members table
    │
    ▼
loadFromDatabase()
    └── Reads all data from DB into in-memory arrays
```

### Subsequent Runs

```
App starts
    │
    ▼
loadFromDatabase()
    └── isTableEmpty("books") == false → skip seeding
    └── Loads all saved data from DB into arrays
        (books, multimedia, members, borrowed items, borrow records)
```

### Borrowing a Book

```
User enters Member ID and Item ID
    │
    ▼
BorrowController.borrowItem()
    │
    ├── librarian.findMemberById() → searches in-memory members[] array
    ├── librarian.findItemById()   → searches in-memory catalog[] array
    │
    ├── member.borrowItem(item)
    │       ├── item.setAvailable(false)  ← in-memory change
    │       └── adds item to borrowedItems[]
    │
    └── librarian.recordBorrow(member, item)
            ├── creates BorrowRecord in borrowRecords[] ← in-memory
            ├── DatabaseManager.insertBorrowRecord()    ← saved to DB
            ├── DatabaseManager.insertMemberBorrowedItem() ← saved to DB
            └── DatabaseManager.updateItemAvailability() ← saved to DB
```

---

## 11. Running the Project

### Compile the project

```bash
mvn compile
```

This downloads the SQLite JDBC driver (first time only) and compiles all `.java` files to `target/classes/`.

### Run the project

```bash
mvn exec:java -Dexec.mainClass="com.fad.LibrarySystem.Main"
```

You should see:

```
===== Library System =====
1. Manage Books
2. Manage Members
3. Borrow / Return Item
4. Reservations
5. Fines
0. Exit
Choose an option:
```

### Run directly from VS Code

1. Open `src/com/fad/LibrarySystem/Main.java`
2. Click the **Run** button (▷) that appears above `public static void main`
3. The terminal at the bottom will open and run the app

### Verify the database

After running the app, a file named `library.db` will appear in the project root.  
You can inspect it using the **SQLite Viewer** extension in VS Code, or on the command line:

```bash
# List all tables
sqlite3 library.db ".tables"

# View all books
sqlite3 library.db "SELECT * FROM books;"

# View all members
sqlite3 library.db "SELECT * FROM members;"

# View borrow records
sqlite3 library.db "SELECT * FROM borrow_records;"
```

### Test that data persists

1. Run the app → go to **Manage Books → Add Book** → add a new book
2. Exit the app (choose option `0`)
3. Run the app again
4. Go to **Manage Books → View All Books**
5. Your new book should still be there ✓

---

## 12. Troubleshooting

### `mvn: command not found`
Maven is not in your PATH. Re-read [Section 4](#4-installing-maven) and make sure you added the `bin` folder to your system PATH, then open a **new** terminal window.

---

### `java: command not found`
Java is not installed or not in PATH. Re-read [Section 3](#3-installing-java-jdk).

---

### `BUILD FAILURE` — `Source option X is no longer supported`
Your Java version is too old. Run `java -version` — it must be **17 or higher**.

---

### `No suitable driver found for jdbc:sqlite`
The SQLite JDBC driver was not downloaded. Run:
```bash
mvn dependency:resolve
```
If Maven cannot connect to the internet, check your network/firewall settings.

---

### `library.db` file not found after running
The database file is created in whichever directory you run the `mvn` command from. Always run Maven commands from the **project root** (where `pom.xml` is located).

---

### VS Code doesn't recognize Java / Maven project
1. Make sure the **Extension Pack for Java** is installed
2. Press `Ctrl+Shift+P` → type **"Java: Clean Java Language Server Workspace"** → Restart
3. VS Code should re-detect the Maven project automatically

---

### Data is not saving between runs
Make sure you exit the app by choosing option **`0`** (not by closing the terminal forcefully with `Ctrl+C`). If you use `Ctrl+C`, the shutdown hook still runs and the DB closes safely, but it is best practice to exit cleanly.

---

*Tutorial written for students learning Java MVC architecture and SQLite persistence.*
