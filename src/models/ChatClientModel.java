package models;

import config.Env;
import controllers.ChatClientController;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Date;

/**
 * ChatClientModel class is used to do the network stuff in etc. It doesnt interact with the client view
 * directly but can use client controller to interact with view if needed.
 */
public class ChatClientModel {
    SSLSocket socket;
    PassUtil passUtil;
    User user;
    ChatClientController chatClientController;
    ChatClientMainReceiver chatClientMainReceiver;
    Base64Helper base64helper;

    /**
     * Constructor
     * @param user The user
     */
    public ChatClientModel(User user) {
        passUtil = new PassUtil();
        this.user = user;
        base64helper = new Base64Helper();
        createFolders();
    }

    /**
     * Creates neccessary folders in the root directory.
     */
    private void createFolders() {
        File f = new File(System.getProperty("user.dir") + "/chatimages/");
        if (!f.exists()){
            f.mkdir();
        }
    }


    /**
     * Send message & username to all other clients and server (but this method only does send to the server
     * and then the server forwards the message to rest clients)
     * @param msg The message to send
     */
    public void sendMessageToAll(String msg) {
        try {
            if (socket == null){
                JOptionPane.showMessageDialog(null, "SSLSocket is not connected to server. Connect before sending message.",Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            writer.println(passUtil.toHexString("msg|split|" + user.getUsername() + "|split|" + msg));
            writer.flush();
            chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": You (" + user.getUsername() + "): " + msg , "BLUE", null);
        } catch (Exception ex){
            ex.printStackTrace();
            if (ex.toString().toLowerCase().contains("socket output is already shutdown")){
                disconnectFromServer(false);
            }
            JOptionPane.showMessageDialog(null, "Error sending message: " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends message to specific client. Ask for username to send to input and message input and then
     * sends to server a message that the server understands and forwards the specified message to the specified username.
     */
    public void sendMessageToSpecific() {
        try {
            if (socket == null){
                JOptionPane.showMessageDialog(null, "SSLSocket is not connected to server. Connect before sending message.",Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            String usernameSendTo = JOptionPane.showInputDialog(null, "Enter username to send message to");
            String msg = JOptionPane.showInputDialog(null, "Enter message to send to client with username: " + usernameSendTo);
            if (usernameSendTo.trim().equals(user.getUsername().trim())){
                JPanel jp = new JPanel();
                jp.setLayout(new BorderLayout());
                JLabel jl = new JLabel(
                        "<html><font color=red><b>Are you sure that you want to send a message to yourself? </font>:"
                                + "</b><br><br></html>");
                Font font = new Font("Arial", java.awt.Font.PLAIN, 14);
                jl.setFont(font);
                jp.add(jl, BorderLayout.NORTH);
                if (JOptionPane.showConfirmDialog(null, jp, Env.ChatClientMessageBoxTitle,
                        JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null)  == 0) {
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                    writer.println(passUtil.toHexString("msgspecific|split|" + usernameSendTo + "|split|" + user.getUsername() + "|split|" + msg));
                    writer.flush();
                    chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": You (" + user.getUsername() + "): To: " + usernameSendTo + ": " + msg, "BLUE", null);
                } else {
                    System.out.println("Input cancelled");
                }
            } else {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                writer.println(passUtil.toHexString("msgspecific|split|" + usernameSendTo + "|split|" + user.getUsername() + "|split|" + msg));
                writer.flush();
                chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": You (" + user.getUsername() + "): To: " + usernameSendTo + ": " + msg, "BLUE", null);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            if (ex.toString().toLowerCase().contains("socket output is already shutdown")){
                disconnectFromServer(false);
            }
            JOptionPane.showMessageDialog(null, "Error sending message: " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Connects to server.
     * @param ip Server ip
     * @param port Server port
     */
    public void connectToServer(String ip, int port){
        if (chatClientMainReceiver != null) {
            if (chatClientMainReceiver.socket != null) {
                if (!chatClientMainReceiver.socket.isClosed()) {
                    JOptionPane.showMessageDialog(null, "You are already connected to the server.", Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        System.setProperty("javax.net.ssl.keyStore", Env.SSLKeyStore);
        System.setProperty("javax.net.ssl.keyStorePassword", Env.SSLKeyStorePass);
        try {
            final char[] password = Env.SSLKeyStorePass.toCharArray();

            //final KeyStore keyStore = KeyStore.getInstance(new File(Env.SSLKeyStore), password);
            //final KeyStore keyStore = KeyStore.getInstance(Env.SSLKeyStore, String.valueOf(password));
            final KeyStore keyStore = KeyStore.getInstance("JKS");
            java.io.FileInputStream fis = null;
            try {
                fis = new java.io.FileInputStream(Env.SSLKeyStore);
                keyStore.load(fis, password);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
            keyManagerFactory.init(keyStore, password);

            final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
            context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final SSLSocketFactory factory = context.getSocketFactory();

            socket = ((SSLSocket) factory.createSocket(ip, port));
            chatClientMainReceiver = new ChatClientMainReceiver(socket, chatClientController);
            chatClientMainReceiver.start();
            chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": Connected to server " + ip + ":" + port , "GREEN", null);
            sendUsernameToServer();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server " + ip + ":" + port + ": " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * First tells server that this client will disconnect, then closes socket connection so this client is no longer connected to the server.
     */
    public void disconnectFromServer(boolean sendDisconnectToServer) {
        if (chatClientMainReceiver == null & sendDisconnectToServer){
            JOptionPane.showMessageDialog(null, "You are not connected to a server", Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (sendDisconnectToServer) {
            sendDisconnectToServer();
        }
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
        try {
            chatClientMainReceiver.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatClientMainReceiver.socket = null;
        try {
            chatClientMainReceiver.stop();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        chatClientMainReceiver = null;
        chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": Disconnected from server", "RED", null);
    }

    /**
     * Asks user to select image to send through filedialog and then sends the image to the server as bytes in another thread.
     */
    public void sendImageToAll(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String imagepath = fileChooser.getSelectedFile().getAbsolutePath();
            File image = new File(imagepath);
            long imgsize = image.length();
            try {
                BufferedImage img = ImageIO.read(image);
                String base64Img = null;
                if (imagepath.endsWith(".png")) {
                    base64Img = base64helper.encodeImageToBase64String(img, "png");
                } else {
                    base64Img = base64helper.encodeImageToBase64String(img, "jpg");
                }
                if (base64Img == null){
                    JOptionPane.showMessageDialog(null, "Error sending image to all because image didnt get loaded correctly.", Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    if (socket == null){
                        JOptionPane.showMessageDialog(null, "SSLSocket is not connected to server. Connect before sending image.",Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                    writer.println(passUtil.toHexString("image|split|" + user.getUsername() + "|split|" + base64Img));
                    writer.flush();
                    chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": You (" + user.getUsername() + "): " , "BLUE", imagepath);
                } catch (Exception ex){
                    ex.printStackTrace();
                    if (ex.toString().toLowerCase().contains("socket output is already shutdown")){
                        disconnectFromServer(false);
                    }
                    JOptionPane.showMessageDialog(null, "Error sending image to all: " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }



    /**
     * Sends disconnect message to server so the server knows that this client disconnected.
     */
    public void sendDisconnectToServer(){
        try {
            if (socket == null){
                JOptionPane.showMessageDialog(null, "SSLSocket is not connected to server. Connect before sending username.",Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            writer.println(passUtil.toHexString("disconnect-me"));
            writer.flush();
            System.out.println("Sent disconnect message to server");
        } catch (Exception ex){
            ex.printStackTrace();
            if (ex.toString().toLowerCase().contains("socket output is already shutdown")){
                disconnectFromServer(false);
            }
            JOptionPane.showMessageDialog(null, "Error sending disconnect message: " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends username to server so the server directly know which username each client has.
     */
    public void sendUsernameToServer() {
        try {
            if (socket == null){
                JOptionPane.showMessageDialog(null, "SSLSocket is not connected to server. Connect before sending username.",Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return;
            }
            chatClientMainReceiver.username = user.getUsername();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            writer.println(passUtil.toHexString("setusername|split|" + user.getUsername()));
            writer.flush();
            System.out.println("Sent username to server");
        } catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending username: " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    public SSLSocket getSocket() {
        return socket;
    }

    public void setSocket(SSLSocket socket) {
        this.socket = socket;
    }

    public PassUtil getPassUtil() {
        return passUtil;
    }

    public void setPassUtil(PassUtil passUtil) {
        this.passUtil = passUtil;
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
