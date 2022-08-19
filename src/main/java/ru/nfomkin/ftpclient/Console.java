package ru.nfomkin.ftpclient;

import ru.nfomkin.ftpclient.exception.ConnectionException;
import ru.nfomkin.ftpclient.ftp.ErrorMessage;
import ru.nfomkin.ftpclient.ftp.FtpClient;
import ru.nfomkin.ftpclient.ftp.Mode;
import ru.nfomkin.ftpclient.student.*;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

public class Console {

    private final Reader reader;
    private FtpClient ftpClient;
    private StudentService studentService;

    public Console(Reader reader) {
        this.reader = reader;
    }

    public void open() {
        System.out.println("Ftp-client");
        while (true) {
            String ans;
            try {
                ans = readLine().trim().replaceAll("[\\s]{2,}", " ");
            } catch (IOException ex) {
                continue;
            }

            String[] values = ans.split(" ");
            if (ans.startsWith("open")) {
                if (values.length != 2) {
                    System.out.println("Использование: open имя-узла");
                    continue;
                }
                String ip = values[1];
                try {
                    ftpClient = new FtpClient(ip, "D:/", "/D/FtpServer/students.json");
                    studentService = new StudentServiceImpl(ftpClient, new StudentJsonParser());
                    break;
                } catch (ConnectionException ex) {
                    System.out.println(ex.getMessage() + "\nПопробуйте еще раз");
                }
            }
        }
        System.out.println("Подключено");
    }

    public void login() {
        boolean successful = false;
        while (!successful) {
            try {
                System.out.print("Пользователь: ");
                String user = readLine();
                System.out.print("Пароль: ");
                String password = readLine();
                successful = ftpClient.login(user, password);
                if (!successful) {
                    System.out.println(ErrorMessage.Authentication.message);
                }
            } catch (IOException ex) {
                System.out.println(ErrorMessage.Authentication.message);
            }
        }
        System.out.println("Вы успешно вошли");
    }

    public void setMode(Mode mode) throws IOException {
        ftpClient.setMode(mode);
    }

    public void scan() {
        try {
            while (true) {
                menu();
                System.out.print("Команда: ");
                String choice = readLine();
                Integer numChoice;
                try {
                    numChoice = Integer.parseInt(choice);
                } catch (NumberFormatException ex) {
                    System.out.println("Введите число!");
                    continue;
                }

                if (numChoice == 1) {
                    List<StudentDto> students = studentService.getStudents();
                    if (students != null && !students.isEmpty()) {
                        students.forEach(System.out::println);
                    } else {
                        System.out.println("Студенты с таким именем не найдены");
                    }
                    System.out.print("Next...");
                    readLine();

                } else if (numChoice == 2) {
                    Integer id;
                    while (true) {
                        System.out.print("Введите id: ");
                        try {
                            id = Integer.parseInt(readLine());
                            break;
                        }
                        catch (NumberFormatException ex) {
                            System.out.println("Введите число!");
                        }
                    }

                    Optional<StudentDto> maybeStudent = studentService.getById(id);
                    if (maybeStudent.isPresent()) {
                        System.out.println(maybeStudent.get());
                    } else {
                        System.out.println("Студент с таким id не найден");
                    }
                    System.out.print("Next...");
                    readLine();

                } else if (numChoice == 3) {
                    try {
                        System.out.print("Введите имя студента: ");
                        String name = readLine();
                        boolean successful = studentService.addStudent(new StudentDto(name));
                        if (!successful) {
                            throw new IOException();
                        }
                    } catch (IOException ex) {
                        System.out.println("Не удалось добавить");
                        System.out.println(ex.getMessage());
                    }
                    System.out.println("Студент добавлен");
                    System.out.print("Next...");
                    readLine();

                } else if (numChoice == 4) {
                    Integer id;
                    while (true) {
                        System.out.print("Введите id: ");
                        try {
                            id = Integer.parseInt(readLine());
                            break;
                        }
                        catch (NumberFormatException ex) {
                            System.out.println("Введите число!");
                        }
                    }

                    boolean successful = studentService.deleteStudentById(id);
                    if (!successful) {
                        System.out.println("Не удалось удалить студента");
                    }
                    else {
                        System.out.println("Студент удален");
                    }

                }
                else if (Integer.parseInt(choice) == 5) {
                    close();
                } else {
                    System.out.println("Такой команды нет");
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void close() {
        try {
            ftpClient.quit();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }

    private void menu() {
        System.out.println("1. Получить список студентов");
        System.out.println("2. Получить информацию о студенте по id");
        System.out.println("3. Добавить студента");
        System.out.println("4. Удалить студента по id");
        System.out.println("5. Выход");
    }


    private String readLine() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char ch;
        while ((ch = (char) reader.read()) != '\n') {
            stringBuilder.append(ch);
        }

        return stringBuilder.toString();
    }


}
