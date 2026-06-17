package resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Book;
import service.LibraryManager;

@Path("/books")
public class BookResource {

    @Inject
    private LibraryManager library;

    // public BookResource() {
    //     BookRepository bookRepository = new DatabaseBookRepository();
    //     StudentRepository studentRepository = new DatabaseStudentRepository();
    //     BorrowRepository borrowRepository = new DatabaseBorrowRepository();
    //     library = new LibraryManagerImpl(
    //             bookRepository,
    //             studentRepository,
    //             borrowRepository
    //     );
    // }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooks() {

        return library.viewBooks();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addBook(Book book) {

        library.addBook(book);

        return Response.status(Response.Status.CREATED)
                .entity("Book added successfully")
                .build();
    }

    @GET
    @Path("/search/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> searchBooks(
            @PathParam("value") String value) {

        return library.searchBook(value);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateBook(
            @PathParam("id") int id,
            Book book) {

        if (id != book.getBookId()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Path ID and Book ID do not match.")
                    .build();
        }

        library.updateBook(book);

        return Response.ok("Book updated successfully.")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBook(
            @PathParam("id") int id) {

        List<Book> books = library.searchBook(String.valueOf(id));

        if (books.isEmpty()) {

            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found.")
                    .build();
        }

        library.deleteBook(books.getFirst());

        return Response.ok("Book deleted successfully.")
                .build();
    }
}
