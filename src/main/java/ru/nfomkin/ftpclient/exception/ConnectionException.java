package ru.nfomkin.ftpclient.exception;

import java.io.IOException;

public class ConnectionException extends IOException {
    public ConnectionException() {
        super();
    }

    public ConnectionException(String message) {
        super(message);
    }
}
