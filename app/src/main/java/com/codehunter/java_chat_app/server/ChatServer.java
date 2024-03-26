package com.codehunter.java_chat_app.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ChatServer {

    public static final Logger log = LogManager.getLogger(ChatServer.class);
    private static final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2000)) {
            log.info("Server started, waiting for connection");
            while (true) {
                Socket socket = serverSocket.accept();
                log.info("Client connected {}", socket.getInetAddress().getCanonicalHostName());
                ClientHandler clientThread = new ClientHandler(socket, clients);
                clients.add(clientThread);

                new Thread(clientThread).start();
            }

        } catch (Exception e) {
            log.error(e, e);
        }

    }

    private static class ClientHandler implements Runnable {

        private static final Logger log = LogManager.getLogger(ClientHandler.class);
        private final Socket clientSocket;
        private final List<ClientHandler> allClients;
        public PrintWriter writer;

        public ClientHandler(Socket clientSocket, List<ClientHandler> allClients) {
            this.clientSocket = clientSocket;
            this.allClients = allClients;
        }

        @Override
        public void run() {
            log.info("Client handler is running");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                this.writer = new PrintWriter(this.clientSocket.getOutputStream(), true);
                String inputLine;
                while ((inputLine = in.readLine()) != null && !inputLine.equals("null")) {
                    onReceiveMessage(inputLine);
                }
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                this.writer.close();
            }
        }

        private void onReceiveMessage(String message) {
            log.info("Receive message: {}", message);
            this.allClients.forEach(client -> {
                client.notify(message);
            });
        }

        public void notify(String message) {
            this.writer.println(">" + Thread.currentThread().getName() + ": " + message);
        }
    }

}
