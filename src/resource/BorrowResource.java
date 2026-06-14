package resource;

import java.time.LocalDate;
import java.util.List;

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
import repository.BookRepository;
import repository.BorrowRepository;
import repository.DatabaseBookRepository;
import repository.DatabaseBorrowRepository;
import repository.DatabaseStudentRepository;
import repository.StudentRepository;
import service.LibraryManager;
import service.LibraryManagerImpl;

@Path("/borrow-records")
public class BorrowResource {

    private final LibraryManager library;

    public BorrowResource() {

        BookRepository bookRepository
                = new DatabaseBookRepository();

        StudentRepository studentRepository
                = new DatabaseStudentRepository();

        BorrowRepository borrowRepository
                = new DatabaseBorrowRepository();

        library
                = new LibraryManagerImpl(
                        bookRepository,
                        studentRepository,
                        borrowRepository);
    }

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
    public Response borrowBook(BorrowRecord record) {

        if (library.isBorrowRecordExists(
                record.getBorrowId())) {

            return Response.status(
                    Response.Status.CONFLICT)
                    .entity("Borrow ID already exists.")
                    .build();
        }

        if (!library.isBookAvailable(
                record.getBookId())) {

            return Response.status(
                    Response.Status.CONFLICT)
                    .entity("Book is already borrowed.")
                    .build();
        }

        library.borrowBook(record);

        return Response.status(
                Response.Status.CREATED)
                .entity("Book borrowed successfully.")
                .build();
    }

    @PUT
    @Path("/{borrowId}/return")
    @Produces(MediaType.TEXT_PLAIN)
    public Response returnBook(
            @PathParam("borrowId") int borrowId) {

        if (!library.isBorrowRecordExists(
                borrowId)) {

            return Response.status(
                    Response.Status.NOT_FOUND)
                    .entity("Borrow record not found.")
                    .build();
        }

        library.returnBook(
                borrowId,
                LocalDate.now());

        return Response.ok(
                "Book returned successfully.")
                .build();
    }
}
