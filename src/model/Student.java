package model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Student {

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_FACULTY_LENGTH = 100;
    public static final int MAX_BATCH_LENGTH = 20;

    private final int studentId;
    private final String studentName;
    private final String faculty;
    private final String batch;

    private String createdBy;
    private LocalDateTime createdOn;

    private String updatedBy;
    private LocalDateTime updatedOn;

    private String deletedBy;
    private LocalDateTime deletedOn;

    @JsonCreator
    public Student(
            @JsonProperty("studentId") int studentId,
            @JsonProperty("studentName") String studentName,
            @JsonProperty("faculty") String faculty,
            @JsonProperty("batch") String batch) {

        if (studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be positive.");
        }

        this.studentId = studentId;

        this.studentName = requireLength(studentName, MAX_NAME_LENGTH, "Student name");

        this.faculty = requireLength(faculty, MAX_FACULTY_LENGTH, "Faculty");

        this.batch = requireLength(batch, MAX_BATCH_LENGTH, "BATCH");
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(LocalDateTime deletedOn) {
        this.deletedOn = deletedOn;
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
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
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
