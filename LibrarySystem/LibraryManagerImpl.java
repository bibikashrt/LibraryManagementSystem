
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
    public void viewBooks() {
        System.out.println("\nBook List");
        for (Book book : repository.getAllBooks()) {
            System.out.println(book);
        }
    }

    @Override
    public void searchBook(String value) {
        var results = repository.searchBooks(value);
        if (results.isEmpty()) {
            System.out.println("No matching book found");
        } else {
            results.forEach(System.out::println);
        }
    }

    @Override
    public void updateBook(String searchvalue, String newValue) {
        repository.updateBook(searchvalue, newValue);
    }

    @Override
    public void deleteBook(String value) {
        repository.deleteBook(value);
    }
}
