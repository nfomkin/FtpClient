package ru.nfomkin.ftpclient.ftp;

import java.util.List;

public enum Command {

    Get("RETR", List.of(150, 226)),
    User("USER", List.of(331)),
    Password("PASS", List.of(230)),
    Put("STOR", List.of(150, 226)),
    Quit("QUIT", List.of(221)),
    Active("PORT", List.of(200)),
    Passive("PASV", List.of(227));

    final String name;
    final List<Integer> successCodes;

    Command(String name, List<Integer> successCodes) {
        this.name = name;
        this.successCodes = successCodes;
    }
}
