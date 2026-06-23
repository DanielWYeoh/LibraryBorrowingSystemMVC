package com.fad.LibrarySystem.model;

public class Multimedia extends LibraryItem {

    private String type;
    private String duration;

    public Multimedia(String itemId, String title, String type, String duration) {
        super(itemId, title);
        this.type     = type;
        this.duration = duration;
    }

    public static Multimedia[] getInitialMultimedia() {
        return new Multimedia[]{
            // ── Original ──────────────────────────────────────────────────────
            new Multimedia("MM001", "Inception",         "DVD",       "148 min"),
            new Multimedia("MM002", "Dark Side of Moon", "CD",        "43 min"),
            new Multimedia("MM003", "Sapiens Audiobook", "Audiobook", "15 hrs"),
            // ── Videos (DVD) ──────────────────────────────────────────────────
            new Multimedia("MM004", "The Shawshank Redemption",          "DVD", "142 min"),
            new Multimedia("MM005", "The Godfather",                     "DVD", "175 min"),
            new Multimedia("MM006", "The Dark Knight",                   "DVD", "152 min"),
            new Multimedia("MM007", "Pulp Fiction",                      "DVD", "154 min"),
            new Multimedia("MM008", "Forrest Gump",                      "DVD", "142 min"),
            new Multimedia("MM009", "The Matrix",                        "DVD", "136 min"),
            new Multimedia("MM010", "Schindler's List",                  "DVD", "195 min"),
            new Multimedia("MM011", "Interstellar",                      "DVD", "169 min"),
            new Multimedia("MM012", "The Lord of the Rings: Fellowship", "DVD", "178 min"),
            new Multimedia("MM013", "Gladiator",                         "DVD", "155 min"),
            new Multimedia("MM014", "The Lion King",                     "DVD", "88 min"),
            new Multimedia("MM015", "Jurassic Park",                     "DVD", "127 min"),
            new Multimedia("MM016", "Titanic",                           "DVD", "194 min"),
            new Multimedia("MM017", "Avatar",                            "DVD", "162 min"),
            new Multimedia("MM018", "Parasite",                          "DVD", "132 min"),
            new Multimedia("MM019", "Spirited Away",                     "DVD", "125 min"),
            new Multimedia("MM020", "Whiplash",                          "DVD", "106 min"),
            new Multimedia("MM021", "The Prestige",                      "DVD", "130 min"),
            new Multimedia("MM022", "Fight Club",                        "DVD", "139 min"),
            new Multimedia("MM023", "Goodfellas",                        "DVD", "145 min"),
            new Multimedia("MM024", "Saving Private Ryan",               "DVD", "169 min"),
            new Multimedia("MM025", "The Silence of the Lambs",          "DVD", "118 min"),
            new Multimedia("MM026", "Braveheart",                        "DVD", "178 min"),
            new Multimedia("MM027", "Catch Me If You Can",               "DVD", "141 min"),
            new Multimedia("MM028", "The Green Mile",                    "DVD", "188 min"),
            new Multimedia("MM029", "A Beautiful Mind",                  "DVD", "135 min"),
            new Multimedia("MM030", "Rain Man",                          "DVD", "133 min"),
            new Multimedia("MM031", "Dead Poets Society",                "DVD", "128 min"),
            new Multimedia("MM032", "Good Will Hunting",                 "DVD", "126 min"),
            new Multimedia("MM033", "The Truman Show",                   "DVD", "103 min"),
        };
    }

    @Override
    public String getInfo() {
        String status = available ? "Available" : "Borrowed";
        return "[" + itemId + "] \"" + title + "\""
                + " | Type: " + type
                + " | Duration: " + duration
                + " (" + status + ")";
    }

    @Override
    public String toString() { return getInfo(); }

    public String getType()     { return type; }
    public String getDuration() { return duration; }

    public void setType(String type)         { this.type     = type; }
    public void setDuration(String duration) { this.duration = duration; }
}
