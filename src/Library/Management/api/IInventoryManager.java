package Library.Management.api;

import Library.model.api.IBook;

import java.util.List;

public interface IInventoryManager {
    void addBook(IBook book);
    void removeBook(String isbn);
    void updateBook(String isbn, String title, String author, Integer PublicationYear);

    List<IBook> searchBookByTitle(String title);
    List<IBook> searchBookByAuthor(String author);
    IBook searchBookByISBN(String isbn);

    void checkoutBook(String isbn);
    void returnBook(String isbn);

    List<IBook> getAvailableBooks();
    List<IBook> getBorrowedBooks();
}
