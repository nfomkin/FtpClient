package ru.nfomkin.ftpclient.ftp;

import ru.nfomkin.ftpclient.Student;
import ru.nfomkin.ftpclient.StudentDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;


public class FtpClient {
    private String user;
    private String password;
    private String ip;
    private String localPath;
    private String serverPath;
    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private Mode mode;
    private String hostAddress;
    private static final Integer port = 21;


    public FtpClient(String user, String password, String ip) {
        this.user = user;
        this.password = password;
        this.ip = ip;
        this.mode = Mode.Active;
    }

    public FtpClient() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean login() throws IOException {
        if (executeCommand(Commands.User, user)) {
            if (executeCommand(Commands.Password, password)) {
                return true;
            }
        }
        return false;
    }

    public boolean open() throws IOException {
        socket = new Socket(ip, port);
        hostAddress = socket.getLocalAddress().getHostAddress();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream(), true);
        String reply = readReply();
        if (reply.startsWith("220")) {
            return true;
        }
        return false;
    }

    public void start() throws IOException {
        login();
        setMode();
        get();
        quit();
    }


    public boolean get() throws IOException {
        return executeCommand(Commands.Get, serverPath);
    }

    private boolean setMode() throws IOException {
        boolean result = false;
        if (mode.equals(Mode.Active)) {
            int port = getDataPort();
            System.out.println("Get data port:" + port);
            int x = (int) Math.round((port / 256) - 0.5);
            int y = port - x * 256;
            result = executeCommand(Commands.Active, (hostAddress.replace('.', ',') + "," + x + "," + y));
        } else if (mode.equals(Mode.Passive)) {
            result = executeCommand(Commands.Passive, null);
        }
        return result;
    }

    public boolean executeCommand(Commands command, String arg) throws IOException {
        boolean successful = false;
        out.println(command.name + " " + arg);
        String reply = readReply();
        System.out.println(reply);
        if (command.successCodes.contains(getResponseCode(reply))) {
            successful = true;
        }
        return successful;
    }

    private int getDataPort() {
        return socket.getLocalPort() + 1;
    }

    private String readReply() throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (in.ready()) {
            result.append(in.readLine());
        }
        return result.toString();
    }


    public void quit() throws IOException {
        out.println(Commands.Quit.name);
        System.out.println(readReply());
        socket.close();
        in.close();
        out.close();
    }

    private Integer getResponseCode(String response) {
        return Integer.parseInt(response.substring(0, 3));
    }

    public List<Student> getStudentsByName(String name) {
        return null;
    }

    public StudentDto getById(Integer id) {
        return null;
    }

    public boolean addStudent(StudentDto student) {
        return true;
    }

    public boolean deleteById(Integer id) {
        return true;
    }

}

