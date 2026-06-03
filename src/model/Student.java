package model;

public class Student {

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_FACULTY_LENGTH = 100;
    public static final int MAX_BATCH_LENGTH = 20;

    private final int studentId;
    private final String studentName;
    private final String faculty;
    private final String batch;

    public Student(
            int studentId,
            String studentName,
            String faculty,
            String batch) {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive.");
        }

        this.studentId = studentId;

        this.studentName = requireLength(
                studentName,
                MAX_NAME_LENGTH,
                "Student name");

        this.faculty = requireLength(
                faculty,
                MAX_FACULTY_LENGTH,
                "Faculty");

        this.batch = requireLength(
                batch,
                MAX_BATCH_LENGTH,
                "BATCH");
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getBatch() {
        return batch;
    }

    @Override
    public String toString() {

        return "ID: "
                + studentId
                + " | Name: "
                + studentName
                + " | Faculty: "
                + faculty
                + " | Batch: "
                + batch;
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
