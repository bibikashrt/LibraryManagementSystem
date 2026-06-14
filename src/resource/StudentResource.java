package resource;

import java.util.List;

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
import model.Student;
import repository.BookRepository;
import repository.BorrowRepository;
import repository.DatabaseBookRepository;
import repository.DatabaseBorrowRepository;
import repository.DatabaseStudentRepository;
import repository.StudentRepository;
import service.LibraryManager;
import service.LibraryManagerImpl;

@Path("/students")
public class StudentResource {

    private final LibraryManager library;

    public StudentResource() {

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
                        borrowRepository
                );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudents() {

        return library.viewStudents();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addStudent(Student student) {

        library.addStudent(student);

        return Response.status(Response.Status.CREATED)
                .entity("Student added successfully.")
                .build();
    }

    @GET
    @Path("/search/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> searchStudents(
            @PathParam("value") String value) {

        return library.searchStudent(value);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateStudent(
            @PathParam("id") int id,
            Student student) {

        if (id != student.getStudentId()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Path ID and Student ID do not match.")
                    .build();
        }

        library.updateStudent(student);

        return Response.ok("Student updated successfully.")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteStudent(
            @PathParam("id") int id) {

        List<Student> students
                = library.searchStudent(String.valueOf(id));

        if (students.isEmpty()) {

            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Student not found.")
                    .build();
        }

        library.deleteStudent(students.get(0));

        return Response.ok("Student deleted successfully.")
                .build();
    }

}
