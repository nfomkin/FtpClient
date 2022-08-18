package ru.nfomkin.ftpclient.student;

import ru.nfomkin.ftpclient.json.Constant;
import ru.nfomkin.ftpclient.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class StudentJsonParser {

    public List<Student> fromJson(String json) {
        JSONObject object = new JSONObject(json);
        List<String> students = object.getJSONArray("students").getObjects();
        List<Student> result = new ArrayList<>(students.size());
        if (students != null) {
            for (String student : students) {
                Integer id = null;
                String name = null;
                student = student.replaceAll("[\\{\\}]", "");
                String[] fields = student.split(Constant.COMMA.toString());
                for (String field: fields) {
                    String[] keyValue = field.split(Constant.COLON.toString());
                    if (keyValue[0].equals("id")) {
                        id = Integer.parseInt(keyValue[1]);
                    }
                    else if (keyValue[0].equals("name")) {
                        name = keyValue[1];
                    }
                }

                if (id != null && name != null) {
                    result.add(new Student(id, name));
                }
            }
        }
        return result;
    }

    public String toJson(Student student) {
        return String.format("{\"id\": %d, \"name\": \"%s\"}", student.getId(), student.getName());
    }

    public String toJson(List<Student> students) {
        StringBuilder json = new StringBuilder("{\"students\":[");
        for (Student student: students) {
            json.append(toJson(student))
                    .append(',');
        }
        json.deleteCharAt(json.length() - 1);
        json.append("]}");
        return json.toString();
    }

    public String addStudentToJson(String json, StudentDto studentDto) {
        List<Student> students = fromJson(json);
        Student student = new Student(studentDto);
        OptionalInt maxId = students.stream().mapToInt(Student::getId).max();
        if (maxId.isEmpty()) {
            student.setId(1);
        }
        else {
            student.setId(maxId.getAsInt() + 1);
        }

        int lastCloseBracketsIndex = json.lastIndexOf(']');
        String jsonStudent = toJson(student);
        StringBuilder builder = new StringBuilder(json);
        builder.insert(lastCloseBracketsIndex, ",\n" + jsonStudent + '\n');
        return builder.toString();
    }
}
