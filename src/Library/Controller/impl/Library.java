package Library.Controller.impl;

import Library.Controller.api.ILibrary;
import Library.Factory.api.IBookFactory;
import Library.Factory.api.IPatronFactory;
import Library.Management.api.IInventoryManager;
import Library.Management.api.IPatronManager;
import Library.model.api.IBook;
import Library.utils.AppLogger;

import java.util.List;

public class Library implements ILibrary{
    private static Library instance;
    private final IInventoryManager inventoryManager;
    private final IPatronManager patronManager;
    private final IBookFactory bookFactory;
    private final IPatronFactory patronFactory;

    public Library(IInventoryManager inventoryManager, IPatronManager patronManager,
                   IBookFactory bookFactory, IPatronFactory patronFactory) {
        this.bookFactory = bookFactory;
        this.patronFactory = patronFactory;
        this.inventoryManager = inventoryManager;
        this.patronManager = patronManager;
    }
    public static Library getInstance(IInventoryManager inventoryManager, IPatronManager patronManager,
                                       IBookFactory bookFactory, IPatronFactory patronFactory) {
        if (instance == null) {
            instance = new Library(inventoryManager, patronManager, bookFactory, patronFactory);
        }
        return instance;
    }
    public void addBook(String isbn, String title, String author, int publicationYear) {
        try{
            inventoryManager.addBook(bookFactory.createBook( isbn, title, author,publicationYear));
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error adding book: " + e.getMessage());
        }
    }
    public void removeBook(String isbn) {
        try{
            inventoryManager.removeBook(isbn);
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error removing book: " + e.getMessage());
        }
    }
    public void updateBook(String isbn, String title, String author, int publicationYear) {
        try{
            inventoryManager.updateBook(isbn, title, author, publicationYear);
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error updating book: " + e.getMessage());
        }
    }
    public void addPatron(String name, String email, String phone) {
        try {
            patronManager.addPatron(patronFactory.createPatron(name, email, phone));
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error adding patron: " + e.getMessage());
        }
    }
    public void removePatron(String email) {
        try {
            patronManager.removePatron(email);
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error removing patron: " + e.getMessage());
        }
    }
    public void updatePatron(String email, String newName, String newEmail) {
        try{
            patronManager.updatePatron(email, newName, newEmail);
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error updating patron: " + e.getMessage());
        }
    }
    public void borrowBook(String email, String isbn) {
        try {
            patronManager.borrowBook(email, isbn);
            inventoryManager.checkoutBook(isbn);
        }
        catch (Exception e) {
            AppLogger.error(getClass(), "Error borrowing book: " + e.getMessage());
        }
    }
    public void returnBook(String email, String isbn) {
        if (isbn == null || email == null) {
            AppLogger.error(getClass(), "ISBN or email cannot be null");
            return;
        }
        patronManager.returnBook(email, isbn);
        inventoryManager.returnBook(isbn);
    }
    public void searchBookByTitle(String title) {
        List<IBook> bookList = inventoryManager.searchBookByTitle(title);
        if (bookList.isEmpty()){
            AppLogger.info(getClass(), "No books found with title: " + title);
            return;
        }
        for (IBook book : bookList) {
            AppLogger.info(getClass(),"Books with title: " + title + book.getBookDetails());
        }
    }
    public void searchBookByAuthor(String author) {
        List<IBook> bookList = inventoryManager.searchBookByAuthor(author);
        if (bookList.isEmpty()){
            AppLogger.info(getClass(), "No books found by author: " + author);
            return;
        }
        for (IBook book : bookList) {
            AppLogger.info(getClass(),"Books by author: " + author + book.getBookDetails());
        }
    }
    public void searchBookByISBN(String isbn) {
        IBook book = inventoryManager.searchBookByISBN(isbn);
        if (book != null) {
            AppLogger.info(getClass(), String.valueOf(book.getBookDetails()));
        } else {
            AppLogger.info(getClass(), "Book with ISBN " + isbn + " not found.");
        }
    }
    public void getAvailableBooks() {
        List<IBook> bookList = inventoryManager.getAvailableBooks();
        if (bookList.isEmpty()){
            AppLogger.info(getClass(), "No available books found.");
            return;
        }
        for (IBook book : bookList) {
            AppLogger.info(getClass(),"Available Books:"+ book.getBookDetails());
        }
    }
    public void getBorrowedBooks() {
        List<IBook> bookList = inventoryManager.getBorrowedBooks();
        if (bookList.isEmpty()){
            AppLogger.info(getClass(), "No borrowed books found.");
            return;
        }
        for (IBook book : bookList) {
            AppLogger.info(getClass(),"Borrowed Books :"+ book.getBookDetails());
        }
    }
}
