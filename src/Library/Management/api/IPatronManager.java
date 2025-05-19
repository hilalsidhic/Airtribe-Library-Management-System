package Library.Management.api;

import Library.model.api.IPatron;

public interface IPatronManager {

    void addPatron(IPatron patron);
    void removePatron(String email);
    void updatePatron(String email, String newName, String newEmail);

    void borrowBook(String email, String bookIsbn);
    void returnBook(String email, String bookIsbn);

    void getPatronDetails(String email);
    void getAllPatrons();
    void getPatronCount();
    void getPatronBorrowedBooks(String email);
}
