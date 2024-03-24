package com.codehunter.java_chat_app.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ChatServer {

    public static final Logger log = LogManager.getLogger(ChatServer.class);
    private static final List<ClientHandler> clients = new ArrayList<ClientHandler>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2000)) {
            log.info("Server started, waiting for connection");
            while (true) {
                Socket socket = serverSocket.accept();
                log.info("Client connected {}", socket.getInetAddress().getCanonicalHostName());
                ClientHandler clientThread = new ClientHandler(socket);
                clients.add(clientThread);

                new Thread(clientThread).start();
            }

        } catch (Exception e) {
            log.error(e);
        }

    }

    private static class ClientHandler implements Runnable {

        private static final Logger log = LogManager.getLogger(ClientHandler.class);
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            log.info("Client handler is running");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null && !inputLine.equals("null")) {
                    log.info(inputLine);
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

}
