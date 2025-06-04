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

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        IInventoryManager inventoryManager = new InventoryManagement();
        IPatronManager patronManager = new PatronManagement();
        IBookFactory bookFactory = new BookFactory();
        IPatronFactory patronFactory = new PatronFactory();

        ILibrary library = Library.getInstance(inventoryManager, patronManager, bookFactory, patronFactory);

        int choice;
        do {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Update Book");
            System.out.println("4. Add Patron");
            System.out.println("5. Remove Patron");
            System.out.println("6. Update Patron");
            System.out.println("7. Borrow Book");
            System.out.println("8. Return Book");
            System.out.println("9. Search Book by Title");
            System.out.println("10. Search Book by Author");
            System.out.println("11. Search Book by ISBN");
            System.out.println("12. View Available Books");
            System.out.println("13. View Borrowed Books");
            System.out.println("14. View All Patrons");
            System.out.println("15. View Patron Count");
            System.out.println("16. View Patron Details");
            System.out.println("17. View Patron Borrowed Books");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            String isbn, title, author, name, email, phone, newEmail, newName;
            int year;

            switch (choice) {
                case 1:
                    System.out.print("Enter ISBN: ");
                    isbn = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    author = scanner.nextLine();
                    System.out.print("Enter Publication Year: ");
                    year = scanner.nextInt();
                    scanner.nextLine();
                    library.addBook(isbn, title, author, year);
                    break;
                case 2:
                    System.out.print("Enter ISBN to remove: ");
                    isbn = scanner.nextLine();
                    library.removeBook(isbn);
                    break;
                case 3:
                    System.out.print("Enter ISBN to update: ");
                    isbn = scanner.nextLine();
                    System.out.print("Enter new Title: ");
                    title = scanner.nextLine();
                    System.out.print("Enter new Author: ");
                    author = scanner.nextLine();
                    System.out.print("Enter new Publication Year: ");
                    year = scanner.nextInt();
                    scanner.nextLine();
                    library.updateBook(isbn, title, author, year);
                    break;
                case 4:
                    System.out.print("Enter Name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    email = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    phone = scanner.nextLine();
                    library.addPatron(name, email, phone);
                    break;
                case 5:
                    System.out.print("Enter Email to remove patron: ");
                    email = scanner.nextLine();
                    library.removePatron(email);
                    break;
                case 6:
                    System.out.print("Enter current Email of patron: ");
                    email = scanner.nextLine();
                    System.out.print("Enter new Name: ");
                    newName = scanner.nextLine();
                    System.out.print("Enter new Email: ");
                    newEmail = scanner.nextLine();
                    library.updatePatron(email, newName, newEmail);
                    break;
                case 7:
                    System.out.print("Enter Patron Email: ");
                    email = scanner.nextLine();
                    System.out.print("Enter ISBN to borrow: ");
                    isbn = scanner.nextLine();
                    library.borrowBook(email, isbn);
                    break;
                case 8:
                    System.out.print("Enter Patron Email: ");
                    email = scanner.nextLine();
                    System.out.print("Enter ISBN to return: ");
                    isbn = scanner.nextLine();
                    library.returnBook(email, isbn);
                    break;
                case 9:
                    System.out.print("Enter Title to search: ");
                    title = scanner.nextLine();
                    library.searchBookByTitle(title);
                    break;
                case 10:
                    System.out.print("Enter Author to search: ");
                    author = scanner.nextLine();
                    library.searchBookByAuthor(author);
                    break;
                case 11:
                    System.out.print("Enter ISBN to search: ");
                    isbn = scanner.nextLine();
                    library.searchBookByISBN(isbn);
                    break;
                case 12:
                    library.getAvailableBooks();
                    break;
                case 13:
                    library.getBorrowedBooks();
                    break;
                case 14:
                    library.getAllPatrons();
                    break;
                case 15:
                    library.getPatronCount();
                    break;
                case 16:
                    System.out.print("Enter Patron Email to view details: ");
                    email = scanner.nextLine();
                    library.getPatronDetails(email);
                    break;
                case 17:
                    System.out.print("Enter Patron Email to view borrowed books: ");
                    email = scanner.nextLine();
                    library.getPatronBorrowedBooks(email);
                    break;
                case 0:
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}
