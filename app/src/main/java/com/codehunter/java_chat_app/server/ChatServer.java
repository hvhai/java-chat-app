package com.codehunter.java_chat_app.server;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



class ChatServer {
    public static final Logger log = LogManager.getLogger(ChatServer.class);
    

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2000)) {
            log.info("Server started, waiting for connection");
            Socket socket = serverSocket.accept();
            log.info("Client connected {}", socket.getInetAddress().getCanonicalHostName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}