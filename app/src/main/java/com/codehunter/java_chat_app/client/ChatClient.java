package com.codehunter.java_chat_app.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ChatClient {

    public static final Logger log = LogManager.getLogger(ChatClient.class);

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 2000);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());) {
                log.info("connected");
                String line = "";
                while (!"exit".equals(line)) {
                    line = in.readLine();
                    log.info("input line: {}", line);
                    writer.println(line);
                }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
