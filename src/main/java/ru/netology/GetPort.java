package ru.netology;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// класс для получения номера порта
public class GetPort {
    static String fileName = "settings.txt";
    public static int port = readPortFromFile(fileName);

    // считываем порт из файла
    public static int readPortFromFile(String fileName) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(fileName))
        ) {
            String line;
            // получаем следующую строку
            while ((line = reader.readLine()) != null) { // проверяем что не пустая
                if (line.startsWith("port=")) {  // проверяем на содержание искомых элементов
                    // разбиваем по символу "=", берем второй [1] элемент и удалем возможные пробелы
                    return Integer.parseInt(line.split("=")[1].trim());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
