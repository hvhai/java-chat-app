package com.codehunter.java_chat_app.client;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class ChatClient {
    public static final Logger log = LogManager.getLogger(ChatClient.class);
    
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 2000)) {
            log.info("connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
