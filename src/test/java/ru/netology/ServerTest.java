package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ServerTest {
    private Socket mockSocket;
    private PrintWriter mockPrintWriter;
    private ClientHandler clientHandler;

    @BeforeEach
    void testSetUp() {
        // создание списка клиентов перед каждым тестом
        Server.clients = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        // очистка списка клиентов
        Server.clients.clear();
    }

    @Test
    void testBroadcastMessage() {
        // создаю мок-сокет и мок-PrintWriter
        mockSocket = mock(Socket.class);
        mockPrintWriter = mock(PrintWriter.class);

        // создаю нового клиента и добавляю его в список
        clientHandler = new  ClientHandler (mockSocket, mockPrintWriter, null);
        Server.clients.add(clientHandler);

        // отправка тестового сообщения через метод broadcastMessage
        String msg = "test";
        Server.broadcastMessage(msg);

        // проверка, что метод sendMessage был вызван с нужным сообщением
        verify(mockPrintWriter).println(msg);
    }
    @Test
    void testRemoveClient(){
        // создаю нового клиента и добавляю его в список
        clientHandler = new ClientHandler(mockSocket, mockPrintWriter, null);
        Server.clients.add(clientHandler);

        // удаляю его из списка
        Server.clients.remove(clientHandler);

        // проверяю, что его в списке нет
        assertFalse(Server.clients.contains(clientHandler));
    }
}
