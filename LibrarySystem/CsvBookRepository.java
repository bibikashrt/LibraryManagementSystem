
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class CsvBookRepository implements BookRepository {

    private static final String DELIMITER = "|";

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

                    bw.write(String.join(DELIMITER, headers));

                    bw.newLine();
                }

            }

        } catch (IOException e) {

            throw new RuntimeException("Error creating file" + FILE_NAME, e);
        }
    }

    @Override
    public void addBook(Book book) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            bw.write(toCSV(book));

            bw.newLine();

        } catch (IOException e) {

            throw new RuntimeException("Unable to add book to file: " + FILE_NAME, e);
        }
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {

                try {
                    books.add(fromCsv(line));
                } catch (Exception e) {
                    System.err.println("Warning: Skipping corrupt line in CSV: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading books", e);
        }
        books.sort(Comparator.comparingInt(Book::getBookId));
        return books;
    }

    @Override
    public List<Book> searchBooks(String value) {

        List<Book> results = new ArrayList<>();

        for (Book b : getAllBooks()) {

            if (b.matches(value)) {

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

        String tempFileName = FILE_NAME + ".tmp";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFileName))) {
            bw.write(String.join(DELIMITER, headers));
            bw.newLine();
            for (Book b : books) {
                bw.write(toCSV(b));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing temporary file", e);
        }

        File tempFile = new File(tempFileName);
        File originalFile = new File(FILE_NAME);

        try {
            Files.move(tempFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error replacing books file after save", e);
        }
    }

    private String toCSV(Book book) {
        return book.getBookId()
                + DELIMITER
                + book.getBookName()
                + DELIMITER
                + book.getAuthorName()
                + DELIMITER
                + book.getCategory()
                + DELIMITER
                + book.getPublicationYear();

    }

    private Book fromCsv(String line) {

        String[] parts = line.split("\\|");

        return new Book(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                Integer.parseInt(parts[4].trim())
        );
    }
}
