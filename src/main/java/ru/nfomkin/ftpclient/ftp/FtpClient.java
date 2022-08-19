package ru.nfomkin.ftpclient.ftp;

import ru.nfomkin.ftpclient.exception.CommandException;
import ru.nfomkin.ftpclient.exception.ConnectionException;
import ru.nfomkin.ftpclient.student.StudentJsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;


public class FtpClient {
    private String ip;
    private String localPath;
    private String serverPath;
    private Socket cmdSocket;
    private BufferedReader in;
    private PrintStream out;
    private Mode mode;
    private String hostAddress;
    private StudentJsonParser parser;
    private String reply;
    private static final Integer port = 21;


    public FtpClient(String ip, String localPath, String serverPath) throws ConnectionException {
        this.ip = ip;
        try {
            cmdSocket = new Socket(ip, port);
            hostAddress = cmdSocket.getLocalAddress().getHostAddress();
            in = new BufferedReader(new InputStreamReader(cmdSocket.getInputStream()));
            out = new PrintStream(cmdSocket.getOutputStream(), true);
            if (!isConnected()) {
                throw new IOException();
            }
        } catch (IOException ex) {
            throw new ConnectionException("Не удается подключиться к серверу");
        }
        this.mode = Mode.Active;
        this.localPath = localPath;
        this.serverPath = serverPath;
        this.parser = new StudentJsonParser();
    }

    public String getIp() {
        return ip;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean login(String user, String password) throws IOException {
        if (executeCommand(Command.User, user)) {
            if (executeCommand(Command.Password, password)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConnected() throws IOException {
        reply = readReply();
        return reply.startsWith("220");
    }

    public String get() throws IOException {
        System.out.println("Method get");
        Socket dataSocket = null;
        ServerSocket serverSocket = null;

        if (mode.equals(Mode.Passive)) {
            dataSocket = passive();
        } else if (mode.equals(Mode.Active)) {
            serverSocket = active();
        }

        if (!executeCommand(Command.Get, serverPath)) {
            throw new CommandException("Команда -- Get, неуспешно");
        }

        if (serverSocket != null) {
            dataSocket = serverSocket.accept();
        }

        try (BufferedReader dataIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()))) {
            StringBuilder result = new StringBuilder();
            while (dataIn.ready()) {
                result.append(dataIn.readLine());
            }
            readReply();
            return result.toString();


        } finally {
            if (dataSocket != null) {
                dataSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }

        }
    }

    public void put(String file) throws IOException {
        Socket dataSocket = null;
        ServerSocket serverSocket = null;

        if (mode.equals(Mode.Active)) {
            serverSocket = active();
        } else if (mode.equals(Mode.Passive)) {
            dataSocket = passive();
        }

        if (!executeCommand(Command.Put, serverPath)) {
            if (dataSocket != null) {
                dataSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            throw new CommandException("Команда -- Put, неуспешно");
        }

        if (serverSocket != null) {
            dataSocket = serverSocket.accept();
        }

        try (PrintStream dataOut = new PrintStream(dataSocket.getOutputStream(), true)) {
            dataOut.println(file);
        } finally {
            readReply();
            dataSocket.close();
        }
    }

    private Socket passive() throws IOException {
        if (executeCommand(Command.Passive, "")) {
            try {
                int opening = reply.indexOf('(');
                int closing = reply.indexOf(')', opening + 1);

                String dataLink = reply.substring(opening + 1, closing);

                StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
                String ip = tokenizer.nextToken() + '.' + tokenizer.nextToken() + '.' + tokenizer.nextToken() + '.' + tokenizer.nextToken();
                Integer port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
                return new Socket(ip, port);
            } catch (IOException ex) {
                throw new IOException("Команда passive не выполнена\n" + ex.getMessage());
            }
        }
        throw new IOException("Команда passive не выполнена");
    }

    private ServerSocket active() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();
        int x = (int) Math.round((port / 256) - 0.5);
        int y = port - x * 256;
        if (executeCommand(Command.Active, (hostAddress.replace('.', ',') + "," + x + "," + y))) {
            return serverSocket;
        }
        throw new IOException("Команда active не выполнена");
    }

    private boolean executeCommand(Command command, String arg) throws IOException {
        boolean successful = false;
        out.println(command.name + " " + arg);
        reply = readReply();
        if (command.successCodes.contains(getResponseCode(reply))) {
            successful = true;
        }
        return successful;
    }

    private int getDataPort() {
        return cmdSocket.getLocalPort() + 1;
    }

    private String readReply() throws IOException {
        StringBuilder reply = new StringBuilder();

        while (!in.ready()) {
        }

        while (in.ready()) {
            reply.append(in.readLine());
        }
        return reply.toString();
    }


    public void quit() throws IOException {
        out.println(Command.Quit.name);
        if (cmdSocket != null) {
            cmdSocket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }

    private Integer getResponseCode(String response) {
        return Integer.parseInt(response.substring(0, 3));
    }

}

