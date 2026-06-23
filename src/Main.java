
import java.util.Scanner;

import service.LibraryManager;
import service.LibraryManagerImpl;
import ui.LibraryConsoleApp;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LibraryManager library = new LibraryManagerImpl();

        LibraryConsoleApp app = new LibraryConsoleApp(sc, library);
        app.run();

        sc.close();
    }
}
