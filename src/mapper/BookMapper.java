package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.Book;

public interface BookMapper {

    boolean existsById(@Param("bookId") int bookId);

    void addBook(Book book);

    List<Book> getAllBooks();

    List<Book> searchBooks(@Param("value") String value);

    void updateBook(Book book);

    void deleteBook(Book book);
}
