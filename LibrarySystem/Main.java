
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LibraryManager library = new LibraryManagerImpl();
        MenuOption choice;

        System.out.println("Library Management System Started.");
        System.out.println("If books.csv did not exist, it has been created with header.");

        do {
            System.out.println("\n Menu");
            System.out.println("1. Add Book\n2. View Books\n3. Update Book\n4. Delete Book\n5. Search Book\n6. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter numbers only for menu choice");
                sc.next();
            }
            choice = MenuOption.fromInt(sc.nextInt());
            sc.nextLine();

            if (choice == null) {
                System.out.println("Invalid choice!");
                continue;
            }

            switch (choice) {
                case ADD_BOOK: {
                    int id;
                    while (true) {
                        id = readInt(sc, "Enter Book ID: ");
                        if (library.isDuplicateBookId(id)) {
                            System.out.println("ID already exists. Please enter a different Book ID.");
                        } else {
                            break;
                        }
                    }
                    System.out.print("Enter Book Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Author Name: ");
                    String author = sc.nextLine();
                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();
                    int year = readInt(sc, "Enter Publication Year: ");
                    library.addBook(new Book(id, name, author, category, year));
                    break;
                }

                case VIEW_BOOKS:
                    library.viewBooks();
                    break;

                case UPDATE_BOOK: {
                    System.out.print("Enter existing value: ");
                    String oldValue = sc.nextLine();
                    System.out.print("Enter new value: ");
                    String newValue = sc.nextLine();
                    library.updateBook(oldValue, newValue);
                    break;
                }

                case DELETE_BOOK: {
                    System.out.print("Enter value to delete: ");
                    String delValue = sc.nextLine();
                    library.deleteBook(delValue);
                    break;
                }

                case SEARCH_BOOK: {
                    System.out.print("Enter value to search: ");
                    String value = sc.nextLine();
                    library.searchBook(value);
                    break;
                }

                case EXIT:
                    System.out.println("Exiting program...");
                    break;
            }

        } while (choice != MenuOption.EXIT);

        sc.close();
    }

    private static int readInt(Scanner sc, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                value = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Try again.");
            }
        }
        return value;
    }
}
