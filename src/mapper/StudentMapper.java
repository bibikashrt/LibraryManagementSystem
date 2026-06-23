package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.Student;

public interface StudentMapper {

    boolean existsById(@Param("studentId") int studentId);

    void addStudent(Student student);

    List<Student> getAllStudents();

    List<Student> searchStudents(@Param("value") String value);

    void updateStudent(Student student);

    void deleteStudent(Student student);
}
