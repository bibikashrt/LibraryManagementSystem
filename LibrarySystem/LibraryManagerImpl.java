
import java.util.List;

public class LibraryManagerImpl implements LibraryManager {

    private final BookRepository repository;

    public LibraryManagerImpl(BookRepository repository) {
        this.repository = repository;
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
    public List<Book> searchBook(String value) {
        return repository.searchBooks(value);
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
