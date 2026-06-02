package service;

import java.util.List;
import model.Book;
import model.Student;
import repository.BookRepository;
import repository.StudentRepository;

public class LibraryManagerImpl implements LibraryManager {

    private final BookRepository bookrepository;

    private final StudentRepository studentRepository;

    public LibraryManagerImpl(BookRepository bookrepository, StudentRepository studentRepository) {
        this.bookrepository = bookrepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean isDuplicateBookId(int id) {
        return bookrepository.existsById(id);
    }

    @Override
    public void addBook(Book book) {
        bookrepository.addBook(book);
    }

    @Override
    public List<Book> viewBooks() {
        return bookrepository.getAllBooks();
    }

    @Override
    public List<Book> searchBook(String value) {
        return bookrepository.searchBooks(value);
    }

    @Override
    public void updateBook(Book book) {
        bookrepository.updateBook(book);
    }

    @Override
    public void deleteBook(Book book) {
        bookrepository.deleteBook(book);
    }

    @Override
    public boolean isDuplicateStudentId(int id) {

        return studentRepository.existsById(id);
    }

    @Override
    public void addStudent(Student student) {

        studentRepository.addStudent(student);
    }

    @Override
    public List<Student> viewStudents() {

        return studentRepository.getAllStudents();
    }

    @Override
    public List<Student> searchStudent(String value) {

        return studentRepository.searchStudents(value);
    }

    @Override
    public void updateStudent(Student student) {

        studentRepository.updateStudent(student);
    }

    @Override
    public void deleteStudent(Student student) {

        studentRepository.deleteStudent(student);
    }
}
