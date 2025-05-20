package Library.Factory.api;

import Library.model.api.IBook;

public interface IBookFactory {
    IBook createBook( String isbn, String title, String author,int publicationYear);
}
