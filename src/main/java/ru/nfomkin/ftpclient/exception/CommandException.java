package ru.nfomkin.ftpclient.exception;

import java.io.IOException;

public class CommandException extends IOException {
    public CommandException() {
        super();
    }

    public CommandException(String message) {
        super(message);
    }
}
