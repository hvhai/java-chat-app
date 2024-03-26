package com.codehunter.java_chat_app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ChatClient {

    public static final Logger log = LogManager.getLogger(ChatClient.class);

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 2000);) {
            log.info("connected");

            // process to display all message
            Runnable runnable = () -> {
                try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String receiveMessage;
                    while ((receiveMessage = socketReader.readLine()) != null && !receiveMessage.equals("null")) {
                        System.out.println(receiveMessage);
                    }
                } catch (IOException e) {
                    log.error(e, e);
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();

            // send message to socket
            String line = "";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
                while (!"exit".equals(line)) {
                    line = in.readLine();
                    writer.println(line);
                }
            } catch (Exception e) {
                log.error(e, e);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
