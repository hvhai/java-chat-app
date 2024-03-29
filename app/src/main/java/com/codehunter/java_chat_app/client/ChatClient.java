package com.codehunter.java_chat_app.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ChatClient implements Closeable {

    public static final Logger log = LogManager.getLogger(ChatClient.class);
    private final Consumer<String> onMessageListener;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader socketReader;
    private String username;

    public ChatClient(Consumer<String> onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public void connect(String host, int port, String username) throws IOException {
        this.username = username;
        this.socket = new Socket(host, port);
        this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        startListen();
    }

    private void startListen() {
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
        String formatedMessage = String.format("> %s: %s", this.username, message);
        writer.println(formatedMessage);
    }

    public static void main(String[] args) {

        try (var chatClient = new ChatClient(System.out::println);){
            chatClient.connect("localhost", 2000, UUID.randomUUID().toString());
            log.info("connected");
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
    
    public String getUsername() {
        return this.username;
    }
}
