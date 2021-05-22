package models;

import config.Env;
import controllers.ChatServerController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
    Base64Helper base64Helper;

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
        base64Helper = new Base64Helper();
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
                chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Client disconnected: " + socket, "RED", null);
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
                    chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Client self-disconnected: " + socket + " username: " + username + " ID: " + ID, "RED", null);
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
                    String chatmessages = getMessagesFromDB(username);
                    chatServerController.getModel().forwardMessageToSpecific(chatmessages, username);
                }
                if (message.startsWith("msgspecific|split|")){
                    String[] data = message.split("\\|split\\|");
                    String usernameSendTo = data[1];
                    username = data[2];
                    String msg = data[3];
                    System.out.println("SPECIFIC RECEIVED FROM:  " + ID + ": " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    chatServerController.getModel().forwardMessageToSpecific("msgspecific|split|" + usernameSendTo + "|split|" + username + "|split|" + msg, usernameSendTo);
                    chatServerController.appendToPane( socket + " at " + new Date(System.currentTimeMillis()) + ": ID: " + ID + ": Specific from: " + username + ": to: " + usernameSendTo + ": " + msg, Env.messageColor, null);

                    chatServerController.getModel().logMessageDB("chatmessages", username, msg, usernameSendTo, null);
                }
                if (message.startsWith("image|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String base64Img = data[2];
                    System.out.println("RECEIVED IMAGE-BASE64 FROM:  " + ID + ": " + new Date(System.currentTimeMillis()) + ": " + username + ": "+ base64Img);
                    chatServerController.getModel().forwardMessageToAllClients("image|split|" + username + "|split|" + base64Img, ID);
                    BufferedImage image = base64Helper.decodeBase64StringToImage(base64Img);
                    if (image != null){
                        //int random = 1 + (int) (Math.random() * 100000);
                        File dir = new File(System.getProperty("user.dir") + "/chatimages/");
                        int pos = Objects.requireNonNull(dir.list()).length;
                        File f = new File(System.getProperty("user.dir") + "/chatimages/" + username + "-" + pos + ".png");
                        ImageIO.write(image, "png", f);
                        chatServerController.appendToPane( socket + " at " + new Date(System.currentTimeMillis()) + ": ID: " + ID + ": " + username + ": ", Env.messageColor, f.getAbsolutePath());
                    }
                   chatServerController.getModel().logMessageDB("chatmessages", username, null, "All", base64Img);
                }
                if (message.startsWith("msg|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String msg = data[2];
                    System.out.println("RECEIVED FROM:  " + ID + ": " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    chatServerController.getModel().forwardMessageToAllClients("msg|split|" + username + "|split|" + msg, ID);
                    chatServerController.appendToPane( socket + " at " + new Date(System.currentTimeMillis()) + ": ID: " + ID + ": " + username + ": " + msg, Env.messageColor, null);
                    chatServerController.getModel().logMessageDB("chatmessages", username, msg, "All", null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Processor completed ");
    }


    /**
     * Gets all messages from database where sender equals username or where receiver contains username or All.
     * @param username The username of the specific client that just connected
     * @return All chat messages that are relevant for this user to see.
     */
    private String getMessagesFromDB(String username) {
        try{
            ResultSet rs = chatServerController.getModel().getAllMessages();
            StringBuilder sb = new StringBuilder();
            sb.append("chatmessages|end|");
            int count = 0;
            while(rs.next()) {
                if (count == 50){
                    break;
                }
                String msg = rs.getString("BODY");
                String datetime = rs.getString("DATE_TIME");
                String sender = rs.getString("SENDER").trim();
                String receiver = rs.getString("RECEIVER").trim();
                String img_base64 = rs.getString("IMG_BASE64");
                if (sender.equals(username.trim())){
                    sb.append(sender + "|split|" + msg + "|split|" + datetime + "|split|" + receiver + "|split|" + img_base64 + "|end|");
                    count++;
                } else if (receiver.toLowerCase().contains(username.trim().toLowerCase()) || receiver.equals("All")){
                    sb.append(sender + "|split|" + msg + "|split|" + datetime + "|split|" + receiver + "|split|" + img_base64 + "|end|");
                    count++;
                }
            }

            String messages = sb.toString();
            messages = messages.substring(0, (messages.length() -  "|end|".length()));
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

