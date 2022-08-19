package ru.nfomkin.ftpclient.student;

public class StudentDto {
    private String name;

    public StudentDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static StudentDto fromStudent(Student student) {
        return new StudentDto(student.getName());
    }

    @Override
    public String toString() {
        return String.format("Name = %s", name);
    }

}
