package ru.netology;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

// класс для записи переписки в log.
public class LoggerUtil {
    // название
    public static final String DEFAULT_LOG_FILE = "file.log";
    // объект для записи логов
    public static PrintWriter log;

    static {
        // cтатический блок инициализации (создается один раз)
        initLog(DEFAULT_LOG_FILE);
    }


    public static void initLog(String fileName) {
        try {
            // файл для инициализации логов
            FileWriter logFile = new FileWriter(fileName, true);
            // инициализация объекта log
            log = new PrintWriter(logFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // метод записи в лог
    public static void printLog(String msg) {
        // формируем строку для записи
        log.printf("%s : %s\n", LocalDateTime.now(), msg);
        // записываем строку
        log.flush();
    }
    // метод по закртию .log
    public static void closedLog () {
        log.close();
    }
}
