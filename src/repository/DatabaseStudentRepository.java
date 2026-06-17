package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import config.DatabaseConnection;
import config.LoggerConfig;
import exception.LibraryException;
import model.Student;

public class DatabaseStudentRepository implements StudentRepository {

    private static final Logger LOGGER = LoggerConfig.LOGGER;

    @Override
    public void addStudent(Student student) {

        String sql = """
    INSERT INTO students (
        id,
        name,
        faculty,
        batch,
        created_by,
        created_on
    )
    VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getStudentId());

            ps.setString(2, student.getStudentName());

            ps.setString(3, student.getFaculty());

            ps.setString(4, student.getBatch());

            ps.setString(5, student.getCreatedBy());

            ps.setTimestamp(6, Timestamp.valueOf(student.getCreatedOn()));

            ps.executeUpdate();

            LOGGER.info("Student added successfully. ID=" + student.getStudentId());

        } catch (SQLException e) {

            LOGGER.severe("Database error while adding student: " + e.getMessage());

            throw new LibraryException("Failed to add student.", e);
        }
    }

    @Override
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = """
    SELECT *
    FROM students
    WHERE deleted_on IS NULL
    ORDER BY id
    """;

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                students.add(mapRow(rs));
            }

        } catch (SQLException e) {

            LOGGER.severe("Failed to retrieve students. Error: " + e.getMessage());

            throw new LibraryException("Failed to retrieve students.", e);
        }

        return students;
    }

    @Override
    public List<Student> searchStudents(String value) {

        List<Student> students = new ArrayList<>();

        String sql = """
    SELECT *
    FROM students
    WHERE deleted_on IS NULL
      AND (
            CAST(id AS VARCHAR) = ?
         OR LOWER(name) LIKE ?
         OR LOWER(faculty) LIKE ?
         OR LOWER(batch) LIKE ?
      )
    ORDER BY id
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = value.trim().toLowerCase();

            ps.setString(1, search);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    students.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {

            LOGGER.severe("Failed to search students. Search value: "
                    + value
                    + ". Error: "
                    + e.getMessage());

            throw new LibraryException("Failed to search students.", e);
        }

        return students;
    }

    @Override
    public void updateStudent(Student student) {

        String sql = """
            UPDATE students
            SET name = ?,
                faculty = ?,
                batch = ?,
                updated_by = ?,
                updated_on = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getStudentName());

            ps.setString(2, student.getFaculty());

            ps.setString(3, student.getBatch());

            ps.setString(4, student.getUpdatedBy());

            ps.setTimestamp(5, Timestamp.valueOf(student.getUpdatedOn()));

            ps.setInt(6, student.getStudentId());

            int rows = ps.executeUpdate();

            if (rows == 0) {

                LOGGER.warning("Update failed. Student not found. ID=" + student.getStudentId());

            } else {

                LOGGER.info("Student updated successfully. ID=" + student.getStudentId());
            }

        } catch (SQLException e) {

            LOGGER.severe("Failed to update student. ID="
                    + student.getStudentId()
                    + ". Error: "
                    + e.getMessage());

            throw new LibraryException("Failed to update student.", e);
        }
    }

    @Override
    public void deleteStudent(Student student) {

        String sql = """
    UPDATE students
    SET deleted_by = ?,
        deleted_on = ?
    WHERE id = ?
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getDeletedBy());

            ps.setTimestamp(2, Timestamp.valueOf(student.getDeletedOn()));

            ps.setInt(3, student.getStudentId());

            int rows = ps.executeUpdate();

            if (rows == 0) {

                LOGGER.warning("Delete failed. Student not found. ID=" + student.getStudentId());

            } else {

                LOGGER.info("Student deleted successfully. ID=" + student.getStudentId());
            }

        } catch (SQLException e) {

            LOGGER.severe("Failed to delete student. ID="
                    + student.getStudentId()
                    + ". Error: "
                    + e.getMessage());

            throw new LibraryException("Failed to delete student.", e);
        }
    }

    @Override
    public boolean existsById(int studentId) {

        String sql = """
    SELECT COUNT(*)
    FROM students
    WHERE id = ?
      AND deleted_on IS NULL
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {

            LOGGER.severe("Failed to check student existence. ID="
                    + studentId
                    + ". Error: "
                    + e.getMessage());

            throw new LibraryException("Failed to check student existence.", e);
        }
    }

    private Student mapRow(ResultSet rs)
            throws SQLException {

        Student student
                = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("faculty"),
                        rs.getString("batch")
                );

        student.setCreatedBy(rs.getString("created_by"));

        Timestamp createdOn = rs.getTimestamp("created_on");

        if (createdOn != null) {

            student.setCreatedOn(createdOn.toLocalDateTime());
        }

        student.setUpdatedBy(rs.getString("updated_by"));

        Timestamp updatedOn = rs.getTimestamp("updated_on");

        if (updatedOn != null) {

            student.setUpdatedOn(updatedOn.toLocalDateTime());
        }

        student.setDeletedBy(rs.getString("deleted_by"));

        Timestamp deletedOn = rs.getTimestamp("deleted_on");

        if (deletedOn != null) {

            student.setDeletedOn(deletedOn.toLocalDateTime());
        }

        return student;
    }
}
