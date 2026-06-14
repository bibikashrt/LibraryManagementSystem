package model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import enums.BorrowStatus;

public class BorrowRecord {

    private final int borrowId;

    private final int studentId;

    private final int bookId;

    private final LocalDate issueDate;

    private final LocalDate dueDate;

    private final LocalDate returnDate;

    private final BorrowStatus status;

    public BorrowRecord(
            @JsonProperty("borrowId") int borrowId,
            @JsonProperty("studentId") int studentId,
            @JsonProperty("bookId") int bookId,
            @JsonProperty("issueDate") LocalDate issueDate,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("returnDate") LocalDate returnDate,
            @JsonProperty("status") BorrowStatus status) {

        if (borrowId < 0) {
            throw new IllegalArgumentException("Borrow ID cannot be negative.");
        }

        if (studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be positive.");
        }

        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive.");
        }

        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date cannot be null.");
        }

        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null.");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be blank.");
        }

        this.borrowId = borrowId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {

        return "Borrow ID: "
                + borrowId
                + " | Student ID: "
                + studentId
                + " | Book ID: "
                + bookId
                + " | Issue Date: "
                + issueDate
                + " | Due Date: "
                + dueDate
                + " | Return Date: "
                + (returnDate == null
                        ? "Not Returned"
                        : returnDate)
                + " | Status: "
                + status;
    }
}
