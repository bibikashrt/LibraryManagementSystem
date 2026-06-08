package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        String sql = "INSERT INTO students VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getStudentId());

            ps.setString(2, student.getStudentName());

            ps.setString(3, student.getFaculty());

            ps.setString(4, student.getBatch());

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

        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                students.add(mapRow(rs));
            }

        } catch (SQLException e) {

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
            WHERE CAST(id AS VARCHAR) = ?
               OR LOWER(name) LIKE ?
               OR LOWER(faculty) LIKE ?
               OR LOWER(batch) LIKE ?
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
                batch = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getStudentName());

            ps.setString(2, student.getFaculty());

            ps.setString(3, student.getBatch());

            ps.setInt(4, student.getStudentId());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new LibraryException("Failed to update student.", e);
        }
    }

    @Override
    public void deleteStudent(Student student) {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getStudentId());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new LibraryException("Failed to delete student.", e);
        }
    }

    @Override
    public boolean existsById(int studentId) {

        String sql = "SELECT COUNT(*) FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {

            throw new LibraryException("Failed to check student existence.", e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {

        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("faculty"),
                rs.getString("batch")
        );
    }
}
