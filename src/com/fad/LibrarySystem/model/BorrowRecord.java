package com.fad.LibrarySystem.model;

public class BorrowRecord {

    private String  recordId;
    private Member  member;
    private LibraryItem item;
    private String  borrowDate;
    private String  returnDate;
    private boolean returned;

    public BorrowRecord(String recordId, Member member, LibraryItem item, String borrowDate) {
        this.recordId   = recordId;
        this.member     = member;
        this.item       = item;
        this.borrowDate = borrowDate;
        this.returnDate = "-";
        this.returned   = false;
    }

    public String getInfo() {
        return "Record[" + recordId + "]"
                + " Member: "    + member.getName()
                + " | Item: "    + item.getTitle()
                + " | Borrowed: " + borrowDate
                + " | Returned: " + returnDate;
    }

    public String  getRecordId()   { return recordId; }
    public Member  getMember()     { return member; }
    public LibraryItem getItem()   { return item; }
    public String  getBorrowDate() { return borrowDate; }
    public String  getReturnDate() { return returnDate; }
    public boolean isReturned()    { return returned; }

    public void setRecordId(String recordId)     { this.recordId   = recordId; }
    public void setMember(Member member)         { this.member     = member; }
    public void setItem(LibraryItem item)        { this.item       = item; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setReturned(boolean returned)    { this.returned   = returned; }

    public String toString() { return getInfo(); }
}
