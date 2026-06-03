package repository;

import java.time.LocalDate;
import java.util.List;
import model.BorrowRecord;

public interface BorrowRepository {

    void borrowBook(BorrowRecord record);

    void returnBook(int borrowId, LocalDate returnDate);

    List<BorrowRecord> getAllBorrowRecords();

    List<BorrowRecord> getActiveBorrowRecords();

    List<BorrowRecord> getOverdueBorrowRecords();

    boolean isBookBorrowed(int bookId);

    boolean existsByBorrowId(int borrowId);
}
