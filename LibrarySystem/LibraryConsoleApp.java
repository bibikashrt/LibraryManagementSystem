
import java.time.Year;
import java.util.*;

public class LibraryConsoleApp {

    private static final int MIN_YEAR = 1000;
    private static final int MAX_YEAR_OFFSET = 1;

    private final Scanner sc;
    private final LibraryManager library;

    public LibraryConsoleApp(Scanner sc, LibraryManager library) {
        this.sc = sc;
        this.library = library;
    }

    public void run() {
        MenuOption choice;

        do {
            printMenu();
            choice = MenuOption.fromInt(readInt("Enter the choice: "));

            if (choice == null) {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                continue;
            }

            try {
                switch (choice) {
                    case ADD_BOOK:
                        handleAddBook();
                        break;

                    case VIEW_BOOKS:
                        handleViewBooks();
                        break;

                    case UPDATE_BOOK:
                        handleUpdate();
                        break;

                    case DELETE_BOOK:
                        handleDelete();
                        break;

                    case SEARCH_BOOK:
                        handleSearch();
                        break;

                    case EXIT:
                        System.out.println("Goodbye! Thank you for using the Library System.");
                        break;
                }
            } catch (LibraryException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }

        } while (choice != MenuOption.EXIT);
    }

    private void printMenu() {
        System.out.println("\n========Library System========");
        System.out.println("\n1. Add Book");
        System.out.println("2. View Books");
        System.out.println("3. Update Book");
        System.out.println("4. Delete Book");
        System.out.println("5. Search Book");
        System.out.println("6. Exit");
    }

    private void handleAddBook() {

        int id;

        while (true) {

            id = readInt("Book ID: ");

            if (id <= 0) {

                System.out.println("Book ID must be a positive number.");

            } else if (library.isDuplicateBookId(id)) {

                System.out.println("Duplicate ID. Please enter a different ID.");

            } else {

                break;
            }
        }
        String name = readNonBlank("Name:", Book.MAX_NAME_LENGTH);

        String author = readNonBlank("Author:", Book.MAX_AUTHOR_LENGTH);

        String category = readNonBlank("Category:", Book.MAX_CATEGORY_LENGTH);

        int year = readYear("Year:");

        library.addBook(
                new Book(
                        id,
                        name,
                        author,
                        category,
                        year
                ));

        System.out.println("Book added successfully.");

    }

    private void handleViewBooks() {

        List<Book> books = library.viewBooks();

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        books.forEach(System.out::println);
    }

    private void handleSearch() {

        System.out.println("\nSearch books");

        System.out.print("Enter book information: ");

        String value = sc.nextLine().trim();
        if (value.isEmpty()) {
            System.out.println("Please enter a search term.");
            return;
        }

        List<Book> books = library.searchBook(value);

        if (books.isEmpty()) {

            System.out.println("No matching books found");

            return;
        }

        books.forEach(System.out::println);
    }

    private void handleDelete() {

        System.out.println("\nFind the book you want to delete");

        System.out.print("Search book to delete: ");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(value);

        if (books.isEmpty()) {

            System.out.println("No matching books found");

            return;
        }

        books.forEach(System.out::println);

        int id = readInt("Select Book ID:");

        Book selected = findById(books, id);

        if (selected == null) {

            System.out.println("That ID was not in the search results. Please select from the listed books.");

            return;
        }

        System.out.println(selected);

        System.out.println("\nAre you sure you want to delete this book?");

        System.out.println("1.Yes");

        System.out.println("2.No");

        ConfirmationOption confirm = ConfirmationOption.fromInt(readInt("Confirm:"));

        if (confirm == null) {
            System.out.println("Invalid choice. Deletion cancelled.");
            return;
        }

        if (confirm == ConfirmationOption.YES) {

            library.deleteBook(selected);

            System.out.println("Book deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }

    }

    private void handleUpdate() {

        System.out.println("\nWhat do you want to update?:");

        System.out.println("1.BookName");

        System.out.println("2.Author");

        System.out.println("3.Category");

        System.out.println("4.Year");

        System.out.println("5.All");

        UpdateOption option = UpdateOption.fromInt(readInt("Choose option:"));

        if (option == null) {

            System.out.println("Invalid choice. Update cancelled.");

            return;
        }

        System.out.print("\nEnter book information to search: ");
        String value = sc.nextLine();

        List<Book> books = library.searchBook(value);

        if (books.isEmpty()) {
            System.out.println("No matching books found");
            return;
        }

        books.forEach(System.out::println);

        int id = readInt("Select Book ID:");

        Book b = findById(books, id);

        if (b == null) {
            System.out.println("That ID was not in the search results. Please select from the listed books.");
            return;
        }

        System.out.println("\nSelected Book:");
        System.out.println(b);

        String name = b.getBookName();
        String author = b.getAuthorName();
        String category = b.getCategory();
        int year = b.getPublicationYear();

        switch (option) {

            case BOOK_NAME:

                name = readNonBlank("New Name: ", Book.MAX_NAME_LENGTH);

                break;

            case AUTHOR:

                author = readNonBlank("New Author: ", Book.MAX_AUTHOR_LENGTH);

                break;

            case CATEGORY:

                category = readNonBlank("New Category: ", Book.MAX_CATEGORY_LENGTH);

                break;

            case YEAR:

                year = (readYear("New Year:"));

                break;

            case ALL: {

                name = readNonBlank("Name: ", Book.MAX_NAME_LENGTH);
                author = readNonBlank("Author: ", Book.MAX_AUTHOR_LENGTH);
                category = readNonBlank("Category: ", Book.MAX_CATEGORY_LENGTH);
                year = readYear("Year: ");

                break;
            }
        }
        library.updateBook(new Book(b.getBookId(), name, author, category, year));

        System.out.println("Book updated successfully.");
    }

    private int readInt(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private String readNonBlank(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            } else if (input.length() > maxLength) {
                System.out.println("Input is too long (max " + maxLength + " characters). Please try again.");
            } else {
                return input;
            }
        }
    }

    private int readYear(String prompt) {

        int maxYear = Year.now().getValue() + MAX_YEAR_OFFSET;

        while (true) {

            int year = readInt(prompt);

            if (year >= MIN_YEAR && year <= maxYear) {

                return year;
            }

            System.out.println("Please enter a valid year between  " + MIN_YEAR + " and " + maxYear + ".");
        }
    }

    private Book findById(List<Book> books, int id) {
        return books.stream()
                .filter(b -> b.getBookId() == id)
                .findFirst()
                .orElse(null);
    }
}
