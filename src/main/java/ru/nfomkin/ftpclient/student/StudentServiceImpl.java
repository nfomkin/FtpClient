package ru.nfomkin.ftpclient.student;

import ru.nfomkin.ftpclient.ftp.FtpClient;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {
    private FtpClient ftpClient;
    private StudentJsonParser parser;

    public StudentServiceImpl(FtpClient ftpClient, StudentJsonParser parser) {
        this.ftpClient = ftpClient;
        this.parser = parser;
    }

    public List<StudentDto> getStudents(){
        try {
            String file = ftpClient.get();
            List<Student> students = parser.fromJson(file);
            return students.stream()
                    .map(StudentDto::fromStudent)
                    .sorted(Comparator.comparing(StudentDto::getName))
                    .collect(Collectors.toList());
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Optional<StudentDto> getById(Integer id) {
        try {
            String file = ftpClient.get();
            List<Student> students = parser.fromJson(file);
            return students.stream()
                    .filter(student -> student.getId().equals(id))
                    .findAny()
                    .map(StudentDto::fromStudent);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return Optional.empty();
        }
    }

    public boolean addStudent(StudentDto student){
        try {
            String file = ftpClient.get();
            String newFile = addStudentToJson(file, student);
            ftpClient.put(newFile);
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean deleteStudentById(Integer id) {
        try {
            String file = ftpClient.get();
            String newFile = deleteStudentFromJson(file, id);
            ftpClient.put(newFile);
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private String deleteStudentFromJson(String json, Integer id) {
        List<Student> students = parser.fromJson(json)
                .stream().filter(student -> !student.getId().equals(id))
                .collect(Collectors.toList());
        return parser.toJson(students);
    }

    private String addStudentToJson(String json, StudentDto studentDto) {
        List<Student> students = parser.fromJson(json);
        Student student = new Student(studentDto);
        OptionalInt maxId = students.stream().mapToInt(Student::getId).max();
        if (!maxId.isPresent()) {
            student.setId(1);
        }
        else {
            student.setId(maxId.getAsInt() + 1);
        }

        int lastCloseBracketsIndex = json.lastIndexOf(']');
        String jsonStudent = parser.toJson(student);
        StringBuilder builder = new StringBuilder(json);
        builder.insert(lastCloseBracketsIndex, ",\n" + jsonStudent + '\n');
        return builder.toString();
    }


}
