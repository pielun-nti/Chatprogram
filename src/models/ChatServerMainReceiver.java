package models;

import config.Env;
import controllers.ChatServerController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

/**
 * ChatServerMainReceiver is a thread that listens for received messages from any client.
 */
public class ChatServerMainReceiver extends Thread {
    ChatServerController chatServerController;
    Socket socket;
    PassUtil passUtil;
    String message;
    String clientIP;
    int ID;
    String username;
    String country;

    /**
     * Constructor
     * @param ID The id of this receiver
     * @param socket The socket that is what we communicate with
     * @param chatServerController The server controller
     */
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


    /**
     * The server main receiver thread runs method reads socket inputstream using inputstreamreader and then
     * converts to bufferedreader and then while socket is not closed, it will read incoming message from the socket inputstream
     * using readFromStream method (and that text will be in hex because client sends it in hex), so then it
     * converts it to string using hexToString method and then it splits the string and displays username and message
     * in a gui.
     */
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
                if (message.equals("disconnect-me")){
                    Info.clientsConnected--;
                    chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Client self-disconnected: " + socket + " username: " + username + " ID: " + ID, "RED");
                    try {
                        chatServerController.getModel().getReceiversConnected().remove(ID);
                        socket = null;
                        stop();
                    } catch (Exception ex2){
                        ex2.printStackTrace();
                    }
                    System.out.println("Self-disconnected client with ID: " +  ID + " and username: " + username + " " + socket);
                }
                if (message.startsWith("setusername|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    System.out.println("Set username: " + username);
                }
                if (message.startsWith("msgspecific|split|")){
                    String[] data = message.split("\\|split\\|");
                    String usernameSendTo = data[1];
                    username = data[2];
                    String msg = data[3];
                    System.out.println("SPECIFIC RECEIVED FROM:  " + ID + ": " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    chatServerController.getModel().forwardMessageToSpecific("msgspecific|split|" + usernameSendTo + "|split|" + username + "|split|" + msg, usernameSendTo);
                    chatServerController.appendToPane( socket + " at " + new Date(System.currentTimeMillis()) + ": ID: " + ID + ": Specific from: " + username + ": to: " + usernameSendTo + ": " + msg, Env.messageColor);
                    //log chat message in database then retrieve&print around 10 msgs on start for both server & client?
                    //send the last 10 msgs from server to each new client connected?
                    //Do this if i have time over
                }
                if (message.startsWith("msg|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String msg = data[2];
                    System.out.println("RECEIVED FROM:  " + ID + ": " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    chatServerController.getModel().forwardMessageToAllClients("msg|split|" + username + "|split|" + msg, ID);
                    chatServerController.appendToPane( socket + " at " + new Date(System.currentTimeMillis()) + ": ID: " + ID + ": " + username + ": " + msg, Env.messageColor);
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

