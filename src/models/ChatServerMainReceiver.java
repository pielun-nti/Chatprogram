package models;

import config.Env;
import controllers.ChatServerController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

public class ChatServerMainReceiver extends Thread {
    ChatServerController chatServerController;
    Socket socket;
    PassUtil passUtil;
    String message;
    String clientIP;
    int ID;
    String username;
    String country;
    public ChatServerMainReceiver(int ID, Socket socket, ChatServerController chatServerController) {
        this.ID = ID;
        this.socket = socket;
        this.chatServerController = chatServerController;
        passUtil = new PassUtil();
    }

    /**
     * Reads the stream until the first /r/n
     */
    private String readFromStream(BufferedReader stream) //Read string line from the stream
    {
        try {
            return stream.readLine(); //Read the stream until the first \r\n
        } catch (Exception ex) {
            System.err.println("readFromStream Error: " + ex);
            if (ex.toString().contains("Connection reset")){
                Info.clientsConnected--;
                chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Client disconnected: " + socket, "RED");
                try {
                    chatServerController.getModel().getReceiversConnected().remove(ID);
                    socket = null;
                    stop();
                } catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
            return null;
        }
    }


    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            while (!socket.isClosed()) {
                String hexMsg = readFromStream(br);
                if (hexMsg == null) {
                    continue;
                }
                message = passUtil.hexToString(hexMsg);
                if (message == null) {
                    continue;
                }
                clientIP = socket.getInetAddress().getHostAddress();
                if (message.startsWith("msg|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String msg = data[2];
                    System.out.println("RECEIVED FROM:  " + ID + ": " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    chatServerController.getModel().forwardMessageToAllClients("msg|split|" + username + "|split|" + msg, ID);
                    chatServerController.appendToPane( socket + " at " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg, Env.messageColor);
                    //log chat message in database then retrieve&print around 10 msgs on start for both server & client?
                    //send the last 10 msgs from server to each new client connected?
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Processor completed ");
    }

}

