package models;

import config.Env;
import controllers.ChatClientController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ChatClientMainReceiver is a thread that reads messages from the socket using inputstreamreader and bufferedreader.
 */
public class ChatClientMainReceiver extends Thread {
    ChatClientController chatClientController;
    Socket socket;
    PassUtil passUtil;
    String message;
    String clientIP;
    String username;
    String country;
    Base64Helper base64Helper;

    /**
     * Constructor 1 with only socket argument (only used for test classes)
     * @param socket The client connected
     */
    public ChatClientMainReceiver(Socket socket) {
        this.socket = socket;
        passUtil = new PassUtil();
        base64Helper = new Base64Helper();
    }

    /**
     * Constructor 2
     * @param socket The client connected
     * @param chatClientController The chat client controller used to interact with client gui, model etc
     */
    public ChatClientMainReceiver(Socket socket, ChatClientController chatClientController) {
        this.socket = socket;
        this.chatClientController = chatClientController;
        passUtil = new PassUtil();
        base64Helper = new Base64Helper();
    }


    /**
     * Reads the stream until the first /r/n
     * @param stream The bufferedreader stream
     */
    private String readFromStream(BufferedReader stream) //Read string line from the stream
    {
        try {
            return stream.readLine(); //Read the stream until the first \r\n
        } catch (Exception ex) {
            System.err.println("readFromStream Error: " + ex);

            return null;
        }
    }

