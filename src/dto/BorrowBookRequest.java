package dto;

import lombok.Data;

@Data
public class BorrowBookRequest {

    private int studentId;
    private int bookId;

    // public int getStudentId() {
    //     return studentId;
    // }
    // public void setStudentId(int studentId) {
    //     this.studentId = studentId;
    // }
    // public int getBookId() {
    //     return bookId;
    // }
    // public void setBookId(int bookId) {
    //     this.bookId = bookId;
    // }
}
