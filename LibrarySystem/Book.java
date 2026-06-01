
public class Book {

    private final int bookId;
    private final String bookName;
    private final String authorName;
    private final String category;
    private final int publicationYear;

    public Book(int bookId, String bookName, String authorName, String category, int publicationYear) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.category = category;
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
}
