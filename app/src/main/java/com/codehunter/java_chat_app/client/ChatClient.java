package com.codehunter.java_chat_app.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

class ChatClient implements Closeable {

    public static final Logger log = LogManager.getLogger(ChatClient.class);
    private final Socket socket;
    private final Consumer<String> onMessageListener;
    private final BufferedReader socketReader;
    private final PrintWriter writer;

    public ChatClient(String host, int port, Consumer<String> onMessageListener) throws IOException {
        this.socket = new Socket(host, port);
        this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageListener = onMessageListener;
    }

    public void startListen() {
        Runnable runnable = () -> {
            try {
                String receiveMessage;
                while ((receiveMessage = socketReader.readLine()) != null && !receiveMessage.equals("null")) {
                    onMessageListener.accept(receiveMessage);
                }
            } catch (IOException e) {
                log.error(e, e);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void sentMessage(String message) {
        writer.println(message);
    }

    public static void main(String[] args) {

        try (var chatClient = new ChatClient("localhost", 2000, System.out::println);){
            log.info("connected");
            // process to display all message
            chatClient.startListen();
            // send message to socket
            String line = "";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));) {
                while (!"exit".equals(line)) {
                    line = in.readLine();
                    chatClient.sentMessage(line);
                }
            } catch (Exception e) {
                log.error(e, e);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
        this.socketReader.close();
        this.socket.close();
    }
}
