package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// класс клиента
public class Client {
    PrintWriter out;
    BufferedReader in;

    // конструктор клиента, принимающий сокет и инициализирующий потоки
    public Client(Socket clientSocket) throws IOException {
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static void main(String[] args) throws IOException {

        final String HOST = "127.0.0.1"; // адрес сервера
        final int PORT = GetPort.port;   // получение номера порта из класса GetPort

        if (PORT == -1) {
            System.err.println("Ошибка подключения к порту");
            return; // выход если порт не задан
        }
        try (
                Socket clientSocket = new Socket(HOST, PORT)) {
            Client client = new Client(clientSocket);
            client.start();
        }
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println(in.readLine()); // получаем первое сообщение от сервера
            String name = sc.nextLine(); // сохраняем имя в переменную
            out.println(name); // отправляем его на сервер

            // цикл для чтения сообщений от сервера и отправки своих сообщений
            while (true) {
                // получаю сообщение от сервера
                String serverResponse = in.readLine();
                if (serverResponse == null) {  // проверка, что не пустое
                    System.out.println("Сервер отключился");
                    break; // выход из цикла если сервер отключился
                }
                System.out.println(serverResponse); // вывод сообщения на экран

                // отправляю свои сообщения
                if (sc.hasNextLine()) {  // проверка на наличие сообщений
                    String userInput = sc.nextLine();  // получает сообщение
                    out.println(userInput); // отправляет его на сервер

                    // выход из чата по команде /exit
                    if (userInput.equalsIgnoreCase("/exit")) {
                        System.out.println("Вы отключились");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        }
    }
}