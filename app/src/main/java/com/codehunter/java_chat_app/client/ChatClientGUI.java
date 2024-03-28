package com.codehunter.java_chat_app.client;

import javax.swing.*;

class ChatClientGUI {

    private static void createAndDisplay() {
        // setup window
        JFrame frame = new JFrame("Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add label
        JLabel label = new JLabel("Hello Swing");
        // frame.getContentPane().add(label);
        frame.add(label);

        var textField = new JTextField();
        textField.setSize(500, 50);
        frame.add(textField);

        var button = new JButton("Send");
        button.setSize(100, 50);
        frame.add(button);

        // Display window
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndDisplay());
    }

}
