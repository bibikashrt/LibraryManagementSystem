
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

                System.out.println("Invalid");

                continue;
            }

            switch (choice) {

                case ADD_BOOK:

                    int id;

                    while (true) {

                        id = readInt(sc, "Book ID: ");

                        if (library.isDuplicateBookId(id)) {

                            System.out.println("Duplicate ID");
                        } else {

                            break;
                        }
                    }

                    System.out.print("Name:");

                    String name = sc.nextLine();

                    System.out.print("Author:");

                    String author = sc.nextLine();

                    System.out.print("Category:");

                    String category = sc.nextLine();

                    int year = readInt(sc, "Year:");

                    library.addBook(
                            new Book(
                                    id,
                                    name,
                                    author,
                                    category,
                                    year
                            ));

                    System.out.println("Added");

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

                    System.out.println("Exiting...");
            }

        } while (choice
                != MenuOption.EXIT);
    }

    static void handleSearch(Scanner sc, LibraryManager library) {

        String field = askField(sc);

        System.out.print("Enter search value:");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(field, value);

        if (books.isEmpty()) {

            System.out.println("Not matching books found");

            return;
        }

        books.forEach(System.out::println);
    }

    static void handleDelete(Scanner sc, LibraryManager library) {

        String field = askField(sc);

        System.out.print("Enter value to delete:");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(field, value);

        if (books.isEmpty()) {

            System.out.println("Not matching books found");

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

        System.out.println("1.Yes");

        System.out.println("2.No");

        ConfirmationOption confirm = ConfirmationOption.fromInt(readInt(sc, "Confirm:"));

        if (confirm == ConfirmationOption.YES) {

            library.deleteBook(selected);

            System.out.println("Deleted");
        }

    }

    static void handleUpdate(Scanner sc, LibraryManager library) {

        String field = askField(sc);

        System.out.print("Enter value to update:");

        String value = sc.nextLine();

        List<Book> books = library.searchBook(field, value);

        if (books.isEmpty()) {

            System.out.println("Not matching books found");

            return;
        }

        books.forEach(System.out::println);

        int id = readInt(sc, "Select Book ID:");

        Book b = books.stream().filter(x -> x.getBookId() == id).findFirst().orElse(null);

        if (b == null) {

            System.out.println("Invalid ID selected."
            );

            return;
        }

        System.out.println("1.BookName");

        System.out.println("2.Author");

        System.out.println("3.Category");

        System.out.println("4.Year");

        System.out.println("5.All");

        UpdateOption option = UpdateOption.fromInt(readInt(sc, "Choose:"));

        String name = b.getBookName();
        String author = b.getAuthorName();
        String category = b.getCategory();
        int year = b.getPublicationYear();

        switch (option) {

            case BookNAME:

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

        int c = readInt(sc, "Search By:");

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

                System.out.println("Invalid");
            }
        }
    }

}
