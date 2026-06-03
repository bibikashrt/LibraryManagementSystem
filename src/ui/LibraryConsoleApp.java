package ui;

import enums.BorrowStatus;
import enums.ConfirmationOption;
import enums.MenuOption;
import enums.StudentUpdateOption;
import enums.UpdateOption;
import exception.LibraryException;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import model.Book;
import model.BorrowRecord;
import model.Student;
import service.LibraryManager;

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

                    case ADD_STUDENT:
                        handleAddStudent();
                        break;

                    case VIEW_STUDENTS:
                        handleViewStudents();
                        break;

                    case SEARCH_STUDENT:
                        handleSearchStudent();
                        break;

                    case UPDATE_STUDENT:
                        handleUpdateStudent();
                        break;

                    case DELETE_STUDENT:
                        handleDeleteStudent();
                        break;

                    case BORROW_BOOK:
                        handleBorrowBook();
                        break;

                    case RETURN_BOOK:
                        handleReturnBook();
                        break;

                    case VIEW_BORROW_RECORDS:
                        handleViewBorrowRecords();
                        break;

                    case VIEW_OVERDUE_BOOKS:
                        handleViewOverdueBooks();
                        break;

                    case EXIT:
                        System.out.println("Goodbye! Thank you for using the Library System.");
                        break;
                }
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
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

        System.out.println("6. Add Student");
        System.out.println("7. View Students");
        System.out.println("8. Search Student");
        System.out.println("9. Update Student");
        System.out.println("10. Delete Student");

        System.out.println("11. Borrow Book");
        System.out.println("12. Return Book");
        System.out.println("13. View Borrow Records");
        System.out.println("14. View Overdue Books");

        System.out.println("15. Exit");

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

        for (Book book : books) {

            String status = library.isBookAvailable(
                    book.getBookId())
                    ? "Available"
                    : "Borrowed";

            System.out.println(
                    book
                    + " | Status: "
                    + status);
        }
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
        for (Book book : books) {

            String status
                    = library.isBookAvailable(
                            book.getBookId())
                    ? "Available"
                    : "Borrowed";

            System.out.println(
                    book
                    + " | Status: "
                    + status);
        }

    }

    private void handleDelete() {

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

        if (confirmAction("Delete this book?")) {
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

    private void handleAddStudent() {

        int id;

        while (true) {

            id = readInt("Student ID: ");

            if (id <= 0) {

                System.out.println("Student ID must be positive.");

            } else if (library.isDuplicateStudentId(id)) {

                System.out.println("Duplicate ID. Please enter a different ID.");

            } else {

                break;
            }
        }

        String name = readNonBlank("Student Name: ", Student.MAX_NAME_LENGTH);

        String faculty = readNonBlank("Faculty: ", Student.MAX_FACULTY_LENGTH);

        String batch = readNonBlank("Batch: ", Student.MAX_BATCH_LENGTH);

        try {
            library.addStudent(new Student(id, name, faculty, batch));
            System.out.println("Student added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void handleViewStudents() {

        List<Student> students = library.viewStudents();

        if (students.isEmpty()) {

            System.out.println("No students available.");

            return;
        }

        students.forEach(System.out::println);
    }

    private void handleSearchStudent() {

        System.out.println("\nSearch Student");

        System.out.print("Enter student information: ");

        String value = sc.nextLine().trim();

        if (value.isEmpty()) {

            System.out.println("Please enter a search term.");

            return;
        }

        List<Student> students = library.searchStudent(value);

        if (students.isEmpty()) {

            System.out.println("No matching students found.");

            return;
        }

        students.forEach(System.out::println);
    }

    private void handleUpdateStudent() {

        System.out.println("\nWhat do you want to update?");

        System.out.println("1. Name");
        System.out.println("2. FACULTY");
        System.out.println("3. BATCH");
        System.out.println("4. All");

        StudentUpdateOption option = StudentUpdateOption.fromInt(readInt("Choose option: "));

        if (option == null) {

            System.out.println("Invalid choice. Update cancelled.");

            return;
        }

        System.out.print("\nEnter student information to search: ");

        String value = sc.nextLine();

        List<Student> students = library.searchStudent(value);

        if (students.isEmpty()) {

            System.out.println("No matching students found.");

            return;
        }

        students.forEach(System.out::println);

        int id = readInt("Select Student ID: ");

        Student selected = students.stream()
                .filter(s
                        -> s.getStudentId() == id)
                .findFirst()
                .orElse(null);

        if (selected == null) {

            System.out.println("That ID was not in the search results.");

            return;
        }

        System.out.println("\nSelected Student:");

        System.out.println(selected);

        String name = selected.getStudentName();

        String faculty = selected.getFaculty();

        String batch = selected.getBatch();

        switch (option) {

            case NAME:

                name = readNonBlank("New Name: ", Student.MAX_NAME_LENGTH);

                break;

            case FACULTY:

                faculty = readNonBlank("New Email: ", Student.MAX_FACULTY_LENGTH);

                break;

            case BATCH:

                batch = readNonBlank("New BATCH: ", Student.MAX_BATCH_LENGTH);

                break;

            case ALL:

                name = readNonBlank("Name: ", Student.MAX_NAME_LENGTH);

                faculty = readNonBlank("FACULTY: ", Student.MAX_FACULTY_LENGTH);

                batch = readNonBlank("BATCH: ", Student.MAX_BATCH_LENGTH);

                break;
        }

        library.updateStudent(
                new Student(
                        selected.getStudentId(),
                        name,
                        faculty,
                        batch));

        System.out.println("Student updated successfully.");
    }

    private void handleDeleteStudent() {

        System.out.print("\nSearch student to delete: ");

        List<Student> students = library.searchStudent(sc.nextLine());

        if (students.isEmpty()) {
            System.out.println("No matching students found.");
            return;
        }
        students.forEach(System.out::println);

        int id = readInt("Select Student ID: ");

        Student selected = students.stream()
                .filter(s
                        -> s.getStudentId() == id)
                .findFirst()
                .orElse(null);

        if (selected == null) {

            System.out.println("That ID was not in the search results.");

            return;
        }

        System.out.println(selected);

        if (confirmAction("Delete this student?")) {
            library.deleteStudent(selected);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void handleBorrowBook() {

        int studentId = readInt("Student ID: ");

        if (!library.isDuplicateStudentId(studentId)) {

            System.out.println("Student not found.");

            return;
        }

        // System.out.println("\nStudent Found:");
        // List<Student> students = library.searchStudent(String.valueOf(studentId));
        // students.forEach(System.out::println);
        System.out.print("\nSearch Book: ");
        String searchValue = sc.nextLine();

        List<Book> books = library.searchBook(searchValue);

        if (books.isEmpty()) {

            System.out.println("No matching books found.");

            return;
        }

        System.out.println("\nMatching Books:");

        for (Book book : books) {

            String status = library.isBookAvailable(
                    book.getBookId())
                    ? "Available"
                    : "Borrowed";

            System.out.println(
                    book
                    + " | Status: "
                    + status);
        }

        int bookId = readInt("\nSelect Book ID: ");

        Book selectedBook
                = books.stream()
                        .filter(book
                                -> book.getBookId()
                        == bookId)
                        .findFirst()
                        .orElse(null);

        if (selectedBook == null) {

            System.out.println("Book ID not found in search results.");

            return;
        }

        if (!library.isBookAvailable(bookId)) {

            System.out.println("Book is currently borrowed.");

            return;
        }

        LocalDate issueDate = LocalDate.now();

        LocalDate dueDate = issueDate.plusDays(14);

        BorrowRecord record = new BorrowRecord(
                0,
                studentId,
                bookId,
                issueDate,
                dueDate,
                null,
                BorrowStatus.BORROWED
        );

        library.borrowBook(record);

        System.out.println("\nBook borrowed successfully.");

        System.out.println("Issue Date : " + issueDate);

        System.out.println("Due Date   : " + dueDate);
    }

    private void handleReturnBook() {

        int borrowId = readInt("Borrow ID: ");

        if (!library.isBorrowRecordExists(borrowId)) {

            System.out.println("Borrow record not found.");

            return;
        }

        library.returnBook(borrowId, LocalDate.now());

        System.out.println("Book returned successfully.");
    }

    private void handleViewBorrowRecords() {

        List<BorrowRecord> records = library.viewBorrowRecords();

        if (records.isEmpty()) {

            System.out.println("No borrow records available.");

            return;
        }

        records.forEach(System.out::println);
    }

    private void handleViewOverdueBooks() {

        List<BorrowRecord> records = library.viewOverdueBorrowRecords();

        if (records.isEmpty()) {

            System.out.println("No overdue books.");

            return;
        }

        records.forEach(System.out::println);
    }

    private boolean confirmAction(String prompt) {
        System.out.println("\n" + prompt);
        System.out.println("1. Yes  2. No");
        ConfirmationOption confirm = ConfirmationOption.fromInt(readInt("Confirm: "));
        if (confirm == null) {
            System.out.println("Invalid choice. Action cancelled.");
            return false;
        }
        return confirm == ConfirmationOption.YES;
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
