
import java.util.*;

public class LibraryConsoleApp {

    private final Scanner sc;
    private final LibraryManager library;

    public LibraryConsoleApp(Scanner sc, LibraryManager library) {
        this.sc = sc;
        this.library = library;
    }

    public void run() {
        MenuOption choice;
        do {
            System.out.println("\n========Library System========");

            System.out.println("\n1.Add Book");
            System.out.println("2.View Book");
            System.out.println("3.Update Book");
            System.out.println("4.Delete Book");
            System.out.println("5.Search Book");
            System.out.println("6.Exit");

            choice = MenuOption.fromInt(readInt("Enter the choice:"));

            if (choice == null) {

                System.out.println("Invalid choice. Please enter a number between 1 and 6.");

                continue;
            }

            switch (choice) {

                case ADD_BOOK:
                    handleAddBook();
                    break;

                case VIEW_BOOKS:
                    handleViewBooks();
                    break;

                case SEARCH_BOOK:
                    handleSearch();
                    break;

                case UPDATE_BOOK:
                    handleUpdate();
                    break;

                case DELETE_BOOK:
                    handleDelete();
                    break;

                case EXIT:
                    System.out.println("Goodbye! Thank you for using the Library System.");
                    break;
            }

        } while (choice != MenuOption.EXIT);
        sc.close();

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
        String name = readNonBlank("Name:");

        String author = readNonBlank("Author:");

        String category = readNonBlank("Category:");

        int year = readYear("Year:");

        library.addBook(
                new Book(
                        id,
                        name,
                        author,
                        category,
                        year
                ));

        System.out.println("Book added successfully");

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

        String value = sc.nextLine();

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

        Book selected = books.stream()
                .filter(b -> b.getBookId() == id)
                .findFirst()
                .orElse(null);

        if (selected == null) {

            System.out.println("Invalid ID selected.");

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

        Book b = books.stream().filter(x -> x.getBookId() == id).findFirst().orElse(null);

        if (b == null) {
            System.out.println("Invalid ID selected.");
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

                name = readNonBlank("New Name: ");

                break;

            case AUTHOR:

                author = readNonBlank("New Author: ");

                break;

            case CATEGORY:

                category = readNonBlank("New Category: ");

                break;

            case YEAR:

                year = (readYear("New Year:"));

                break;

            case ALL:

                name = readNonBlank("Name: ");
                author = readNonBlank("Author: ");
                category = readNonBlank("Category: ");
                year = readYear("Year: ");

                break;
        }
        Book updatedBook = new Book(
                b.getBookId(),
                name,
                author,
                category,
                year
        );

        library.updateBook(updatedBook);

        System.out.println("Updated");
    }

    private int readInt(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Invalid. Please enter a number.");
            }
        }
    }

    private String readNonBlank(String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {

                return input;
            }

            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    private int readYear(String prompt) {

        while (true) {

            int year = readInt(prompt);

            if (year >= 1000 && year <= 2100) {

                return year;
            }

            System.out.println("Please enter a valid year between 1000 and 2100.");
        }
    }
}
