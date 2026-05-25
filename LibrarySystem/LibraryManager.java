
public interface LibraryManager {

    boolean isDuplicateBookId(int id);

    void addBook(Book book);

    void viewBooks();

    void searchBook(String value);

    void updateBook(String searchvalue,
            String newValue);

    void deleteBook(String value);
}
