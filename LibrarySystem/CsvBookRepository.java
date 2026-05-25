
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
                    bw.flush();
                    System.out.println("books.csv created with header.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error creating file with header: " + e.getMessage());
        }
    }

    @Override
    public void addBook(Book book) {
        ensureHeaderExists();
        try {
            List<Book> books = getAllBooks();
            books.add(book);
            saveAll(books);
            System.out.println("Book added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> searchBooks(String value) {

        List<Book> results = new ArrayList<>();

        for (Book b : getAllBooks()) {

            String field = b.matchesAnyField(value);

            if (field != null) {

                results.add(b);
            }
        }

        return results;
    }

    @Override
    public void updateBook(String searchValue, String newValue) {

        List<Book> books = getAllBooks();

        List<Book> matchedBooks = new ArrayList<>();

        List<String> matchedFields = new ArrayList<>();

        for (Book b : books) {

            String field = b.matchesAnyField(searchValue);

            if (field != null) {

                matchedBooks.add(b);

                matchedFields.add(field);
            }
        }

        if (matchedBooks.isEmpty()) {

            System.out.println("No matching book found");

            return;
        }

        Scanner sc = new Scanner(System.in);

        int choice = 1;

        if (matchedBooks.size() > 1) {

            System.out.println("Multiple matches found:");

            for (int i = 0; i < matchedBooks.size(); i++) {

                System.out.println(
                        (i + 1)
                        + ". "
                        + matchedFields.get(i)
                        + " -> "
                        + matchedBooks.get(i));
            }

            System.out.print("Choose option: ");

            choice = Integer.parseInt(sc.nextLine());
        }

        Book selectedBook = matchedBooks.get(choice - 1);

        String field = matchedFields.get(choice - 1);

        switch (field) {

            case "BookID":

                int newId = Integer.parseInt(newValue);

                boolean duplicate = books.stream().anyMatch(b -> b.getBookId() == newId);

                if (duplicate) {

                    System.out.println("ID already exists");

                    return;
                }

                selectedBook.setBookId(newId);

                break;

            case "BookName":

                selectedBook.setBookName(newValue);
                break;

            case "Author":

                selectedBook.setAuthorName(newValue);
                break;

            case "Category":

                selectedBook.setCategory(newValue);
                break;

            case "Year":

                selectedBook.setPublicationYear(Integer.parseInt(newValue));
                break;
        }

        saveAll(books);

        System.out.println("Update successful.");
    }

    @Override
    public void deleteBook(String value) {

        List<Book> books = getAllBooks();

        List<Book> matchedBooks = new ArrayList<>();

        List<String> matchedFields = new ArrayList<>();

        for (Book b : books) {

            String field = b.matchesAnyField(value);

            if (field != null) {

                matchedBooks.add(b);

                matchedFields.add(field);
            }
        }

        if (matchedBooks.isEmpty()) {

            System.out.println("No matching book found");

            return;
        }

        Scanner sc = new Scanner(System.in);

        int choice = 1;

        if (matchedBooks.size() > 1) {

            System.out.println("Multiple matches found:");

            for (int i = 0;
                    i < matchedBooks.size();
                    i++) {

                System.out.println(
                        (i + 1)
                        + ". "
                        + matchedFields.get(i)
                        + " -> "
                        + matchedBooks.get(i));
            }

            System.out.print("Choose option: ");

            choice = Integer.parseInt(sc.nextLine());
        }

        Book selectedBook = matchedBooks.get(choice - 1);

        books.remove(selectedBook);

        saveAll(books);

        System.out.println("Book deleted successfully.");
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
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
