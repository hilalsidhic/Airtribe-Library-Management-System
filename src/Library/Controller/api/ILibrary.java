package Library.Controller.api;

public interface ILibrary {
    void addBook(String isbn, String title, String author, int publicationYear);
    void removeBook(String isbn);
    void updateBook(String isbn, String newTitle, String newAuthor, int newPublicationYear);

    void addPatron(String name, String email, String phoneNumber);
    void removePatron(String email);
    void updatePatron(String email, String newName, String newEmail);

    void borrowBook(String email, String bookIsbn);
    void returnBook(String email, String bookIsbn);

    void searchBookByTitle(String title);
    void searchBookByAuthor(String author);
    void searchBookByISBN(String isbn);
    void getAvailableBooks();
    void getBorrowedBooks();
}
