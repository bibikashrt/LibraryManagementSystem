
import java.util.*;
import repository.BookRepository;
import repository.BorrowRepository;
import repository.DatabaseBookRepository;
import repository.DatabaseBorrowRepository;
import repository.DatabaseStudentRepository;
import repository.StudentRepository;
import service.LibraryManager;
import service.LibraryManagerImpl;
import ui.LibraryConsoleApp;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BookRepository bookrepository = new DatabaseBookRepository();

        StudentRepository studentRepository = new DatabaseStudentRepository();

        BorrowRepository borrowRepository = new DatabaseBorrowRepository();

        LibraryManager library = new LibraryManagerImpl(bookrepository, studentRepository, borrowRepository);

        LibraryConsoleApp app = new LibraryConsoleApp(sc, library);

        app.run();

        sc.close();

    }

}
