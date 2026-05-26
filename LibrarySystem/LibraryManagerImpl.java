
import java.util.List;

public class LibraryManagerImpl implements LibraryManager {

    private final BookRepository repository;

    public LibraryManagerImpl() {
        this.repository = new CsvBookRepository("books.csv");
    }

    @Override
    public boolean isDuplicateBookId(int id) {
        return repository.getAllBooks().stream()
                .anyMatch(b -> b.getBookId() == id);
    }

    @Override
    public void addBook(Book book) {
        repository.addBook(book);
    }

    @Override
    public List<Book> viewBooks() {
        return repository.getAllBooks();
    }

    @Override
    public List<Book> searchBook(String field, String value) {
        return repository.searchBooks(field, value);
    }

    @Override
    public void updateBook(Book book) {
        repository.updateBook(book);
    }

    @Override
    public void deleteBook(Book book) {
        repository.deleteBook(book);
    }
}
