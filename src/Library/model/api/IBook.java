package Library.model.api;

import java.util.List;

public interface IBook {
    String getTitle();
    String getAuthor();
    String getIsbn();
    Integer getPublicationYear();

    List<String> getBookDetails();

    void setTitle(String title);
    void setAuthor(String author);
    void setPublicationYear(Integer publicationYear);
    void setBorrowed(boolean borrowed);
    boolean isBorrowed();
    void checkoutBook();
    void returnBook();
    boolean isAvailable();
}
