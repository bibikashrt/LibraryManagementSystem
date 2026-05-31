
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookRepository repository = new CsvBookRepository("books.csv");

        LibraryManager library = new LibraryManagerImpl(repository);

        MenuOption choice;

        do {
            System.out.println("\n========Library System========");

            System.out.println("\n1.Add Book");
            System.out.println("2.View Book");
            System.out.println("3.Update Book");
            System.out.println("4.Delete Book");
            System.out.println("5.Search Book");
            System.out.println("6.Exit");

            choice = MenuOption.fromInt(readInt(sc, "Enter the choice:"));

            if (choice == null) {

                System.out.println("Invalid choice. Please enter a number between 1 and 6.");

                continue;
            }

            switch (choice) {

                case ADD_BOOK:

                    int id;

                    while (true) {

                        id = readInt(sc, "Book ID: ");

                        if (id <= 0) {
                            System.out.println("Book ID must be a positive number.");
                        } else if (library.isDuplicateBookId(id)) {

                            System.out.println("Duplicate ID. Please enter a different ID.");
                        } else {

                            break;
                        }
                    }

                    String name = readNonBlank(sc, "Name:");

                    String author = readNonBlank(sc, "Author:");

                    String category = readNonBlank(sc, "Category:");

                    int year = readYear(sc, "Year:");

                    library.addBook(
                            new Book(
                                    id,
                                    name,
                                    author,
                                    category,
                                    year
                            ));

                    System.out.println("Book added successfully");

                    break;

                case VIEW_BOOKS:

                    List<Book> books = library.viewBooks();

                    if (books.isEmpty()) {

                        System.out.println("No books available");

                        break;
                    }

                    books.forEach(System.out::println);

                    break;

                case SEARCH_BOOK:

                    handleSearch(sc, library);

                    break;

                case UPDATE_BOOK:

                    handleUpdate(sc, library);
                    break;

                case DELETE_BOOK:

                    handleDelete(sc, library);

                    break;

                case EXIT:

                    System.out.println("Goodbye! Thank you for using the Library System.");

                    break;
            }

        } while (choice != MenuOption.EXIT);

        sc.close();
    }

    static void handleSearch(Scanner sc, LibraryManager library) {

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

    static void handleDelete(Scanner sc, LibraryManager library) {

        System.out.println("\nFind the book you want to delete");

        System.out.print("Search book to delete: ");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(value);

        if (books.isEmpty()) {

            System.out.println("No matching books found");

            return;
        }

        books.forEach(System.out::println);

        int id = readInt(sc, "Select Book ID:");

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

        ConfirmationOption confirm = ConfirmationOption.fromInt(readInt(sc, "Confirm:"));

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

    static void handleUpdate(Scanner sc, LibraryManager library) {

        System.out.println("\nWhat do you want to update?:");

        System.out.println("1.BookName");

        System.out.println("2.Author");

        System.out.println("3.Category");

        System.out.println("4.Year");

        System.out.println("5.All");

        UpdateOption option = UpdateOption.fromInt(readInt(sc, "Choose option:"));

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

        int id = readInt(sc, "Select Book ID:");

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

                name = readNonBlank(sc, "New Name: ");

                break;

            case AUTHOR:

                author = readNonBlank(sc, "New Author: ");

                break;

            case CATEGORY:

                category = readNonBlank(sc, "New Category: ");

                break;

            case YEAR:

                year = (readYear(sc, "New Year:"));

                break;

            case ALL:

                name = readNonBlank(sc, "Name: ");
                author = readNonBlank(sc, "Author: ");
                category = readNonBlank(sc, "Category: ");
                year = readYear(sc, "Year: ");

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

    static int readInt(Scanner sc, String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Invalid. Please enter a number.");
            }
        }
    }

    static String readNonBlank(Scanner sc, String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {

                return input;
            }

            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    static int readYear(Scanner sc, String prompt) {

        while (true) {

            int year = readInt(sc, prompt);

            if (year >= 1000 && year <= 2100) {

                return year;
            }

            System.out.println("Please enter a valid year between 1000 and 2100.");
        }
    }

}
