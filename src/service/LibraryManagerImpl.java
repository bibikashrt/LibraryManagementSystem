package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import enums.BorrowStatus;
import jakarta.enterprise.context.ApplicationScoped;
import mapper.BookMapper;
import mapper.BorrowMapper;
import mapper.StudentMapper;
import model.Book;
import model.BorrowRecord;
import model.Student;

@ApplicationScoped
public class LibraryManagerImpl implements LibraryManager {

    // private BookRepository bookRepository;
    // private StudentRepository studentRepository;
    // private BorrowRepository borrowRepository;
    public LibraryManagerImpl() {
    }

    // @Inject
    // public LibraryManagerImpl(BookRepository bookrepository, StudentRepository studentRepository, BorrowRepository borrowRepository) {
    //     this.bookRepository = bookrepository;
    //     this.studentRepository = studentRepository;
    //     this.borrowRepository = borrowRepository;
    // }
    @Override
    public boolean isDuplicateBookId(int id) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            return mapper.existsById(id);
        }
    }

    @Override
    public void addBook(Book book) {
        book.setCreatedBy("SYSTEM");
        book.setCreatedOn(LocalDateTime.now());

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.addBook(book);
        }
    }

    @Override
    public List<Book> viewBooks() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            return mapper.getAllBooks();
        }
    }

    @Override
    public List<Book> searchBook(String value) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            return mapper.searchBooks(value);
        }
    }

    @Override
    public void updateBook(Book book) {
        book.setUpdatedBy("SYSTEM");
        book.setUpdatedOn(LocalDateTime.now());

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.updateBook(book);
        }
    }

    @Override
    public void deleteBook(Book book) {
        book.setDeletedBy("SYSTEM");
        book.setDeletedOn(LocalDateTime.now());

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.deleteBook(book);
        }
    }

    @Override
    public boolean isDuplicateStudentId(int id) {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            return mapper.existsById(id);
        }
    }

    @Override
    public void addStudent(Student student) {

        student.setCreatedBy("SYSTEM");
        student.setCreatedOn(LocalDateTime.now());

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            mapper.addStudent(student);
        }
    }

    @Override
    public List<Student> viewStudents() {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            return mapper.getAllStudents();
        }
    }

    @Override
    public List<Student> searchStudent(String value) {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            return mapper.searchStudents(value);
        }
    }

    @Override
    public void updateStudent(Student student) {

        student.setUpdatedBy("SYSTEM");
        student.setUpdatedOn(LocalDateTime.now());

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            mapper.updateStudent(student);
        }
    }

    @Override
    public void deleteStudent(Student student) {

        student.setDeletedBy("SYSTEM");
        student.setDeletedOn(LocalDateTime.now());

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(true)) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            mapper.deleteStudent(student);
        }
    }

    @Override
    public void borrowBook(int studentId, int bookId) {

        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false);

        try {
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);
            BookMapper bookMapper = session.getMapper(BookMapper.class);
            BorrowMapper borrowMapper = session.getMapper(BorrowMapper.class);

            if (!studentMapper.existsById(studentId)) {
                throw new IllegalArgumentException("Student not found.");
            }

            if (!bookMapper.existsById(bookId)) {
                throw new IllegalArgumentException("Book not found.");
            }

            if (borrowMapper.isBookBorrowed(bookId)) {
                throw new IllegalStateException("Book is already borrowed.");
            }

            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = issueDate.plusDays(7);

            BorrowRecord record = new BorrowRecord(
                    0,
                    studentId,
                    bookId,
                    issueDate,
                    dueDate,
                    null,
                    BorrowStatus.BORROWED
            );

            record.setCreatedBy("SYSTEM");
            record.setCreatedOn(LocalDateTime.now());

            borrowMapper.borrowBook(record);

            session.commit();

        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void returnBook(int borrowId, LocalDate returnDate) {

        SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false);

        try {
            BorrowMapper mapper = session.getMapper(BorrowMapper.class);

            if (!mapper.existsByBorrowId(borrowId)) {
                throw new IllegalArgumentException("Borrow record not found.");
            }

            if (mapper.isBookReturned(borrowId)) {
                throw new IllegalStateException("Book has already been returned.");
            }

            mapper.returnBook(
                    borrowId,
                    returnDate,
                    "SYSTEM",
                    LocalDateTime.now()
            );

            session.commit();

        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<BorrowRecord> viewBorrowRecords() {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BorrowMapper mapper = session.getMapper(BorrowMapper.class);
            return mapper.getAllBorrowRecords();
        }
    }

    @Override
    public List<BorrowRecord> viewOverdueBorrowRecords() {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BorrowMapper mapper = session.getMapper(BorrowMapper.class);
            return mapper.getOverdueBorrowRecords();
        }
    }

    @Override
    public boolean isBorrowRecordExists(int borrowId) {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BorrowMapper mapper = session.getMapper(BorrowMapper.class);
            return mapper.existsByBorrowId(borrowId);
        }
    }

    @Override
    public boolean isBookAvailable(int bookId) {

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BorrowMapper mapper = session.getMapper(BorrowMapper.class);
            return !mapper.isBookBorrowed(bookId);
        }
    }

    @Override
    public boolean isStudentExists(int studentId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            return mapper.existsById(studentId);
        }
    }

    @Override
    public boolean isBookExists(int bookId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            return mapper.existsById(bookId);
        }
    }
}
