package service;

import java.time.LocalDate;
import java.util.List;
import model.Book;
import model.BorrowRecord;
import model.Student;

public interface LibraryManager {

    boolean isDuplicateBookId(int id);

    void addBook(Book book);

    List<Book> viewBooks();

    List<Book> searchBook(String value);

    void updateBook(Book book);

    void deleteBook(Book book);

    boolean isDuplicateStudentId(int id);

    void addStudent(Student student);

    List<Student> viewStudents();

    List<Student> searchStudent(String value);

    void updateStudent(Student student);

    void deleteStudent(Student student);

    void borrowBook(BorrowRecord record);

    void returnBook(int borrowId, LocalDate returnDate);

    List<BorrowRecord> viewBorrowRecords();

    List<BorrowRecord> viewOverdueBorrowRecords();

    boolean isBorrowRecordExists(int borrowId);

    boolean isBookAvailable(int bookId);
}
