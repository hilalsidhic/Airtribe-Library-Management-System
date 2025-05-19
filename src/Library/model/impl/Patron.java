package Library.model.impl;

import Library.model.api.IPatron;
import Library.utils.AppLogger;
import Library.utils.LibraryConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patron implements IPatron {
    private String name;
    private String email;
    private String phoneNumber;
    private Set<String> borrowedBookIsbnList;

    public Patron(String name, String email, String phoneNumber) {
        setEmail(email);
        setName(name);
        setPhoneNumber(phoneNumber);
        this.borrowedBookIsbnList = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void addBook(String bookIsbn) {
        if (getBorrowedBookIsbnList().contains(bookIsbn)) {
            AppLogger.info(getClass(), "Book with ISBN " + bookIsbn + " already borrowed.");
            return;
        }
        if (!canBorrowMoreBooks()) {
            AppLogger.info(getClass(), "Cannot borrow more books. Limit reached.");
            return;
        }
        borrowedBookIsbnList.add(bookIsbn);
    }

    @Override
    public void removeBook(String bookIsbn) {
        if (!borrowedBookIsbnList.contains(bookIsbn)) {
            AppLogger.info(getClass(), "Book with ISBN " + bookIsbn + " not found in borrowed list.");
            return;
        }
        borrowedBookIsbnList.remove(bookIsbn);
    }

    @Override
    public int getBorrowedBooksCount() {
        if (borrowedBookIsbnList == null) {
            return 0;
        }
        return borrowedBookIsbnList.size();
    }

    @Override
    public List<String> getBorrowedBookIsbnList() {
        if (borrowedBookIsbnList == null) {
            return null;
        }
        return List.copyOf(borrowedBookIsbnList);
    }

    @Override
    public boolean canBorrowMoreBooks() {
        if (borrowedBookIsbnList == null) {
            return true;
        }
        return borrowedBookIsbnList.size() < LibraryConstants.MAX_BORROWED_BOOKS;
    }

    @Override
    public List<String> getDetails() {
        return List.of(name, email, phoneNumber);
    }
}
