
import java.util.List;

public interface LibraryManager {

    boolean isDuplicateBookId(int id);

    void addBook(Book book);

    List<Book> viewBooks();

    List<Book> searchBook(String value);

    void updateBook(Book book);

    void deleteBook(Book book);
}
