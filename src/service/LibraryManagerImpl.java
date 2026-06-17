package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import model.Book;
import model.BorrowRecord;
import model.Student;
import repository.BookRepository;
import repository.BorrowRepository;
import repository.StudentRepository;

@Singleton
public class LibraryManagerImpl implements LibraryManager {

    @Inject
    private BookRepository bookRepository;

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private BorrowRepository borrowRepository;

    public LibraryManagerImpl() {
    }

    @Inject
    public LibraryManagerImpl(BookRepository bookrepository, StudentRepository studentRepository, BorrowRepository borrowRepository) {
        this.bookRepository = bookrepository;
        this.studentRepository = studentRepository;
        this.borrowRepository = borrowRepository;
    }

    @Override
    public boolean isDuplicateBookId(int id) {
        return bookRepository.existsById(id);
    }

    @Override
    public void addBook(Book book) {
        book.setCreatedBy("SYSTEM");
        book.setCreatedOn(LocalDateTime.now());

        bookRepository.addBook(book);
    }

    @Override
    public List<Book> viewBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public List<Book> searchBook(String value) {
        return bookRepository.searchBooks(value);
    }

    @Override
    public void updateBook(Book book) {
        book.setUpdatedBy("SYSTEM");
        book.setUpdatedOn(LocalDateTime.now());

        bookRepository.updateBook(book);
    }

    @Override
    public void deleteBook(Book book) {
        book.setDeletedBy("SYSTEM");
        book.setDeletedOn(LocalDateTime.now());

        bookRepository.deleteBook(book);
    }

    @Override
    public boolean isDuplicateStudentId(int id) {

        return studentRepository.existsById(id);
    }

    @Override
    public void addStudent(Student student) {

        student.setCreatedBy("SYSTEM");
        student.setCreatedOn(LocalDateTime.now());

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

        student.setUpdatedBy("SYSTEM");
        student.setUpdatedOn(LocalDateTime.now());

        studentRepository.updateStudent(student);
    }

    @Override
    public void deleteStudent(Student student) {

        student.setDeletedBy("SYSTEM");
        student.setDeletedOn(LocalDateTime.now());

        studentRepository.deleteStudent(student);
    }

    @Override
    public void borrowBook(BorrowRecord record) {
        record.setCreatedBy("SYSTEM");
        record.setCreatedOn(LocalDateTime.now());

        borrowRepository.borrowBook(record);
    }

    @Override
    public void returnBook(int borrowId, LocalDate returnDate) {

        borrowRepository.returnBook(
                borrowId,
                returnDate,
                "SYSTEM",
                LocalDateTime.now());
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

    @Override
    public boolean isBookAvailable(int bookId) {

        return !borrowRepository.isBookBorrowed(bookId);
    }
}
