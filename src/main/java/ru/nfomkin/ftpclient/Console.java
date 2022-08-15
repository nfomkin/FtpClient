package ru.nfomkin.ftpclient;

import ru.nfomkin.ftpclient.ftp.ErrorMessage;
import ru.nfomkin.ftpclient.ftp.FtpClient;

import java.io.IOException;
import java.io.Reader;

public class Console {

    private final Reader reader;
    private FtpClient ftpClient;

    public Console(Reader reader) {
        this.reader = reader;
    }

    public void start() throws IOException {
        open();
        login();
    }

    public void open() throws IOException {
        while (true) {
            String ans = readLine().strip().replaceAll("[\\s]{2,}", " ");
            String[] values = ans.split(" ");
            if (ans.startsWith("open")) {
                if (values.length != 2) {
                    System.out.println("Использование: open имя-узла");
                    continue;
                }
                String ip = values[1];
                try {
                    ftpClient = new FtpClient(ip);
                    break;
                }
                catch (IOException ex) {
                    System.out.println(ErrorMessage.Connection.message);
                }
            }
        }
    }

    public void login() {
        boolean successful = false;
        while (!successful) {
            try {
                System.out.println("Пользователь: ");
                String user = readLine();
                System.out.println("Пароль: ");
                String password = readLine();
                successful = ftpClient.login(user, password);
                if (!successful) {
                    System.out.println(ErrorMessage.Authentication.message);
                }
            }
            catch (IOException ex) {
                System.out.println(ErrorMessage.Authentication.message);
            }
        }
    }

    public void clean() {
        System.out.print("\033[H\033[J");
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
