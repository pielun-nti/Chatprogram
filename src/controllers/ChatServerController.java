package controllers;

import models.ChatServer;
import models.ChatServerModel;
import views.ChatServerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * ChatServerController controls the server model and view.
 */
public class ChatServerController {
    ChatServerModel model;
    ChatServerView view;
    ChatServerController chatServerController;

    /**
     * ChatServerController Constructor.
     * @param model The chat server model
     * @param view The chat server view
     */
    public ChatServerController(ChatServerModel model, ChatServerView view){
        this.model = model;
        this.view = view;
        this.chatServerController = this;
        this.model.setChatServerController(chatServerController);
        this.view.addListeners(new ChatServerListener());
        this.view.addFrameWindowListener(new FrameWindowListener());
    }

    /**
     * FrameWindowListener that listens for window events like windowclosing.
     */
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

    /**
     * ChatServerListener listens for jmenuitems clicks.
     */
    private class ChatServerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Start Chat Server")){
                    startChatServer();
                }
                if (command.equalsIgnoreCase("Stop Chat Server")){
                    stopChatServer();
                }
                if (command.equalsIgnoreCase("Exit program")){
                    view.dispose();
                }
            }
        }
    }

    /**
     * Tells model to stop the chat server.
     */
    private void stopChatServer() {
        model.stopChatServer();
    }

    /**
     * Tells model to start the chat server.
     */
    void startChatServer(){
        model.startChatServer();
    }

    public ChatServerModel getModel() {
        return model;
    }

    /**
     * Tells the server view to append text to jtextarea with text color.
     * @param msg Text to add
     * @param color Text color
     */
    public void appendToPane(String msg, String color){
        view.appendToPane(view.txtLog, msg, color);
    }

    public void setModel(ChatServerModel model) {
        this.model = model;
    }

    public ChatServerView getView() {
        return view;
    }

    public void setView(ChatServerView view) {
        this.view = view;
    }

    public ChatServerController getChatServerController() {
        return chatServerController;
    }

    public void setChatServerController(ChatServerController chatServerController) {
        this.chatServerController = chatServerController;
    }
}
