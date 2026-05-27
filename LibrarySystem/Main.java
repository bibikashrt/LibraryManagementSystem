
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        LibraryManager library = new LibraryManagerImpl();

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

                            System.out.println("Duplicate ID. Please enetr a different ID.");
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
            }

        } while (choice != MenuOption.EXIT);
    }

    static void handleSearch(Scanner sc, LibraryManager library) {

        String field = askField(sc);

        System.out.println("\nSearch books");

        System.out.print("Enter book information: ");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(field, value);

        if (books.isEmpty()) {

            System.out.println("No matching books found");

            return;
        }

        books.forEach(System.out::println);
    }

    static void handleDelete(Scanner sc, LibraryManager library) {

        String field = askField(sc);

        System.out.println("\nFind the book you want to delete");

        System.out.print("Search book to delete: ");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(field, value);

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

            System.out.println("Deleted");
        }

    }

    static void handleUpdate(Scanner sc, LibraryManager library) {

        String field = askField(sc);

        System.out.println("\nFind the book you want to update");

        System.out.print("Search book to update: ");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(field, value);

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

        System.out.println("1.BookName");

        System.out.println("2.Author");

        System.out.println("3.Category");

        System.out.println("4.Year");

        System.out.println("5.All");

        UpdateOption option = UpdateOption.fromInt(readInt(sc, "Choose option. Please select number between 1 to 5:"));

        if (option == null) {

            System.out.println("Invalid choice. Update cancelled.");

            return;
        }

        String name = b.getBookName();
        String author = b.getAuthorName();
        String category = b.getCategory();
        int year = b.getPublicationYear();

        switch (option) {

            case BOOK_NAME:

                System.out.print("New Name:");

                name = (sc.nextLine());

                break;

            case AUTHOR:

                System.out.print("New Author:");

                author = (sc.nextLine());

                break;

            case CATEGORY:

                System.out.print("New Category:");

                category = (sc.nextLine());

                break;

            case YEAR:

                year = (readInt(sc, "New Year:"));

                break;

            case ALL:

                System.out.print("Name:");

                name = (sc.nextLine());

                System.out.print("Author:");

                author = (sc.nextLine());

                System.out.print("Category:");

                category = (sc.nextLine());

                year = (readInt(sc, "Year:"));
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

    static String askField(Scanner sc) {

        System.out.println("1.ID");
        System.out.println("2.Name");
        System.out.println("3.Author");
        System.out.println("4.Category");
        System.out.println("5.Year");

        int c = readInt(sc, "Search option. Please enter number between 1 to 5:");

        switch (c) {

            case 1:
                return "id";
            case 2:
                return "name";
            case 3:
                return "author";
            case 4:
                return "category";
            case 5:
                return "year";

            default:
                return "name";
        }
    }

    static int readInt(Scanner sc, String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Integer.parseInt(sc.nextLine());

            } catch (Exception e) {

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
