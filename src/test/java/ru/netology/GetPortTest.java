package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.util.stream.Stream;

public class GetPortTest {
    public static final String TEST_FILE = "test_settings.txt";

    // создаем временный файл перед каждым тестом
    @BeforeEach
    void testSetUp() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(TEST_FILE));
        writer.print("port=8090");
        writer.close(); // закрываем поток
    }

    // удаляем временный файл после каждого теста
    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    // тест на корректность считывания номер порта
    public void testReadPort() {
        int testPort = GetPort.readPortFromFile(TEST_FILE);  // читаем порт из файла
        assertEquals(8090, testPort); // сравниваем
    }

    // метод, предоставляющий параметры для тестирования
    public static Stream<Arguments> testReadPortParameterized() {
        return Stream.of(
                Arguments.of("port=8090", 8090), // корректный случай
                Arguments.of("port=65535", 65535), // максимально допустимый порт
                Arguments.of("port=0", 0), // минимально допустимый порт
                Arguments.of("invalid_port", -1) // некорректный формат
        );
    }

    @ParameterizedTest
    @MethodSource
    void testReadPortParameterized(String testPort, int expected) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_FILE))) {
            writer.print(testPort);
        }
        int currentPort = GetPort.readPortFromFile(TEST_FILE);
        assertEquals(expected, currentPort);
    }

    @Test
    // тест на то, что RuntimeException выбрасывается при отсутствии файла
    void testRuntimeException () {
        assertThrows(RuntimeException.class, ()-> {
            GetPort.readPortFromFile("fake_file.txt");
        });
    }
}
