package service;

import java.time.LocalDate;
import java.util.List;
import model.Book;
import model.BorrowRecord;
import model.Student;
import repository.BookRepository;
import repository.BorrowRepository;
import repository.StudentRepository;

public class LibraryManagerImpl implements LibraryManager {

    private final BookRepository bookrepository;

    private final StudentRepository studentRepository;
    private final BorrowRepository borrowRepository;

    public LibraryManagerImpl(BookRepository bookrepository, StudentRepository studentRepository, BorrowRepository borrowRepository) {
        this.bookrepository = bookrepository;
        this.studentRepository = studentRepository;
        this.borrowRepository = borrowRepository;
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

    @Override
    public void borrowBook(BorrowRecord record) {

        borrowRepository.borrowBook(record);
    }

    @Override
    public void returnBook(int borrowId, LocalDate returnDate) {

        borrowRepository.returnBook(borrowId, returnDate);
    }

    @Override
    public List<BorrowRecord> viewBorrowRecords() {

        return borrowRepository.getAllBorrowRecords();
    }

    @Override
    public List<BorrowRecord> viewOverdueBorrowRecords() {

        return borrowRepository.getOverdueBorrowRecords();
    }

    @Override
    public boolean isBorrowRecordExists(int borrowId) {

        return borrowRepository.existsByBorrowId(borrowId);
    }
}
