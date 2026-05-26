
import java.util.List;

public interface BookRepository {

    void addBook(Book book);

    List<Book> getAllBooks();

    List<Book> searchBooks(String field, String value);

    void updateBook(Book book);

    void deleteBook(Book book);
}
