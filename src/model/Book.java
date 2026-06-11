package model;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    public static final int MAX_NAME_LENGTH = 200;
    public static final int MAX_AUTHOR_LENGTH = 100;
    public static final int MAX_CATEGORY_LENGTH = 100;

    public static final Comparator<Book> BY_NAME
            = Comparator.comparing(b -> b.bookName.toLowerCase());

    public static final Comparator<Book> BY_YEAR
            = Comparator.comparingInt(b -> b.publicationYear);

    private final int bookId;
    private final String bookName;
    private final String authorName;
    private final String category;
    private final int publicationYear;

    @JsonCreator
    public Book(
            @JsonProperty("bookId") int bookId,
            @JsonProperty("bookName") String bookName,
            @JsonProperty("authorName") String authorName,
            @JsonProperty("category") String category,
            @JsonProperty("publicationYear") int publicationYear) {

        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive.");
        }

        this.bookId = bookId;
        this.bookName = requireLength(bookName, MAX_NAME_LENGTH, "Book name");
        this.authorName = requireLength(authorName, MAX_AUTHOR_LENGTH, "Author name");
        this.category = requireLength(category, MAX_CATEGORY_LENGTH, "Category");
        this.publicationYear = publicationYear;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategory() {
        return category;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public boolean matches(String value) {
        value = value.trim().toLowerCase();
        return String.valueOf(bookId).equals(value)
                || bookName.toLowerCase().contains(value)
                || authorName.toLowerCase().contains(value)
                || category.toLowerCase().contains(value)
                || String.valueOf(publicationYear).equals(value);
    }

    @Override
    public String toString() {

        return "ID: "
                + bookId
                + " | Name: "
                + bookName
                + " | Author: "
                + authorName
                + " | Category: "
                + category
                + " | Year: "
                + publicationYear;
    }

    private static String requireLength(String value, int max, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        if (value.length() > max) {
            throw new IllegalArgumentException(
                    fieldName + " exceeds maximum length of " + max + " characters.");
        }
        return value.trim();
    }
}
