package Library.Management.impl;

import Library.Management.api.IInventoryManager;
import Library.model.api.IBook;
import Library.utils.AppLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManagement implements IInventoryManager {
    private Map<String, IBook> books;
    private int bookCount;

    public InventoryManagement() {
        this.books = new HashMap<>();
        setBookCount(0);
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    @Override
    public void addBook(IBook book) {
        if (book == null) {
            AppLogger.info(getClass(), "Cannot add null book.");
            return;
        }
        books.put(book.getIsbn(), book);
        setBookCount(getBookCount()+1);
        AppLogger.info(getClass(), "Book with ISBN " + book.getIsbn() + " added successfully.");
    }

    @Override
    public void removeBook(String isbn) {
        if(!books.containsKey(isbn)) {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " not found.");
            return;
        }
        setBookCount(getBookCount()-1);
        if(books.get(isbn).isBorrowed()) {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " is currently borrowed and cannot be removed.");
            return;
        }
        books.remove(isbn);
    }

    @Override
    public void updateBook(String isbn, String title, String author, Integer publicationYear) {
        IBook book = searchBookByISBN(isbn);
        if (book == null) {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " not found.");
            return;
        }
        if (title != null && !title.isEmpty()) {
            book.setTitle(title);
        }
        if (author != null && !author.isEmpty()) {
            book.setAuthor(author);
        }
        if (publicationYear != null && publicationYear > 0) {
            book.setPublicationYear(publicationYear);
        }
        AppLogger.info(getClass(), "Book with ISBN " + isbn + " updated successfully.");
    }


    @Override
    public List<IBook> searchBookByTitle(String title) {
        List<IBook> booksByTitle = new ArrayList<>();
        for (IBook book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                booksByTitle.add(book);
            }
        }
        if (booksByTitle.isEmpty()) {
            AppLogger.info(getClass(), "No books found with title " + title);
        }
        return booksByTitle;
    }

    @Override
    public List<IBook> searchBookByAuthor(String author) {
        List<IBook> booksByAuthor = new ArrayList<>();
        for (IBook book : books.values()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                booksByAuthor.add(book);
            }
        }
        if (booksByAuthor.isEmpty()) {
            AppLogger.info(getClass(), "No books found by author " + author);
        }
        return booksByAuthor;
    }

    @Override
    public IBook searchBookByISBN(String isbn) {
        if(!books.containsKey(isbn)) {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " not found.");
            return null;
        }
        return books.get(isbn);
    }

    @Override
    public void checkoutBook(String isbn) {
        IBook book = searchBookByISBN(isbn);
        if (book == null) {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " not found.");
            return;
        }
        book.checkoutBook();
        AppLogger.info(getClass(), "Book with ISBN " + isbn + " checked out successfully.");
    }

    @Override
    public void returnBook(String isbn) {
        IBook book = searchBookByISBN(isbn);
        if (book == null) {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " not found.");
            return;
        }
        book.returnBook();
        AppLogger.info(getClass(), "Book with ISBN " + isbn + " returned successfully.");
    }

    public List<IBook> getAvailableBooks() {
        List<IBook> available = new ArrayList<>();
        for (IBook book : books.values()) {
            if (!book.isBorrowed()) {
                available.add(book);
            }
        }

        return available;
    }

    public List<IBook> getBorrowedBooks() {
        List<IBook> borrowed = new ArrayList<>();
        for (IBook book : books.values()) {
            if (book.isBorrowed()) {
                borrowed.add(book);
            }
        }

        return borrowed;
    }

}
