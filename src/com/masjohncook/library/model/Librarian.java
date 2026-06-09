package com.masjohncook.library.model;

public class Librarian extends Person {

    private Books[]       catalog;
    private int           catalogCount;
    private Multimedia[]  multimedia;
    private int           multimediaCount;
    private Member[]      members;
    private int           memberCount;
    private BorrowRecord[] borrowRecords;
    private int           recordCount;
    private Reservation[] reservations;
    private int           reservationCount;
    private int           fineCount;

    private static final int MAX_BOOKS        = 100;
    private static final int MAX_MULTIMEDIA   = 50;
    private static final int MAX_MEMBERS      = 50;
    private static final int MAX_RECORDS      = 200;
    private static final int MAX_RESERVATIONS = 100;

    public Librarian(String librarianId, String name) {
        super(librarianId, name);
        this.catalog          = new Books[MAX_BOOKS];
        this.catalogCount     = 0;
        this.multimedia       = new Multimedia[MAX_MULTIMEDIA];
        this.multimediaCount  = 0;
        this.members          = new Member[MAX_MEMBERS];
        this.memberCount      = 0;
        this.borrowRecords    = new BorrowRecord[MAX_RECORDS];
        this.recordCount      = 0;
        this.reservations     = new Reservation[MAX_RESERVATIONS];
        this.reservationCount = 0;
        this.fineCount        = 0;
        loadInitialData();
    }

    private void loadInitialData() {
        for (Books b : Books.getInitialBooks())           catalog[catalogCount++]      = b;
        for (Multimedia m : Multimedia.getInitialMultimedia()) multimedia[multimediaCount++] = m;
        for (Member m : Member.getInitialMembers())       members[memberCount++]       = m;
    }

    // ── Books ─────────────────────────────────────────────────────────────────

    public Books addBook(String bookId, String title, String author, String genre) {
        if (catalogCount >= MAX_BOOKS)         return null;
        if (findBookById(bookId) != null)      return null;
        Books book = new Books(bookId, title, author, genre);
        catalog[catalogCount++] = book;
        return book;
    }

    public Books addBook(String bookId, String title, String author) {
        return addBook(bookId, title, author, "General");
    }

    public boolean removeBook(String bookId) {
        for (int i = 0; i < catalogCount; i++) {
            if (catalog[i].getBookId().equals(bookId)) {
                if (!catalog[i].isAvailable()) return false;
                for (int j = i; j < catalogCount - 1; j++) catalog[j] = catalog[j + 1];
                catalog[--catalogCount] = null;
                return true;
            }
        }
        return false;
    }

    public boolean updateBook(String bookId, String newTitle, String newAuthor, String newGenre) {
        Books book = findBookById(bookId);
        if (book == null) return false;
        if (!newTitle.trim().isEmpty())  book.setTitle(newTitle.trim());
        if (!newAuthor.trim().isEmpty()) book.setAuthor(newAuthor.trim());
        if (!newGenre.trim().isEmpty())  book.setGenre(newGenre.trim());
        return true;
    }

    public Books findBookById(String bookId) {
        for (int i = 0; i < catalogCount; i++)
            if (catalog[i].getBookId().equals(bookId)) return catalog[i];
        return null;
    }

    // ── Multimedia ────────────────────────────────────────────────────────────

    public Multimedia addMultimedia(String itemId, String title, String type, String duration) {
        if (multimediaCount >= MAX_MULTIMEDIA)    return null;
        if (findMultimediaById(itemId) != null)   return null;
        Multimedia item = new Multimedia(itemId, title, type, duration);
        multimedia[multimediaCount++] = item;
        return item;
    }

    public Multimedia addMultimedia(String itemId, String title, String type) {
        return addMultimedia(itemId, title, type, "Unknown");
    }

    public Multimedia findMultimediaById(String itemId) {
        for (int i = 0; i < multimediaCount; i++)
            if (multimedia[i].getItemId().equals(itemId)) return multimedia[i];
        return null;
    }

    // ── Members ───────────────────────────────────────────────────────────────

    public Member registerMember(String memberId, String name) {
        if (memberCount >= MAX_MEMBERS)          return null;
        if (findMemberById(memberId) != null)    return null;
        Member member = new Member(memberId, name);
        members[memberCount++] = member;
        return member;
    }