    /**
     * Reads from socket inputstream using inputstreamreader and then converts it to bufferedreader and
     * while socket is not closed it will keep reading lines using readFromStream method and the
     * lines will be in hex so it will convert hex to string and then split received text and displays
     * the message and username that it was sent from in the client view.
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
                if (message.startsWith("you-have-been-kicked")){
                    if (chatClientController != null) {
                        chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": You have been kicked from the server, however not banned so you can reconnect if you like." , "RED", null);
                    }
                }
                if (message.startsWith("chatmessages|end|")){
                    String[] parts = message.split("\\|end\\|");
                    Pattern pattern = Pattern.compile("\\|end\\|");
                    Matcher matcher = pattern.matcher(message);
                    int count = 0;
                    while (matcher.find()) count++;
                    System.out.println(message);
                    if (count > 0){
                        chatClientController.appendToPane("------------------------------------------------------------------------------------------------", "AQUA", null);
                        chatClientController.appendToPane("LAST " + count + " MESSAGES/IMAGES BELOW:", "GREEN", null);
                        chatClientController.appendToPane("------------------------------------------------------------------------------------------------", "AQUA", null);
                    }
                    for (int i = 1; i <= count; i++) {
                            if (parts[i] != null) {
                                String[] part2 = parts[i].split("\\|split\\|");
                                String sender = part2[0];
                                String msg = part2[1];
                                String datetime = part2[2];
                                String receiver = part2[3];
                                String img_base64 = part2[4];
                                if (msg != null) {
                                    if (!msg.equalsIgnoreCase("null")) {
                                        if (sender.equals(username) & receiver.toLowerCase().contains(username.toLowerCase())) {
                                            chatClientController.appendToPane(datetime + ": from You to: You (" + username + "): " + msg, Env.messageColor, null);
                                        } else if (receiver.toLowerCase().contains(username.toLowerCase())) {
                                            chatClientController.appendToPane(datetime + ": from: " + sender + " to: You (" + username + "): " + msg, Env.messageColor, null);
                                        } else if (sender.equals(username)) {
                                            chatClientController.appendToPane(datetime + ": You (" + username + "): To: " + receiver + ": " + msg, "BLUE", null);
                                        } else if (receiver.equals("All")) {
                                            chatClientController.appendToPane(datetime + ": " + sender + ": test" + msg, Env.messageColor, null);
                                        }
                                    } else if (img_base64 != null) {
                                        if (!img_base64.equalsIgnoreCase("null")) {
                                            BufferedImage image = base64Helper.decodeBase64StringToImage(img_base64);
                                            if (image != null) {
                                                File dir = new File(System.getProperty("user.dir") + "/chatimages/");
                                                int pos = Objects.requireNonNull(dir.list()).length;
                                                File f = new File(System.getProperty("user.dir") + "/chatimages/" + sender + "-" + pos + ".png");
                                                ImageIO.write(image, "png", f);
                                                if (sender.equals(username) & receiver.toLowerCase().contains(username.toLowerCase())) {
                                                    chatClientController.appendToPane(datetime + ": from You to: You (" + username + "): ", Env.messageColor, f.getAbsolutePath());
                                                } else if (receiver.toLowerCase().contains(username.toLowerCase())) {
                                                    chatClientController.appendToPane(datetime + ": from: " + sender + " to: You (" + username + "): ", Env.messageColor, f.getAbsolutePath());
                                                } else if (sender.equals(username)) {
                                                    chatClientController.appendToPane(datetime + ": You (" + username + "): To: " + receiver + ": ", "BLUE", f.getAbsolutePath());
                                                } else if (receiver.equals("All")) {
                                                    chatClientController.appendToPane(datetime + ": " + sender + ": hello", Env.messageColor, f.getAbsolutePath());
                                                }
                                            }
                                        }
                                    }
                                } else if (img_base64 != null){
                                    if (!img_base64.equalsIgnoreCase("null")){
                                    BufferedImage image = base64Helper.decodeBase64StringToImage(img_base64);
                                    if (image != null) {
                                        File dir = new File(System.getProperty("user.dir") + "/chatimages/");
                                        int pos = Objects.requireNonNull(dir.list()).length;
                                        File f = new File(System.getProperty("user.dir") + "/chatimages/" + sender + "-" + pos + ".png");
                                        ImageIO.write(image, "png", f);
                                        if (sender.equals(username) & receiver.toLowerCase().contains(username.toLowerCase())) {
                                            chatClientController.appendToPane(datetime + ": from You to: You (" + username + "): ", Env.messageColor, f.getAbsolutePath());
                                        } else if (receiver.toLowerCase().contains(username.toLowerCase())) {
                                            chatClientController.appendToPane(datetime + ": from: " + sender + " to: You (" + username + "): ", Env.messageColor, f.getAbsolutePath());
                                        } else if (sender.equals(username)) {
                                            chatClientController.appendToPane(datetime + ": You (" + username + "): To: " + receiver + ": ", "BLUE", f.getAbsolutePath());
                                        } else if (receiver.equals("All")) {
                                            chatClientController.appendToPane(datetime + ": " + sender + ": hello", Env.messageColor, f.getAbsolutePath());
                                        }
                                    }
                                    }
                                }
                        }
                    }
                    if (count > 0){
                        chatClientController.appendToPane("------------------------------------------------------------------------------------------------", "AQUA", null);
                    }
                }
                if (message.startsWith("msgspecific|split|")){
                    String[] data = message.split("\\|split\\|");
                    String from = data[2];
                    username = data[1];
                    String msg = data[3];
                    System.err.println("RECEIVED MSG FROM SPECIFIC: " + new Date(System.currentTimeMillis()) + ": from:  " + from + ": to: " + username + ": " + msg);
                    chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": from:" + from + ": to: You (" + username + "): " + msg, Env.messageColor, null);

                }
                if (message.startsWith("image|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String base64Img = data[2];
                    System.err.println("RECEIVED IMG-BASE64: " + new Date(System.currentTimeMillis()) + ": " + username + ": " + base64Img);
                    BufferedImage image = base64Helper.decodeBase64StringToImage(base64Img);
                    if (image != null){
                        //int random = 1 + (int) (Math.random() * 100000);
                        File dir = new File(System.getProperty("user.dir") + "/chatimages/");
                        int pos = Objects.requireNonNull(dir.list()).length;
                        File f = new File(System.getProperty("user.dir") + "/chatimages/" + username + "-" + pos + ".png");
                        ImageIO.write(image, "png", f);
                        chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": " + username + ": ", Env.messageColor, f.getAbsolutePath());
                    }

                }
                if (message.startsWith("msg|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String msg = data[2];
                    System.err.println("RECEIVED MSG: " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": " + username + ": " + msg, Env.messageColor, null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Processor completed ");
    }

}

