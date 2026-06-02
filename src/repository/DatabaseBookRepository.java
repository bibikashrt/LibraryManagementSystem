package repository;

import config.DatabaseConnection;
import config.LoggerConfig;
import exception.LibraryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import model.Book;

public class DatabaseBookRepository implements BookRepository {

    private static final Logger LOGGER = LoggerConfig.LOGGER;

    @Override
    public void addBook(Book book) {
        String sql
                = "INSERT INTO books VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, book.getBookId());
            ps.setString(2, book.getBookName());
            ps.setString(3, book.getAuthorName());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getPublicationYear());

            ps.executeUpdate();

            LOGGER.info("Book added successfully. ID="
                    + book.getBookId()
                    + ", Name="
                    + book.getBookName()
            );

        } catch (SQLException e) {

            LOGGER.severe("Database error while adding book: " + e.getMessage());

            throw new LibraryException("Failed to add book: " + book.getBookName(), e);
        }

    }

    @Override
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql
                = "SELECT * FROM books ORDER BY book_id";

        try (
                Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                books.add(mapRow(rs));
            }

        } catch (SQLException e) {

            LOGGER.severe("Database error while retrieving books: " + e.getMessage());

            throw new LibraryException("Failed to retrieve books.", e);
        }

        return books;
    }

    @Override
    public List<Book> searchBooks(String value) {

        LOGGER.info("Searching books with keyword: " + value);

        List<Book> books = new ArrayList<>();

        String sql = """
        SELECT *
        FROM books
        WHERE CAST(book_id AS VARCHAR) = ?
           OR LOWER(book_name) LIKE ?
           OR LOWER(author_name) LIKE ?
           OR LOWER(category) LIKE ?
           OR CAST(publication_year AS TEXT) = ?
        ORDER BY book_id
        """;

        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchValue = value.trim().toLowerCase();

            ps.setString(1, searchValue);
            ps.setString(2, "%" + searchValue + "%");
            ps.setString(3, "%" + searchValue + "%");
            ps.setString(4, "%" + searchValue + "%");
            ps.setString(5, searchValue);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {

            LOGGER.severe("Database error while searching books: " + e.getMessage());

            throw new LibraryException("Failed to search books.", e);
        }

        return books;
    }

    @Override
    public void updateBook(Book book) {

        String sql = """
            UPDATE books
            SET book_name = ?,
                author_name = ?,
                category = ?,
                publication_year = ?
            WHERE book_id = ?
            """;

        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getBookName());
            ps.setString(2, book.getAuthorName());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getPublicationYear());
            ps.setInt(5, book.getBookId());

            ps.executeUpdate();

            LOGGER.info("Book updated successfully. ID=" + book.getBookId());

        } catch (SQLException e) {

            LOGGER.severe("Database error while updating book: " + e.getMessage());

            throw new LibraryException("Failed to update book ID: " + book.getBookId(), e);
        }
    }

    @Override
    public void deleteBook(Book book) {

        String sql
                = "DELETE FROM books WHERE book_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, book.getBookId());

            ps.executeUpdate();

            LOGGER.info("Book deleted successfully. ID=" + book.getBookId());

        } catch (SQLException e) {

            LOGGER.severe("Database error while deleting book: " + e.getMessage());

            throw new LibraryException("Failed to delete book ID: " + book.getBookId(), e);
        }
    }

    @Override
    public boolean existsById(int bookId) {
        String sql = "SELECT COUNT(*) FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {

            LOGGER.severe("Database error while checking book existence: " + e.getMessage());

            throw new LibraryException("Failed to check existence for book ID: " + bookId, e);
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("book_id"),
                rs.getString("book_name"),
                rs.getString("author_name"),
                rs.getString("category"),
                rs.getInt("publication_year")
        );
    }
}
