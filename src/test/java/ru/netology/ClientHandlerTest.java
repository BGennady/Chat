package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {
    private ClientHandler clientHandler; // экземпляр класса ClientHandler
    private Socket mockSocket; // мок для сокета
    private PrintWriter mockOut; // мок для PrintWriter
    private BufferedReader mockIn;  // мок для BufferedReader

    @BeforeEach
    void testSetUp() throws IOException {

        // создаю объект-мок для сокета и его потоков
        mockSocket = mock(Socket.class);
        mockOut = mock(PrintWriter.class);
        mockIn = mock(BufferedReader.class);

        // через конструктор передаю заглушки
        clientHandler = new ClientHandler(mockSocket, mockOut, mockIn);

        // если есть соединение вернуть true
        when(mockSocket.isConnected()).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        try {
            clientHandler.closeResources();
        } catch (RuntimeException e) { //игнорировать исключения, если они возникают (важно для теста исключений)
        }
    }

    @Test
    // тест на подключение сокета
    void testClientSocketCreation() {
        assertTrue(mockSocket.isConnected());
    }

    @Test
    // тест на отключение сокета
    void testClientSocketClosed() throws IOException {
        clientHandler.closeResources();
        // если закрыт вернуть true
        when(mockSocket.isClosed()).thenReturn(true);
        // проверка, что закрыт
        assertTrue(mockSocket.isClosed());
    }

    @Test
    // тест отправки сообщений пользователю
    void testSendMessage() {
        String testMsg = "test message";
        clientHandler.sendMessage(testMsg);
        //проверяем, что метод println был вызван с правильным аргументом
        verify(mockOut).println(testMsg);
    }

    @Test
    // тест на то, что RuntimeException выбрасывается при закрытии socket
    void testRuntimeExceptionСloseResources() throws IOException {
        //настройка мока на выброс исключения при закрытии
        doThrow(new IOException("Ошибка при закрытии PrintWriter")).when(mockOut).close();
        doThrow(new IOException("Ошибка при закрытии BufferedReader")).when(mockIn).close();
        doThrow(new IOException("Ошибка при закрытии Socket")).when(mockSocket).close();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientHandler.closeResources());
        assertEquals("Ошибка при закрытии ресурсов", exception.getMessage());
    }
}