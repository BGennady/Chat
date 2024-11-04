package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.*;

class ClientTest {

    private Socket mockSocket;
    private PrintWriter mockPrintWriter;
    private BufferedReader mockBufferedReader;
    private Client client;

    @BeforeEach
    void setUp() throws IOException {
        // моки для сокета и потоков
        mockSocket = Mockito.mock(Socket.class);
        mockPrintWriter = Mockito.mock(PrintWriter.class);
        mockBufferedReader = Mockito.mock(BufferedReader.class);

        // настройка поведения мока сокета
        when(mockSocket.getOutputStream()).thenReturn(Mockito.mock(java.io.OutputStream.class));
        when(mockSocket.getInputStream()).thenReturn(Mockito.mock(java.io.InputStream.class));

        // клиент
        client = new Client(mockSocket);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (mockSocket != null && !mockSocket.isClosed()) {
            mockSocket.close();
        }
    }

    @Test
        // тест инициализации клиента
    void testClientInitialization() throws IOException {
        verify(mockSocket).getOutputStream();
        verify(mockSocket).getInputStream();
    }
}
