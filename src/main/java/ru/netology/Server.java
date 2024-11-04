package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// класс сервер
public class Server {
    // список для хранения обработчиков клиентов
    public static List<ClientHandler> clients = new ArrayList<>();
    private final int PORT;

    public Server(int port) {
        this.PORT = port;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        int port = GetPort.port; // получение номера порта из класса GetPort

        if (port == -1) {
            System.err.println("Ошибка получени номера порта");
            return; // выход из main
        }
        // передаю в port в конструтор класса Server
        Server server = new Server(port);
        server.start();
    }

    // метод подключения новых пользователей
    public void start() {
        try (
                //серверный сокет
                ServerSocket serverSocket = new ServerSocket(PORT);
        ) {
            while (true) {
                // сервер в цикле ожидает подключения клиента
                Socket clientSocket = serverSocket.accept();
                // сообщение о подключении
                System.out.printf("New connection accepted. Port: %d%n", clientSocket.getPort());

                // создание потоков
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // создание обработчика для нового клиента и передача в него сокета и потоков
                ClientHandler clientHandler = new ClientHandler(clientSocket, out, in);
                clients.add(clientHandler); // добавляем в список
                new Thread(clientHandler).start(); //запуск нового потока для обработки клиента
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        // метод для рассылки по всем пользователям
        public static synchronized void broadcastMessage (String msg){
            for (ClientHandler client : clients) {
                client.sendMessage(msg);
            }
            LoggerUtil.printLog(msg);
        }

        // метод для удаления отключившегося клиента
        public static synchronized void removeClient (ClientHandler clientHandler){
            clients.remove(clientHandler); // удаление из списка
        }
    }
