
import java.io.*;
import java.util.*;

public class CsvBookRepository implements BookRepository {

    private final String FILE_NAME;
    private final List<String> headers = Arrays.asList("BookID", "BookName", "Author", "Category", "Year");

    public CsvBookRepository(String fileName) {
        this.FILE_NAME = fileName;
        ensureHeaderExists();
    }

    private void ensureHeaderExists() {

        File file = new File(FILE_NAME);

        try {

            if (!file.exists() || file.length() == 0) {

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {

                    bw.write(String.join(",", headers));

                    bw.newLine();
                }

            }

        } catch (IOException e) {

            throw new RuntimeException("Error creating file", e);
        }
    }

    @Override
    public void addBook(Book book) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            bw.write(book.toCSV());

            bw.newLine();

        } catch (IOException e) {

            throw new RuntimeException("Error adding file", e);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                books.add(Book.fromCSV(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading books", e);
        }
        books.sort(Comparator.comparingInt(Book::getBookId));
        return books;
    }

    @Override
    public List<Book> searchBooks(String field, String value) {

        List<Book> results = new ArrayList<>();

        for (Book b : getAllBooks()) {

            if (b.matches(field, value)) {

                results.add(b);
            }

        }

        return results;
    }

    @Override
    public void updateBook(Book updatedBook) {

        List<Book> books = getAllBooks();

        for (int i = 0; i < books.size(); i++) {

            if (books.get(i).getBookId() == updatedBook.getBookId()) {

                books.set(i, updatedBook);

                break;
            }
        }

        saveAll(books);
    }

    @Override
    public void deleteBook(Book selectedBook) {

        List<Book> books = getAllBooks();

        books.removeIf(b -> b.getBookId() == selectedBook.getBookId());

        saveAll(books);
    }

    private void saveAll(List<Book> books) {
        books.sort(Comparator.comparingInt(Book::getBookId));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(String.join(",", headers));
            bw.newLine();
            for (Book b : books) {
                bw.write(b.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving books", e);
        }
    }
}
