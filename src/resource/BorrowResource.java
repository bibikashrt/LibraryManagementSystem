package resource;

import java.time.LocalDate;
import java.util.List;

import dto.BorrowBookRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.BorrowRecord;
import service.LibraryManager;

@Path("/borrow-records")
public class BorrowResource {

    @Inject
    private LibraryManager library;

//     public BorrowResource() {
//         BookRepository bookRepository
//                 = new DatabaseBookRepository();
//         StudentRepository studentRepository
//                 = new DatabaseStudentRepository();
//         BorrowRepository borrowRepository
//                 = new DatabaseBorrowRepository();
//         library
//                 = new LibraryManagerImpl(
//                         bookRepository,
//                         studentRepository,
//                         borrowRepository);
//     }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowRecord> getBorrowRecords() {

        return library.viewBorrowRecords();
    }

    @GET
    @Path("/overdue")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowRecord> getOverdueBorrowRecords() {

        return library.viewOverdueBorrowRecords();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response borrowBook(BorrowBookRequest request) {
        try {
            library.borrowBook(request.getStudentId(), request.getBookId());

            return Response.status(Response.Status.CREATED)
                    .entity("Book borrowed successfully.")
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to borrow book.")
                    .build();
        }
    }

    @PUT
    @Path("/{borrowId}/return")
    @Produces(MediaType.TEXT_PLAIN)
    public Response returnBook(@PathParam("borrowId") int borrowId) {
        try {
            library.returnBook(borrowId, LocalDate.now());

            return Response.ok("Book returned successfully.")
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to return book.")
                    .build();
        }
    }
}
