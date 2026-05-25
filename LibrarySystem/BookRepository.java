
import java.util.List;

public interface BookRepository {

    void addBook(Book book);

    List<Book> getAllBooks();

    List<Book> searchBooks(
            String value);

    void updateBook(
            String searchValue,
            String newValue);

    void deleteBook(String value);
}
