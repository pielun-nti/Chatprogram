package controllers;

import config.Env;
import models.ChatClientModel;
import models.User;
import views.ChatClientView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ChatClientController {
    ChatClientModel model;
    ChatClientView view;
    User user;
    ChatClientController chatClientController;
    public ChatClientController(ChatClientModel model, ChatClientView view, User user){
        this.model = model;
        this.view = view;
        this.user = user;
        this.view.addListeners(new ChatClientListener());
        this.view.addFrameWindowListener(new FrameWindowListener());
        this.chatClientController = this;
        model.setChatClientController(chatClientController);
        view.getTxtServerIP().setText("127.0.0.1");
        view.getTxtServerPort().setText("5900");
    }
    private class FrameWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent windowEvent) {

        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
            view.dispose();
        }

        @Override
        public void windowClosed(WindowEvent windowEvent) {

        }

        @Override
        public void windowIconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeiconified(WindowEvent windowEvent) {

        }

        @Override
        public void windowActivated(WindowEvent windowEvent) {

        }

        @Override
        public void windowDeactivated(WindowEvent windowEvent) {


        }
    }

    private class ChatClientListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Connect To Server")){
                    connectToServer();
                }
                if (command.equalsIgnoreCase("Send Message")){
                    sendMessageToAll();
                }
                if (command.equalsIgnoreCase("Exit program")){
                    view.dispose();
                }
            }
        }
    }
    void sendMessageToAll(){
        String msg = view.getTxtMessage().getText().trim();
        if (msg.equalsIgnoreCase("")){
            return;
        }
        model.sendMessageToAll(msg);
        view.getTxtMessage().setText("");
    }
    void connectToServer(){
        String ip = view.getTxtServerIP().getText().trim();
        if (ip.equalsIgnoreCase("") || !ip.contains(".") & !ip.equalsIgnoreCase("localhost")){
            JOptionPane.showMessageDialog(null, "Incorrect ip.", Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
        try {
        int port = Integer.parseInt(view.getTxtServerPort().getText().trim());
        if (port <= 0){
            JOptionPane.showMessageDialog(null, "Incorrect port. Must be an integer higher than 0.", Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
            model.connectToServer(ip, port);
        } catch (NumberFormatException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Incorrect port: " + ex.toString(), Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }


    }
    public void appendToPane(String msg, String color){
        view.appendToPane(view.txtLog, msg, color);
    }
}
