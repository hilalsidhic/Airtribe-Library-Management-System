package Library.model.api;

import java.util.List;

public interface IPatron {
    void setName(String name);
    String getName();
    void setPhoneNumber(String phoneNumber);
    String getPhoneNumber();
    void setEmail(String email);
    String getEmail();
    void addBook(String bookIsbn);
    void removeBook(String bookIsbn);
    int getBorrowedBooksCount();
    List<String> getBorrowedBookIsbnList();
    boolean canBorrowMoreBooks();
    List<String> getDetails();
}
