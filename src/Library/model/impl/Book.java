package Library.model.impl;

import Library.model.api.IBook;
import Library.utils.AppLogger;

import java.util.List;

public class Book implements IBook {
    private String title;
    private String author;
    private final String isbn;
    private Integer publicationYear;
    private boolean isBorrowed;

    public Book(String title, String author, String isbn, Integer publicationYear) {
        this.isbn = isbn;
        setTitle(title);
        setAuthor(author);
        setPublicationYear(publicationYear);
        setBorrowed(false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    @Override
    public List<String> getBookDetails() {
        return List.of("Title: " + getTitle(), "Author: " + getAuthor(), "ISBN: " + getIsbn(),
                "Publication Year: " + getPublicationYear(), "Is Borrowed: " + isBorrowed());
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    @Override
    public void checkoutBook() {
        if(isAvailable()){
            setBorrowed(true);
            AppLogger.info(getClass(), "Book checked out successfully.");
        } else {
            AppLogger.info(getClass(), "Book is already checked out.");
        }
    }

    @Override
    public void returnBook() {
        if(isAvailable()){
            AppLogger.info(getClass(), "Book is already returned.");
        } else {
            setBorrowed(false);
            AppLogger.info(getClass(), "Book returned successfully.");
        }
    }

    @Override
    public boolean isAvailable() {
        return !isBorrowed();
    }
}
