package model;

public class Student {

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;

    private final int studentId;
    private final String studentName;
    private final String email;

    public Student(
            int studentId,
            String studentName,
            String email) {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive.");
        }

        this.studentId = studentId;

        this.studentName = requireLength(
                studentName,
                MAX_NAME_LENGTH,
                "Student name");

        this.email = requireLength(
                email,
                MAX_EMAIL_LENGTH,
                "Email");
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {

        return "ID: "
                + studentId
                + " | Name: "
                + studentName
                + " | Email: "
                + email;
    }

    private static String requireLength(
            String value,
            int max,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank.");
        }

        if (value.length() > max) {
            throw new IllegalArgumentException(
                    fieldName
                    + " exceeds maximum length of "
                    + max
                    + " characters.");
        }

        return value.trim();
    }
}
