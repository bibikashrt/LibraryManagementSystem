
// import java.io.IOException;
// import java.net.URI;

// import org.glassfish.grizzly.http.server.HttpServer;
// import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

// import config.LibraryApplication;

// public class ServerMain {

//     public static final String BASE_URI = "http://localhost:8080/";

//     public static HttpServer startServer() {

//         return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new LibraryApplication());
//     }

//     public static void main(String[] args) throws IOException {

//         HttpServer server = startServer();

//         System.out.println("Server Started : " + BASE_URI);

//         System.out.println("Press Enter to Stop Server...");

//         System.in.read();

//         server.shutdownNow();
//     }
// }
