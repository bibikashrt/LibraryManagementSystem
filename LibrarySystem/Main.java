
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BookRepository repository = new DatabaseBookRepository();

        LibraryManager library = new LibraryManagerImpl(repository);

        LibraryConsoleApp app = new LibraryConsoleApp(sc, library);

        app.run();

        sc.close();

    }

}
