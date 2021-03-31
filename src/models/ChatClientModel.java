package models;

import config.Env;
import controllers.ChatClientController;

import javax.net.ssl.*;
import javax.swing.*;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
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

    /**
     * Constructor
     * @param user The user
     */
    public ChatClientModel(User user) {
        passUtil = new PassUtil();
        this.user = user;

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
            chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": You (" + user.getUsername() + "): " + msg , "BLUE");
        } catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending message: " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Connects to server.
     * @param ip Server ip
     * @param port Server port
     */
    public void connectToServer(String ip, int port){
        System.setProperty("javax.net.ssl.keyStore", Env.SSLKeyStore);
        System.setProperty("javax.net.ssl.keyStorePassword", Env.SSLKeyStorePass);
        try {
            final char[] password = Env.SSLKeyStorePass.toCharArray();

            final KeyStore keyStore = KeyStore.getInstance(new File(Env.SSLKeyStore), password);

            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
            keyManagerFactory.init(keyStore, password);

            final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
            context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final SSLSocketFactory factory = context.getSocketFactory();

            socket = ((SSLSocket) factory.createSocket(ip, port));

            ChatClientMainReceiver chatClientMainReceiver = new ChatClientMainReceiver(socket, chatClientController);
            chatClientMainReceiver.start();
            chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": Connected to server " + ip + ":" + port , "GREEN");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server " + ip + ":" + port + ": " + ex.toString(),Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
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
