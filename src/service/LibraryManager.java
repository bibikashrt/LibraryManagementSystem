package service;

import model.Book;
import model.Student;

import java.util.List;

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
}
