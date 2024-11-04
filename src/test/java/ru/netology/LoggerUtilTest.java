package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerUtilTest {

    // имя файла для логирования
    public static String testName;

    @BeforeEach
    public void testSetUp() throws IOException {
        testName = "test_file.log";
        LoggerUtil.initLog(testName);
    }

    @AfterEach
    public void tearDown() {
        LoggerUtil.closedLog(); // закрываем логгер после теста
        new File(testName).delete(); // удаляем временный файл после теста
    }

    @Test
    // тест для проверки, метода по созданию файла log.
    public void testInitLog() {
        Path path = new File(testName).toPath();
        assertTrue(Files.exists(path)); // проверяет, что файл по указанному пути существует
    }


    @Test
    // тест для проверки записи в log.
    public void testPrintLog() throws IOException {
        String msg = "test message"; // сообщение
        LoggerUtil.printLog(msg); // записываем сообщение в лог
        BufferedReader reader = new BufferedReader(new FileReader(testName)); // создаем BufferedReader для чтения из файла
        String line = reader.readLine(); // читаем первую строку из лога
        assertTrue(line.contains(msg)); // проверяем, что строка содержит сообщение
        reader.close(); // закрываем BufferedReader
    }

    @Test
    // тест на то что .log закрыт
    public void testClosedLog() {
        LoggerUtil.closedLog(); // закрываем логгер
        LoggerUtil.closedLog(); // не должно быть исключения при повторном закрытии
        assertTrue(true); // если доходит до этой строки, значит, тест прошел успешно
    }

    @Test
    // тест на то, что RuntimeException выбрасывается при отсутствии файла
    public void testRuntimeException() {
        String invalidPath = "/invalid_path/file.log"; // указываем путь, который вызывает ошибку
        assertThrows(RuntimeException.class, () -> {
                    LoggerUtil.initLog(invalidPath);
                }
        );

    }
}
