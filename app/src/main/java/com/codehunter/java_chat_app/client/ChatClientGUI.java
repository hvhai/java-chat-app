package com.codehunter.java_chat_app.client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

class ChatClientGUI {

    private static void createAndDisplay() {
        // setup window
        JFrame frame = new JFrame("Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add label
        JLabel label = new JLabel("Hello Swing");
        frame.getContentPane().add(label);

        // Display window
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndDisplay());
    }

}
