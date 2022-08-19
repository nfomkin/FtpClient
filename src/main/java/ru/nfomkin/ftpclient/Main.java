package ru.nfomkin.ftpclient;

import ru.nfomkin.ftpclient.ftp.Mode;

import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        Console console = new Console(new InputStreamReader(System.in));
        console.open();
        console.login();
        console.setMode(Mode.Active);
        console.scan();
        console.close();

    }

}
