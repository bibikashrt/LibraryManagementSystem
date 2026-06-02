package repository;

import java.util.List;
import model.Student;

public interface StudentRepository {

    void addStudent(Student student);

    List<Student> getAllStudents();

    List<Student> searchStudents(String value);

    void updateStudent(Student student);

    void deleteStudent(Student student);

    boolean existsById(int studentId);
}
