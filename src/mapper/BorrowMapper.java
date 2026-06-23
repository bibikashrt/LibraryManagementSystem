package mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.BorrowRecord;

public interface BorrowMapper {

    void borrowBook(BorrowRecord record);

    void returnBook(@Param("borrowId") int borrowId,
            @Param("returnDate") LocalDate returnDate,
            @Param("updatedBy") String updatedBy,
            @Param("updatedOn") LocalDateTime updatedOn);

    List<BorrowRecord> getAllBorrowRecords();

    List<BorrowRecord> getOverdueBorrowRecords();

    boolean existsByBorrowId(@Param("borrowId") int borrowId);

    boolean isBookBorrowed(@Param("bookId") int bookId);
}
