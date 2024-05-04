package com.poc.websocketserver;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Socket to handle
 */
@Slf4j
public class MySocketHandler extends TextWebSocketHandler {


    private static List<Integer> getIntegers(String payload) {
        int maxNumber = Integer.parseInt(payload);
        List<Integer> integerList = new ArrayList<>(maxNumber);
        for (int i = 1; i <= maxNumber; i++) {
            integerList.add(i);
        }
        return integerList;
    }

    private static void wait500() throws InterruptedException {
        Thread.sleep(500);
    }

    private static void shuffleAndSend(WebSocketSession session, List<Integer> integerList) {
        Collections.shuffle(integerList);

        integerList.forEach(n -> {
            try {
                wait500();
                session.sendMessage(new TextMessage(n.toString()));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static boolean isInteger(String payload) {
        try {
            Integer.parseInt(payload);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Takes input number, randomizes the numbers and sends it back over the socket
     *
     * @param session: current session
     * @param message: number payload
     */
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received message: {} ", payload);

        if (isInteger(payload)) {
            List<Integer> integerList = getIntegers(payload);
            session.sendMessage(new TextMessage("Randomizing numbers upto: " + payload));
            wait500();
            shuffleAndSend(session, integerList);
        }
        session.sendMessage(new TextMessage("Completed"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("New client connection established: {}\n", session.getId());
        session.sendMessage(new TextMessage("New WebSocket connection established"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
        log.info("Shutting down client connection: {}\n", session.getId());
    }
}

