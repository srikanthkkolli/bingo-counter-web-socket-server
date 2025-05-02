package com.poc.websocketserver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MySocketHandlerTest {

    private MySocketHandler handler;

    @BeforeEach
    void setUp() {
        handler = new MySocketHandler();
    }

    @AfterEach
    void tearDown() {
        handler = null;
    }

    @Test
    void testGetIntegers() {
        List<Integer> result = MySocketHandler.getIntegers("5");
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    void testIsInteger_Valid() {
        assertTrue(MySocketHandler.isInteger("123"));
    }

    @Test
    void testIsInteger_Invalid() {
        assertFalse(MySocketHandler.isInteger("abc"));
    }

    @Test
    void testHandleTextMessage_withValidNumber() throws Exception {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        when(mockSession.getId()).thenReturn("mock-id");

        handler.handleTextMessage(mockSession, new TextMessage("3"));

        verify(mockSession, atLeastOnce()).sendMessage(any(TextMessage.class));
    }

    @Test
    void testHandleTextMessage_withInvalidNumber() throws Exception {
        WebSocketSession mockSession = mock(WebSocketSession.class);
        when(mockSession.getId()).thenReturn("mock-id");

        handler.handleTextMessage(mockSession, new TextMessage("not-a-number"));

        verify(mockSession).sendMessage(new TextMessage("BINGOOOO!!!"));
    }
}