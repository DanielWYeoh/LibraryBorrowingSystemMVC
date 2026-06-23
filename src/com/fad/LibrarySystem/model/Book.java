package com.fad.LibrarySystem.model;

public class Book extends LibraryItem {

    private String author;
    private String genre;

    public Book(String bookId, String title, String author, String genre) {
        super(bookId, title);
        this.author = author;
        this.genre  = genre;
    }

    public static Book[] getInitialBooks() {
        return new Book[]{
            // ── Original ──────────────────────────────────────────────────────
            new Book("B001", "The Great Gatsby",        "F. Scott Fitzgerald", "Classic"),
            new Book("B002", "To Kill a Mockingbird",   "Harper Lee",          "Fiction"),
            new Book("B003", "1984",                    "George Orwell",       "Dystopian"),
            new Book("B004", "Brave New World",         "Aldous Huxley",       "Sci-Fi"),
            new Book("B005", "The Catcher in the Rye",  "J.D. Salinger",       "Fiction"),
            // ── Fiction ───────────────────────────────────────────────────────
            new Book("B006", "Pride and Prejudice",          "Jane Austen",         "Fiction"),
            new Book("B007", "Jane Eyre",                    "Charlotte Bronte",    "Fiction"),
            new Book("B008", "Wuthering Heights",            "Emily Bronte",        "Fiction"),
            new Book("B009", "The Old Man and the Sea",      "Ernest Hemingway",    "Fiction"),
            new Book("B010", "Of Mice and Men",              "John Steinbeck",      "Fiction"),
            new Book("B011", "East of Eden",                 "John Steinbeck",      "Fiction"),
            new Book("B012", "The Grapes of Wrath",          "John Steinbeck",      "Fiction"),
            new Book("B013", "Beloved",                      "Toni Morrison",       "Fiction"),
            new Book("B014", "The Color Purple",             "Alice Walker",        "Fiction"),
            new Book("B015", "Their Eyes Were Watching God", "Zora Neale Hurston",  "Fiction"),
            new Book("B016", "Gone with the Wind",           "Margaret Mitchell",   "Fiction"),
            new Book("B017", "Rebecca",                      "Daphne du Maurier",   "Fiction"),
            new Book("B018", "The Bell Jar",                 "Sylvia Plath",        "Fiction"),
            new Book("B019", "Mrs Dalloway",                 "Virginia Woolf",      "Fiction"),
            new Book("B020", "To the Lighthouse",            "Virginia Woolf",      "Fiction"),
            // ── Classic ───────────────────────────────────────────────────────
            new Book("B021", "Crime and Punishment",           "Fyodor Dostoevsky",   "Classic"),
            new Book("B022", "War and Peace",                  "Leo Tolstoy",         "Classic"),
            new Book("B023", "Anna Karenina",                  "Leo Tolstoy",         "Classic"),
            new Book("B024", "Don Quixote",                    "Miguel de Cervantes", "Classic"),
            new Book("B025", "Moby Dick",                      "Herman Melville",     "Classic"),
            new Book("B026", "Adventures of Huckleberry Finn", "Mark Twain",          "Classic"),
            new Book("B027", "The Count of Monte Cristo",      "Alexandre Dumas",     "Classic"),
            new Book("B028", "Les Miserables",                 "Victor Hugo",         "Classic"),
            new Book("B029", "Frankenstein",                   "Mary Shelley",        "Classic"),
            new Book("B030", "Dracula",                        "Bram Stoker",         "Classic"),
            new Book("B031", "Oliver Twist",                   "Charles Dickens",     "Classic"),
            new Book("B032", "Great Expectations",             "Charles Dickens",     "Classic"),
            new Book("B033", "A Tale of Two Cities",           "Charles Dickens",     "Classic"),
            new Book("B034", "David Copperfield",              "Charles Dickens",     "Classic"),
            new Book("B035", "Middlemarch",                    "George Eliot",        "Classic"),
            // ── Sci-Fi ────────────────────────────────────────────────────────
            new Book("B036", "Dune",                                    "Frank Herbert",      "Sci-Fi"),
            new Book("B037", "Foundation",                              "Isaac Asimov",       "Sci-Fi"),
            new Book("B038", "The Hitchhiker's Guide to the Galaxy",    "Douglas Adams",      "Sci-Fi"),
            new Book("B039", "Ender's Game",                            "Orson Scott Card",   "Sci-Fi"),
            new Book("B040", "The Martian",                             "Andy Weir",          "Sci-Fi"),
            new Book("B041", "Fahrenheit 451",                          "Ray Bradbury",       "Sci-Fi"),
            new Book("B042", "I Robot",                                 "Isaac Asimov",       "Sci-Fi"),
            new Book("B043", "Neuromancer",                             "William Gibson",     "Sci-Fi"),
            new Book("B044", "The Left Hand of Darkness",               "Ursula K. Le Guin", "Sci-Fi"),
            new Book("B045", "Childhood's End",                         "Arthur C. Clarke",   "Sci-Fi"),
            new Book("B046", "The War of the Worlds",                   "H.G. Wells",         "Sci-Fi"),
            new Book("B047", "The Time Machine",                        "H.G. Wells",         "Sci-Fi"),
            new Book("B048", "Do Androids Dream of Electric Sheep",     "Philip K. Dick",     "Sci-Fi"),
            new Book("B049", "Slaughterhouse-Five",                     "Kurt Vonnegut",      "Sci-Fi"),
            new Book("B050", "The Handmaid's Tale",                     "Margaret Atwood",    "Dystopian"),
            // ── Mystery / Thriller ────────────────────────────────────────────
            new Book("B051", "The Girl with the Dragon Tattoo", "Stieg Larsson",      "Mystery"),
            new Book("B052", "Gone Girl",                        "Gillian Flynn",       "Thriller"),
            new Book("B053", "And Then There Were None",         "Agatha Christie",     "Mystery"),
            new Book("B054", "Murder on the Orient Express",     "Agatha Christie",     "Mystery"),
            new Book("B055", "The Hound of the Baskervilles",    "Arthur Conan Doyle",  "Mystery"),
            new Book("B056", "In Cold Blood",                    "Truman Capote",       "Thriller"),
            new Book("B057", "The Da Vinci Code",                "Dan Brown",           "Thriller"),
            new Book("B058", "Big Little Lies",                  "Liane Moriarty",      "Thriller"),
            new Book("B059", "The Silence of the Lambs",         "Thomas Harris",       "Thriller"),
            new Book("B060", "Sharp Objects",                    "Gillian Flynn",       "Mystery"),
            new Book("B061", "The Girl on the Train",            "Paula Hawkins",       "Mystery"),
            new Book("B062", "The Name of the Rose",             "Umberto Eco",         "Mystery"),
            // ── Fantasy ───────────────────────────────────────────────────────
            new Book("B063", "The Lord of the Rings",                  "J.R.R. Tolkien",    "Fantasy"),
            new Book("B064", "The Hobbit",                             "J.R.R. Tolkien",    "Fantasy"),
            new Book("B065", "Harry Potter and the Sorcerer's Stone",  "J.K. Rowling",      "Fantasy"),
            new Book("B066", "A Game of Thrones",                      "George R.R. Martin","Fantasy"),
            new Book("B067", "The Way of Kings",                       "Brandon Sanderson", "Fantasy"),
            new Book("B068", "American Gods",                          "Neil Gaiman",       "Fantasy"),
            new Book("B069", "Good Omens",                             "Terry Pratchett",   "Fantasy"),
            new Book("B070", "The Name of the Wind",                   "Patrick Rothfuss",  "Fantasy"),
            new Book("B071", "Mistborn",                               "Brandon Sanderson", "Fantasy"),
            new Book("B072", "The Chronicles of Narnia",               "C.S. Lewis",        "Fantasy"),
            // ── Biography ─────────────────────────────────────────────────────
            new Book("B073", "Long Walk to Freedom",                   "Nelson Mandela",    "Biography"),
            new Book("B074", "The Diary of a Young Girl",              "Anne Frank",        "Biography"),
            new Book("B075", "Steve Jobs",                             "Walter Isaacson",   "Biography"),
            new Book("B076", "Educated",                               "Tara Westover",     "Biography"),
            new Book("B077", "The Story of My Experiments with Truth", "Mahatma Gandhi",    "Biography"),
            new Book("B078", "Leonardo da Vinci",                      "Walter Isaacson",   "Biography"),
            new Book("B079", "Becoming",                               "Michelle Obama",    "Biography"),
            new Book("B080", "Born a Crime",                           "Trevor Noah",       "Biography"),
            new Book("B081", "Open",                                   "Andre Agassi",      "Biography"),
            new Book("B082", "I Am Malala",                            "Malala Yousafzai",  "Biography"),
            // ── Non-Fiction ───────────────────────────────────────────────────
            new Book("B083", "Sapiens",                                "Yuval Noah Harari", "History"),
            new Book("B084", "Thinking Fast and Slow",                 "Daniel Kahneman",   "Psychology"),
            new Book("B085", "The Power of Habit",                     "Charles Duhigg",    "Self-Help"),
            new Book("B086", "Atomic Habits",                          "James Clear",       "Self-Help"),
            new Book("B087", "Man's Search for Meaning",               "Viktor Frankl",     "Philosophy"),
            new Book("B088", "Meditations",                            "Marcus Aurelius",   "Philosophy"),
            new Book("B089", "The Art of War",                         "Sun Tzu",           "Philosophy"),
            new Book("B090", "Outliers",                               "Malcolm Gladwell",  "Psychology"),
            new Book("B091", "The 7 Habits of Highly Effective People","Stephen R. Covey",  "Self-Help"),
            new Book("B092", "How to Win Friends and Influence People", "Dale Carnegie",     "Self-Help"),
            // ── Romance ───────────────────────────────────────────────────────
            new Book("B093", "Sense and Sensibility",   "Jane Austen",     "Romance"),
            new Book("B094", "Emma",                     "Jane Austen",     "Romance"),
            new Book("B095", "Persuasion",               "Jane Austen",     "Romance"),
            new Book("B096", "Outlander",                "Diana Gabaldon",  "Romance"),
            new Book("B097", "The Notebook",             "Nicholas Sparks", "Romance"),
            new Book("B098", "Me Before You",            "Jojo Moyes",      "Romance"),
            new Book("B099", "The Fault in Our Stars",   "John Green",      "Romance"),
            new Book("B100", "Normal People",            "Sally Rooney",    "Romance"),
        };
    }

    @Override
    public String getInfo() {
        String status = available ? "Available" : "Borrowed";
        return "[" + itemId + "] \"" + title + "\" by " + author
                + " | Genre: " + genre + " (" + status + ")";
    }

    @Override
    public String toString() { return getInfo(); }

    public String getBookId() { return itemId; }
    public String getAuthor() { return author; }
    public String getGenre()  { return genre; }

    public void setBookId(String bookId) { this.itemId = bookId; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre)   { this.genre  = genre; }
}
