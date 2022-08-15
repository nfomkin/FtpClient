package ru.nfomkin.ftpclient.ftp;

public enum ErrorMessage {
    Connection("Проблемы с подключением. Попробуйте еще раз"),
    Authentication("Проблемы с аутентификацией. Попробуйте еще раз");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
