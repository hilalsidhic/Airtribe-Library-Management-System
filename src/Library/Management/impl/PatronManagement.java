package Library.Management.impl;

import Library.Management.api.IPatronManager;
import Library.model.api.IPatron;
import Library.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

public class PatronManagement implements IPatronManager {
    private Map<String, IPatron> patrons;
    private int patronCount;

    public PatronManagement() {
        this.patrons = new HashMap<>();
        this.patronCount = 0;
    }

    @Override
    public void addPatron(IPatron patron) {
        if (patrons.containsKey(patron.getEmail())) {
            AppLogger.info(getClass(), "Patron with email " + patron.getEmail() + " already exists.");
            return;
        }
        patrons.put(patron.getEmail(), patron);
        patronCount++;
        AppLogger.info(getClass(), "Patron with email " + patron.getEmail() + " added successfully.");
    }

    @Override
    public void removePatron(String email) {
        if (!patrons.containsKey(email)) {
            AppLogger.info(getClass(), "Patron with email " + email + " not found.");
            return;
        }
        patrons.remove(email);
        patronCount--;
        AppLogger.info(getClass(), "Patron with email " + email + " removed successfully.");
    }

    @Override
    public void updatePatron(String email, String newName, String newEmail) {
        if (!patrons.containsKey(email)) {
            AppLogger.info(getClass(), "Patron with email " + email + " not found.");
            return;
        }
        if (!email.equals(newEmail) && patrons.containsKey(newEmail)) {
            AppLogger.info(getClass(), "Email " + newEmail + " already in use.");
            return;
        }
        IPatron patron = patrons.get(email);
        patron.setName(newName);
        patron.setEmail(newEmail);
        patrons.put(newEmail, patron);
        patrons.remove(email);
        AppLogger.info(getClass(), "Patron with email " + email + " updated successfully.");
    }

    @Override
    public void borrowBook(String email, String bookIsbn) {
        if (!patrons.containsKey(email)) {
            AppLogger.info(getClass(), "Patron with email " + email + " not found.");
            return;
        }
        IPatron patron = patrons.get(email);
        if (patron.canBorrowMoreBooks()) {
            patron.addBook(bookIsbn);
        } else {
            AppLogger.info(getClass(), "Patron with email " + email + " cannot borrow more books.");
        }
    }

    @Override
    public void returnBook(String email, String bookIsbn) {
        if (!patrons.containsKey(email)) {
            AppLogger.info(getClass(), "Patron with email " + email + " not found.");
            return;
        }
        IPatron patron = patrons.get(email);
        if (patron.getBorrowedBookIsbnList().contains(bookIsbn)) {
            patron.removeBook(bookIsbn);
            AppLogger.info(getClass(), "Book with ISBN " + bookIsbn + " returned successfully by patron with email " + email + ".");
        } else {
            AppLogger.info(getClass(), "Book with ISBN " + bookIsbn + " not borrowed by patron with email " + email + ".");
        }
    }

    @Override
    public void getPatronDetails(String email) {
        if (!patrons.containsKey(email)) {
            AppLogger.info(getClass(), "Patron with email " + email + " not found.");
            return;
        }
        IPatron patron = patrons.get(email);
        AppLogger.info(getClass(),"Patron Details :"+patron.getDetails());
    }

    @Override
    public void getAllPatrons() {
        if (patrons.isEmpty()) {
            AppLogger.info(getClass(), "No patrons found.");
            return;
        }
        for (IPatron patron : patrons.values()) {
            AppLogger.info(getClass(), "Patron Details :" + patron.getDetails());
        }
    }

    @Override
    public void getPatronCount() {
        AppLogger.info(getClass(), "Total number of patrons: " + patronCount);
    }

    @Override
    public void getPatronBorrowedBooks(String email) {
        if (!patrons.containsKey(email)) {
            AppLogger.info(getClass(), "Patron with email " + email + " not found.");
            return;
        }
        IPatron patron = patrons.get(email);
        if (patron.getBorrowedBookIsbnList().isEmpty()) {
            AppLogger.info(getClass(), "No books borrowed by patron with email " + email + ".");
            return;
        }
        AppLogger.info(getClass(), "Books borrowed by patron with email " + email + ": " + patron.getBorrowedBookIsbnList());
    }
}