    public boolean removeMember(String memberId) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getMemberId().equals(memberId)) {
                if (members[i].getBorrowCount() > 0) return false;
                for (int j = i; j < memberCount - 1; j++) members[j] = members[j + 1];
                members[--memberCount] = null;
                return true;
            }
        }
        return false;
    }

    public Member findMemberById(String memberId) {
        for (int i = 0; i < memberCount; i++)
            if (members[i].getMemberId().equals(memberId)) return members[i];
        return null;
    }

    // ── Combined catalog lookup ───────────────────────────────────────────────

    public LibraryItem findItemById(String itemId) {
        Books book = findBookById(itemId);
        if (book != null) return book;
        return findMultimediaById(itemId);
    }

    public LibraryItem[] getAllItems() {
        LibraryItem[] all = new LibraryItem[catalogCount + multimediaCount];
        for (int i = 0; i < catalogCount; i++)     all[i]                = catalog[i];
        for (int i = 0; i < multimediaCount; i++)  all[catalogCount + i] = multimedia[i];
        return all;
    }

    public int getAllItemsCount() { return catalogCount + multimediaCount; }

    // ── Borrow records ────────────────────────────────────────────────────────

    public void recordBorrow(Member member, LibraryItem item) {
        if (recordCount >= MAX_RECORDS) return;
        String recordId = "REC" + String.format("%03d", recordCount + 1);
        borrowRecords[recordCount++] = new BorrowRecord(recordId, member, item, "2026-04-29");
    }

    public void recordReturn(Member member, LibraryItem item) {
        for (int i = 0; i < recordCount; i++) {
            BorrowRecord r = borrowRecords[i];
            if (!r.isReturned()
                    && r.getMember().getMemberId().equals(member.getMemberId())
                    && r.getItem().getItemId().equals(item.getItemId())) {
                r.setReturnDate("2026-04-29");
                r.setReturned(true);
                return;
            }
        }
    }

    // ── Reservations ──────────────────────────────────────────────────────────

    public Reservation addReservation(Member member, LibraryItem item) {
        if (reservationCount >= MAX_RESERVATIONS) return null;
        String resId   = "RES" + String.format("%03d", reservationCount + 1);
        String resDate = "Day-" + (reservationCount + 1);
        Reservation res = new Reservation(resId, member, item, resDate);
        reservations[reservationCount++] = res;
        return res;
    }

    // ── Feature 4: Search by Genre / Type (method overloading) ───────────────

    public void searchByGenre(Books[] catalog, int size, String genre) {
        System.out.println("Search results for genre: " + genre);
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (catalog[i] != null && catalog[i].getGenre().equalsIgnoreCase(genre)) {
                System.out.println("- " + catalog[i].getTitle()
                        + " | Author: " + catalog[i].getAuthor()
                        + " | Available: " + catalog[i].isAvailable());
                found = true;
            }
        }
        if (!found) System.out.println("No items found for: " + genre);
    }

    public void searchByGenre(Multimedia[] catalog, int size, String type) {
        System.out.println("Search results for type: " + type);
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (catalog[i] != null && catalog[i].getType().equalsIgnoreCase(type)) {
                System.out.println("- " + catalog[i].getTitle()
                        + " | Type: " + catalog[i].getType()
                        + " | Available: " + catalog[i].isAvailable());
                found = true;
            }
        }
        if (!found) System.out.println("No items found for: " + type);
    }

    // ── Info ──────────────────────────────────────────────────────────────────

    @Override
    public String getInfo()    { return "Librarian[" + id + "] " + name; }
    public String toString()   { return getInfo(); }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String          getLibrarianId()      { return id; }
    public Books[]         getCatalog()          { return catalog; }
    public int             getCatalogCount()     { return catalogCount; }
    public int             getCatalogSize()      { return catalogCount; }   // alias for guide
    public Multimedia[]    getMultimedia()       { return multimedia; }
    public int             getMultimediaCount()  { return multimediaCount; }
    public Member[]        getMembers()          { return members; }
    public int             getMemberCount()      { return memberCount; }
    public BorrowRecord[]  getBorrowRecords()    { return borrowRecords; }
    public int             getRecordCount()      { return recordCount; }
    public Reservation[]   getReservations()     { return reservations; }
    public int             getReservationCount() { return reservationCount; }
}
