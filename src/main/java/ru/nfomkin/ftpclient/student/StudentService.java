package ru.nfomkin.ftpclient.student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<StudentDto> getStudents();
    Optional<StudentDto> getById(Integer id);
    boolean addStudent(StudentDto student);
    boolean deleteStudentById(Integer id);
}
