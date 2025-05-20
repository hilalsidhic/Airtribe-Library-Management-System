import Library.Controller.api.ILibrary;
import Library.Controller.impl.Library;
import Library.Factory.api.IBookFactory;
import Library.Factory.api.IPatronFactory;
import Library.Factory.impl.BookFactory;
import Library.Factory.impl.PatronFactory;
import Library.Management.api.IInventoryManager;
import Library.Management.api.IPatronManager;
import Library.Management.impl.InventoryManagement;
import Library.Management.impl.PatronManagement;

public class Main {
    public static void main(String[] args) {
        IInventoryManager inventoryManager = new InventoryManagement();
        IPatronManager patronManager = new PatronManagement();
        IBookFactory bookFactory = new BookFactory();
        IPatronFactory patronFactory = new PatronFactory();

        ILibrary library = Library.getInstance(inventoryManager, patronManager, bookFactory, patronFactory);

        library.addBook("978-3-16-148410-0", "The Great Gatsby", "F. Scott Fitzgerald", 1925);
        library.addBook("978-1-56619-909-4", "To Kill a Mockingbird", "Harper Lee", 1960);
        library.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 2018);
        library.addBook("978-0-201-83595-3", "Head First Java", "Kathy Sierra", 2005);
        library.addBook("978-0134685992", "Effective Java", "Joshua Bloch", 2018);

        //add patrons
        library.addPatron("Alice Johnson", "alice@example.com", "555-1234");
        library.addPatron("Bob Smith", "bob@example.com", "555-5678");

        library.borrowBook("alice@example.com", "978-3-16-148410-0");
//        library.returnBook("alice@example.com", "978-3-16-148410-0");

        // Search books
        library.searchBookByTitle("Effective");
        library.searchBookByAuthor("Kathy Sierra");
        library.searchBookByISBN("978-0134685991");

        // List available and borrowed books
        library.getAvailableBooks();
        library.getBorrowedBooks();

        // Remove a book
//        library.removeBook("978-3-16-148410-0");

        library.removePatron("alice@example.com");

        // Update a book
        library.updateBook("978-1-56619-909-4", "To Kill a Mockingbird", "Harper Lee", 1961);
        // Update a patron

    }
}