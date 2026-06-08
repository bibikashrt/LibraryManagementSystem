package repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import config.DatabaseConnection;
import config.LoggerConfig;
import enums.BorrowStatus;
import exception.LibraryException;
import model.BorrowRecord;

public class DatabaseBorrowRepository
        implements BorrowRepository {

    private static final Logger LOGGER = LoggerConfig.LOGGER;

    @Override
    public void borrowBook(BorrowRecord record) {

        String sql
                = """
                INSERT INTO borrow_records(
                    student_id,
                    book_id,
                    issue_date,
                    due_date,
                    return_date,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, record.getStudentId());

            statement.setInt(2, record.getBookId());

            statement.setDate(3, Date.valueOf(record.getIssueDate()));

            statement.setDate(4, Date.valueOf(record.getDueDate()));

            if (record.getReturnDate() == null) {

                statement.setNull(5, java.sql.Types.DATE);

            } else {

                statement.setDate(5, Date.valueOf(record.getReturnDate()));
            }

            statement.setString(6, record.getStatus().name());

            statement.executeUpdate();

            LOGGER.info("Book borrowed successfully.");

        } catch (SQLException e) {

            throw new LibraryException("Failed to borrow book.", e);
        }
    }

    @Override
    public void returnBook(int borrowId, LocalDate returnDate) {

        String sql
                = """
                UPDATE borrow_records
                SET return_date = ?,
                    status = 'RETURNED'
                WHERE borrow_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(returnDate));

            statement.setInt(2, borrowId);

            int rows = statement.executeUpdate();

            if (rows == 0) {

                throw new LibraryException("Borrow record not found.");
            }

            LOGGER.info("Book returned successfully.");

        } catch (SQLException e) {

            throw new LibraryException("Failed to return book.", e);
        }
    }

    @Override
    public boolean isBookBorrowed(int bookId) {

        String sql
                = """
                SELECT COUNT(*)
                FROM borrow_records
                WHERE book_id = ?
                AND status = 'BORROWED'
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);

            ResultSet rs = statement.executeQuery();

            rs.next();

            return rs.getInt(1) > 0;

        } catch (SQLException e) {

            throw new LibraryException("Failed to check borrow status.", e);
        }
    }

    @Override
    public boolean existsByBorrowId(int borrowId) {

        String sql
                = """
                SELECT 1
                FROM borrow_records
                WHERE borrow_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, borrowId);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            throw new LibraryException("Failed to check borrow ID.", e);
        }
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {

        List<BorrowRecord> records = new ArrayList<>();

        String sql
                = """
                SELECT *
                FROM borrow_records
                ORDER BY borrow_id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection(); Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                records.add(mapRow(rs));
            }

            return records;

        } catch (SQLException e) {

            throw new LibraryException("Failed to fetch borrow records.", e);
        }
    }

    @Override
    public List<BorrowRecord> getActiveBorrowRecords() {

        return getByStatus("BORROWED");
    }

    @Override
    public List<BorrowRecord> getOverdueBorrowRecords() {

        List<BorrowRecord> records = new ArrayList<>();

        String sql
                = """
                SELECT *
                FROM borrow_records
                WHERE status = 'BORROWED'
                AND due_date < CURRENT_DATE
                """;

        try (Connection connection = DatabaseConnection.getConnection(); Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {

                records.add(mapRow(rs));
            }

            return records;

        } catch (SQLException e) {

            throw new LibraryException("Failed to fetch overdue records.", e);
        }
    }

    private List<BorrowRecord> getByStatus(String status) {

        List<BorrowRecord> records = new ArrayList<>();

        String sql
                = """
                SELECT *
                FROM borrow_records
                WHERE status = ?
                ORDER BY borrow_id DESC
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, status);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                records.add(mapRow(rs));
            }

            return records;

        } catch (SQLException e) {

            throw new LibraryException("Failed to fetch records.", e);
        }
    }

    private BorrowRecord mapRow(ResultSet rs) throws SQLException {

        Date returnDate = rs.getDate("return_date");

        return new BorrowRecord(
                rs.getInt("borrow_id"),
                rs.getInt("student_id"),
                rs.getInt("book_id"),
                rs.getDate("issue_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate == null
                        ? null
                        : returnDate.toLocalDate(),
                BorrowStatus.valueOf(rs.getString("status"))
        );
    }
}
