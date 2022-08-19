package ru.nfomkin.ftpclient.ftp;

public enum ErrorMessage {
    Connection("Не удается подключиться к серверу. Попробуйте еще раз"),
    Authentication("Проблемы с аутентификацией. Попробуйте еще раз");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
