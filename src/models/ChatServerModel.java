package models;

import config.Env;
import controllers.ChatServerController;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;

/**
 * Chatservermodel is a class that does the network things mostly.
 */
public class ChatServerModel {
    ChatServerMainReceiver chatMainReceiver;
    ServerSocket ss;
    ChatServer chatServer;
    ChatServerController chatServerController;
    ArrayList<ChatServerMainReceiver> receiversConnected;
    PassUtil passUtil;

    public ChatServerModel(){
        this.passUtil = new PassUtil();
    }

    public void setChatServerController(ChatServerController chatServerController){
        this.chatServerController = chatServerController;
    }

    /**
     * Stops the chat server
     * @return successful stopped server or not
     */
    public boolean stopChatServer(){
        try {
            if (chatServer != null) {
                if (chatServer.running) {
                    chatServer.running = false;
                    chatServer.stopChatServer = true;
                    try {
                        chatServer.stop();
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                    chatServer = null;
                } else {
                    JOptionPane.showMessageDialog(null, "Chat server is not running", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chat server is not running", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                chatServerController.getModel().ss.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
            chatServerController.getModel().ss = null;
            for (int i = 0; i < chatServerController.getModel().receiversConnected.size();i++){
                try {
                    chatServerController.getModel().receiversConnected.get(i).socket.close();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                chatServerController.getModel().receiversConnected.get(i).socket = null;
                try {
                    chatServerController.getModel().receiversConnected.get(i).stop();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                chatServerController.getModel().receiversConnected.remove(i);
            }
            chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Stopped main chat server", "RED");
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Starts the main chat server.
     * @return
     */
    public boolean startChatServer(){
        try {
            if (chatServer != null) {
                if (chatServer.running) {
                    JOptionPane.showMessageDialog(null, "Chat server is already running. Stop before trying to start it.", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            int serverPort = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter chat server port", Env.ChatServerMessageBoxTitle, JOptionPane.QUESTION_MESSAGE));
            if (serverPort <= 0){
                JOptionPane.showMessageDialog(null, "Port must be an integer higher than 0", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
            chatServer = new ChatServer(serverPort, chatServerController);
            chatServer.start();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public ChatServerMainReceiver getChatMainReceiver() {
        return chatMainReceiver;
    }

    public void setChatMainReceiver(ChatServerMainReceiver chatMainReceiver) {
        this.chatMainReceiver = chatMainReceiver;
    }

    public ServerSocket getSs() {
        return ss;
    }

    public void setSs(ServerSocket ss) {
        this.ss = ss;
    }

    public ChatServer getChatServer() {
        return chatServer;
    }

    public void setChatServer(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public ChatServerController getChatServerController() {
        return chatServerController;
    }

    public ArrayList<ChatServerMainReceiver> getReceiversConnected() {
        return receiversConnected;
    }

    public void setReceiversConnected(ArrayList<ChatServerMainReceiver> receiversConnected) {
        this.receiversConnected = receiversConnected;
    }

    /**
     * Forwards message to all clients except the client id that sent the message.
     * @param msg The message to forward
     * @param ID Exception id of client
     */
    public void forwardMessageToAllClients(String msg, int ID) {

        for (int i = 0; i < receiversConnected.size(); i++) {
            try {
                if (receiversConnected.get(i).ID != ID) { //to not send back to the sender
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(receiversConnected.get(i).socket.getOutputStream(), "UTF-8"), true);
                    writer.println(passUtil.toHexString(msg));
                    writer.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Forward message to specific client (filter by username). Loops through receivers arraylist and sends
     * to the receiver/client that's username equals username argument.
     * @param msg Message to send
     * @param username Username to send message to
     */
    public void forwardMessageToSpecific(String msg, String username) {
        for (int i = 0; i < receiversConnected.size(); i++) {
            try {
                if (receiversConnected.get(i).username.equals(username)) { //send to specific client
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(receiversConnected.get(i).socket.getOutputStream(), "UTF-8"), true);
                    writer.println(passUtil.toHexString(msg));
                    writer.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Ask user for input (id or username) and then close the socket connection that has that id or username.
     */
    public boolean disconnectClient() {
        if (JOptionPane.showConfirmDialog(null, "Do you want to enter client ID? Otherwise you have to enter client username. This will be used to disconnect client.", Env.ChatServerMessageBoxTitle,
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null)  == 0) {
            try {
                //ask for client id then disconnect
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } else {
            try {
                //ask for client username then disconnect
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return false;
    }
}
