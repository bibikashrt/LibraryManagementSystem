
public class Book {

    private int bookId;
    private String bookName;
    private String authorName;
    private String category;
    private int publicationYear;

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

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String toCSV() {
        return bookId + "," + bookName + "," + authorName + "," + category + "," + publicationYear;
    }

    public static Book fromCSV(String line) {
        String[] parts = line.split(",");
        return new Book(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                Integer.parseInt(parts[4].trim())
        );
    }

    public String matchesAnyField(String value) {

        value = value.trim();

        if (String.valueOf(bookId).equalsIgnoreCase(value)) {

            return "BookID";
        }

        if (bookName.equalsIgnoreCase(value)) {

            return "BookName";
        }

        if (authorName.equalsIgnoreCase(value)) {

            return "Author";
        }

        if (category.equalsIgnoreCase(value)) {

            return "Category";
        }

        if (String.valueOf(publicationYear).equalsIgnoreCase(value)) {

            return "Year";
        }

        return null;
    }

    @Override
    public String toString() {
        return "ID: " + bookId + " | Name: " + bookName + " | Author: " + authorName
                + " | Category: " + category + " | Year: " + publicationYear;
    }
}
