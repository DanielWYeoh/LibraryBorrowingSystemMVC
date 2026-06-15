package com.fad.LibrarySystem.model;

public class Reservation {

    private String reservationId;
    private Member member;
    private LibraryItem item;
    private String reservationDate;
    private boolean active;

    public Reservation(String reservationId, Member member, LibraryItem item, String reservationDate) {
        this.reservationId   = reservationId;
        this.member          = member;
        this.item            = item;
        this.reservationDate = reservationDate;
        this.active          = true;
    }

    public void cancelReservation() { this.active = false; }

    public String getInfo() {
        return "[Reservation] ID: " + reservationId
                + " | Member: " + member.getName()
                + " | Item: "   + item.getTitle()
                + " | Date: "   + reservationDate
                + " | Status: " + (active ? "Active" : "Cancelled");
    }

    public String      getReservationId()   { return reservationId; }
    public Member      getMember()          { return member; }
    public LibraryItem getItem()            { return item; }
    public String      getReservationDate() { return reservationDate; }
    public boolean     isActive()           { return active; }

    public String toString() { return getInfo(); }
}
