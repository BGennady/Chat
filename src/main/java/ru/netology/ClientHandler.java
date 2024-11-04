package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// класс обработчик клиентов
public class ClientHandler implements Runnable {
    private final Socket clientSocket; // сокет клиента
    private PrintWriter out; // исходящтй поток
    private BufferedReader in; // входящий поток

    public ClientHandler(Socket clientSocket, PrintWriter out, BufferedReader in) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            // сообщение о присвоение номера порта
            System.out.printf("New connection accepted. Port: %d%n", clientSocket.getPort());
            out.println("Добрый день, представтесь пожалуйста"); // запрос имени у клиента
            final String name = in.readLine(); // получение имени клиента

            // оповещение всех пользоватей чата о новом участнике
            Server.broadcastMessage("Пользователь " + name + " присоединился к чату");

            String message;
            // в цикле получаем сообщение от клиента
            while ((message = in.readLine()) != null) { // проверяем что не пустое
                if (message.equalsIgnoreCase("/exit")) { // проверяем на наличие команды /exit

                    // оповещение всех пользоватей чата о выходе участника
                    Server.broadcastMessage(name + " вышел из чата");
                    break;
                }
                // рыссылка сообщения всем пользователям
                Server.broadcastMessage(name + ": " + message);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
        } finally {
            closeResources(); // закрыть потоки и сокет клиента при отключении
            Server.removeClient(this); // удалить клиента из списка при отключении
        }
    }

    public void closeResources() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при закрытии ресурсов", e); // Обработка исключения
        }
    }

    // отправка сообщения клиенту
    public void sendMessage(String message) {
        out.println(message);
    }
}
