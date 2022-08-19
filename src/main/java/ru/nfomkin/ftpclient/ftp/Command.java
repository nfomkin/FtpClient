package ru.nfomkin.ftpclient.ftp;

import java.util.Arrays;
import java.util.List;

public enum Command {

    Get("RETR", Arrays.asList(150, 226)),
    User("USER", Arrays.asList(331)),
    Password("PASS", Arrays.asList(230)),
    Put("STOR", Arrays.asList(150, 226)),
    Quit("QUIT", Arrays.asList(221)),
    Active("PORT", Arrays.asList(200)),
    Passive("PASV", Arrays.asList(227));

    final String name;
    final List<Integer> successCodes;

    Command(String name, List<Integer> successCodes) {
        this.name = name;
        this.successCodes = successCodes;
    }
}
