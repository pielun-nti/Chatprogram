package models;

import config.Env;
import controllers.ChatServerController;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;

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
}
