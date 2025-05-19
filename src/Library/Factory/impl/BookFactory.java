package Library.Factory.impl;

import Library.Factory.api.IBookFactory;
import Library.model.api.IBook;
import Library.model.impl.Book;

public class BookFactory implements IBookFactory {
    @Override
    public IBook createBook( String isbn,String title, String author,int publicationYear) {
        return new Book(title, author, isbn, publicationYear);
    }
}
