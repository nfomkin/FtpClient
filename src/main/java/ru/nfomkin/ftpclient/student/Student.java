package ru.nfomkin.ftpclient.student;

public class Student {
    private Integer id;
    private String name;

    public Student() {
    }

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student(StudentDto studentDto) {
        this.name = studentDto.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Id = %s, Name = %s", id, name);
    }
}
