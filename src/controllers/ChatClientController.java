package controllers;

import config.Env;
import models.ChatClientModel;
import models.User;
import views.ChatClientView;

import javax.swing.*;
import java.awt.event.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ChatClientController is the class that controls the client model and view.
 */
public class ChatClientController {
    ChatClientModel model;
    ChatClientView view;
    User user;
    ChatClientController chatClientController;

    /**
     * ChatClientController constructor
     * @param model The client model
     * @param view Client view
     * @param user User
     */
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
        addEmojisToComboBox();
        view.addItemListeners(new ItemChangeListener());
    }

    /**
     * Adds emojis to the JComboBox.
     */
    private void addEmojisToComboBox() {
        //byte[] emojiBytes = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x81};
        //String emojiAsString = new String(emojiBytes, Charset.forName("UTF-8"));
        view.getEmojiSelector().addItem("grinning face with smiling eyes");
        view.getEmojiSelector().addItem("face with tears of joy");
    }

    /**
     * ItemListener that listens for JComboBox item clicks.
     */
    private class ItemChangeListener implements ItemListener {
        /**
         * When item state changes for the jcombobox.
         * @param event The itemevent that happens when user click on a JComboBox item.
         */
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                try {
                    String emoji = (String) event.getItem();
                    System.out.println("Selected emoji: " + emoji);
                    if (emoji.equalsIgnoreCase("grinning face with smiling eyes")){
                        view.getTxtMessage().setText(view.getTxtMessage().getText() + String.format("%c", 0x1F601));
                        System.out.println(String.format("%c", 0x1F601));
                    } else if (emoji.equalsIgnoreCase("face with tears of joy")){
                        view.getTxtMessage().setText(view.getTxtMessage().getText() + String.format("%c", 0x1F602));
                        System.out.println(String.format("%c", 0x1F602));
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * FrameWindowListener listens for window events like windowclosing.
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
     * ChatClientListener listens for jmenuitems clicks.
     */
    private class ChatClientListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            System.out.println("Executed command: " + command);
            if (command != null){
                if (command.equalsIgnoreCase("Connect To Server")){
                    connectToServer();
                }
                if (command.equalsIgnoreCase("Disconnect From Server")){
                    disconnectFromServer();
                }
                if (command.equalsIgnoreCase("Send Message To Specific Client")){
                    sendMessageToSpecific();
                }
                if (command.equalsIgnoreCase("Send Message To All")){
                    sendMessageToAll();
                }
                if (command.equalsIgnoreCase("Clear Chat Messages")){
                    clearChatMessages();
                }
                if (command.equalsIgnoreCase("Exit program")){
                    view.dispose();
                }
            }
        }
    }

    /**
     * Tells the view to clear the textarea.
     */
    private void clearChatMessages() {
        view.getTxtLog().setText("");
    }

    /**
     * Tells model to disconnect from the server.
     */
    private void disconnectFromServer() {
        model.disconnectFromServer(true);
    }

    /**
     * Send message to specific client
     */
    void sendMessageToSpecific(){
        model.sendMessageToSpecific();
    }

    /**
     * Tells the client model to send message to all other clients and also the server.
     */
    void sendMessageToAll(){
        String msg = view.getTxtMessage().getText().trim();
        if (msg.equalsIgnoreCase("")){
            return;
        }
        model.sendMessageToAll(msg);
        view.getTxtMessage().setText("");
    }

    /**
     * Ask user for port and tells model to try connect to the server.
     */
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

    /**
     * Append text or image to jtextarea on client view with text colors.
     * @param msg Text to display on jtextarea
     * @param color The text color
     * @param imgpath The string path to where the image is stored
     */
    public void appendToPane(String msg, String color, String imgpath){
        view.appendToPane(view.txtLog, msg, color, imgpath);
        System.out.println("Image Path: " + imgpath);
    }

    public ChatClientModel getModel() {
        return model;
    }

    public void setModel(ChatClientModel model) {
        this.model = model;
    }

    public ChatClientView getView() {
        return view;
    }

    public void setView(ChatClientView view) {
        this.view = view;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatClientController getChatClientController() {
        return chatClientController;
    }

    public void setChatClientController(ChatClientController chatClientController) {
        this.chatClientController = chatClientController;
    }
}
