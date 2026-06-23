package com.fad.LibrarySystem.model;

import com.fad.LibrarySystem.database.DatabaseManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibraryService {

    private final List<Book>         catalog       = new ArrayList<>();
    private final List<Multimedia>   multimedia    = new ArrayList<>();
    private final List<Member>       members       = new ArrayList<>();
    private final List<BorrowRecord> borrowRecords = new ArrayList<>();
    private final List<Reservation>  reservations  = new ArrayList<>();
    private final List<Fine>         fines         = new ArrayList<>();

    public LibraryService() {
        DatabaseManager.initSchema();
        loadFromDatabase();
    }

    // ── Startup ───────────────────────────────────────────────────────────────

    private void loadFromDatabase() {
        if (DatabaseManager.isTableEmpty("books")) seedInitialData();

        for (Book b : DatabaseManager.loadBooks())             catalog.add(b);
        for (Multimedia m : DatabaseManager.loadMultimedia())  multimedia.add(m);

        for (Member m : DatabaseManager.loadMembers()) {
            members.add(m);
            for (String itemId : DatabaseManager.loadBorrowedItemIds(m.getMemberId())) {
                LibraryItem item = findItemById(itemId);
                if (item != null) m.addBorrowedItem(item);
            }
        }

        for (String[] row : DatabaseManager.loadBorrowRecordRows()) {
            Member      m    = findMemberById(row[1]);
            LibraryItem item = findItemById(row[2]);
            if (m == null || item == null) continue;
            BorrowRecord r = new BorrowRecord(row[0], m, item, row[3]);
            r.setReturnDate(row[4]);
            r.setReturned(row[5].equals("1"));
            borrowRecords.add(r);
        }

        for (String[] row : DatabaseManager.loadReservationRows()) {
            Member      m    = findMemberById(row[1]);
            LibraryItem item = findItemById(row[2]);
            if (m == null || item == null) continue;
            Reservation res = new Reservation(row[0], m, item, row[3]);
            if (row[4].equals("0")) res.cancelReservation();
            reservations.add(res);
        }

        for (String[] row : DatabaseManager.loadFineRows()) {
            Member      m    = findMemberById(row[1]);
            LibraryItem item = findItemById(row[2]);
            if (m == null || item == null) continue;
            fines.add(new Fine(row[0], m, item, Integer.parseInt(row[3])));
        }
    }

    private void seedInitialData() {
        for (Book b : Book.getInitialBooks())                  DatabaseManager.insertBook(b);
        for (Multimedia m : Multimedia.getInitialMultimedia()) DatabaseManager.insertMultimedia(m);
        for (Member m : Member.getInitialMembers())            DatabaseManager.insertMember(m);
    }

    // ── Books ─────────────────────────────────────────────────────────────────

    public Book addBook(String bookId, String title, String author, String genre) {
        if (findBookById(bookId) != null) return null;
        Book book = new Book(bookId, title, author, genre);
        catalog.add(book);
        DatabaseManager.insertBook(book);
        return book;
    }

    public Book addBook(String bookId, String title, String author) {
        return addBook(bookId, title, author, "General");
    }

    public boolean removeBook(String bookId) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getBookId().equals(bookId)) {
                if (!catalog.get(i).isAvailable()) return false;
                catalog.remove(i);
                DatabaseManager.deleteBook(bookId);
                return true;
            }
        }
        return false;
    }

    public boolean updateBook(String bookId, String newTitle, String newAuthor, String newGenre) {
        Book book = findBookById(bookId);
        if (book == null) return false;
        if (!newTitle.trim().isEmpty())  book.setTitle(newTitle.trim());
        if (!newAuthor.trim().isEmpty()) book.setAuthor(newAuthor.trim());
        if (!newGenre.trim().isEmpty())  book.setGenre(newGenre.trim());
        DatabaseManager.updateBook(book);
        return true;
    }

    public Book findBookById(String bookId) {
        for (Book b : catalog)
            if (b.getBookId().equals(bookId)) return b;
        return null;
    }

    // ── Multimedia ────────────────────────────────────────────────────────────

    public Multimedia addMultimedia(String itemId, String title, String type, String duration) {
        if (findMultimediaById(itemId) != null) return null;
        Multimedia item = new Multimedia(itemId, title, type, duration);
        multimedia.add(item);
        DatabaseManager.insertMultimedia(item);
        return item;
    }

    public Multimedia addMultimedia(String itemId, String title, String type) {
        return addMultimedia(itemId, title, type, "Unknown");
    }

    public Multimedia findMultimediaById(String itemId) {
        for (Multimedia m : multimedia)
            if (m.getItemId().equals(itemId)) return m;
        return null;
    }

    // ── Members ───────────────────────────────────────────────────────────────

    public Member registerMember(String memberId, String name) {
        if (findMemberById(memberId) != null) return null;
        Member member = new Member(memberId, name);
        members.add(member);
        DatabaseManager.insertMember(member);
        return member;
    }

    public boolean removeMember(String memberId) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId().equals(memberId)) {
                if (members.get(i).getBorrowCount() > 0) return false;
                members.remove(i);
                DatabaseManager.deleteMember(memberId);
                return true;
            }
        }
        return false;
    }

    public Member findMemberById(String memberId) {
        for (Member m : members)
            if (m.getMemberId().equals(memberId)) return m;
        return null;
    }

    // ── Combined catalog lookup ───────────────────────────────────────────────

    public LibraryItem findItemById(String itemId) {
        Book book = findBookById(itemId);
        if (book != null) return book;
        return findMultimediaById(itemId);
    }

    // ── Borrow records ────────────────────────────────────────────────────────

    public void recordBorrow(Member member, LibraryItem item) {
        String recordId = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String today    = LocalDate.now().toString();
        BorrowRecord record = new BorrowRecord(recordId, member, item, today);
        borrowRecords.add(record);
        DatabaseManager.insertBorrowRecord(record);
        DatabaseManager.insertMemberBorrowedItem(member.getMemberId(), item.getItemId());
        DatabaseManager.updateItemAvailability(item.getItemId(), false);
    }

    public void recordReturn(Member member, LibraryItem item) {
        String today = LocalDate.now().toString();
        for (BorrowRecord r : borrowRecords) {
            if (!r.isReturned()
                    && r.getMember().getMemberId().equals(member.getMemberId())
                    && r.getItem().getItemId().equals(item.getItemId())) {
                r.setReturnDate(today);
                r.setReturned(true);
                DatabaseManager.markBorrowRecordReturned(member.getMemberId(), item.getItemId(), today);
                DatabaseManager.deleteMemberBorrowedItem(member.getMemberId(), item.getItemId());
                DatabaseManager.updateItemAvailability(item.getItemId(), true);
                return;
            }
        }
    }

    // ── Reservations ──────────────────────────────────────────────────────────

    public Reservation addReservation(Member member, LibraryItem item) {
        String resId   = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String resDate = LocalDate.now().toString();
        Reservation res = new Reservation(resId, member, item, resDate);
        reservations.add(res);
        DatabaseManager.insertReservation(resId, member.getMemberId(), item.getItemId(), resDate);
        return res;
    }

    // ── Fines ─────────────────────────────────────────────────────────────────

    public Fine recordFine(Member member, LibraryItem item, int daysLate) {
        String fineId = "FINE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Fine fine = new Fine(fineId, member, item, daysLate);
        fines.add(fine);
        DatabaseManager.insertFine(fine);
        return fine;
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public List<Book> searchByGenre(String genre) {
        List<Book> results = new ArrayList<>();
        for (Book b : catalog)
            if (b.getGenre().equalsIgnoreCase(genre)) results.add(b);
        return results;
    }

    public List<Multimedia> searchByType(String type) {
        List<Multimedia> results = new ArrayList<>();
        for (Multimedia m : multimedia)
            if (m.getType().equalsIgnoreCase(type)) results.add(m);
        return results;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public List<Book>         getCatalog()       { return catalog; }
    public List<Multimedia>   getMultimedia()    { return multimedia; }
    public List<Member>       getMembers()       { return members; }
    public List<BorrowRecord> getBorrowRecords() { return borrowRecords; }
    public List<Reservation>  getReservations()  { return reservations; }
    public List<Fine>         getFines()         { return fines; }
}
