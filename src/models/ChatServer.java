package models;

import config.Env;
import controllers.ChatServerController;

import javax.net.ssl.SSLServerSocketFactory;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

/**
 * The chatserver is a thread that listens for new client connections and then creates a new thread
 * for each client and also keeps count of the clients connected and add them to arraylist.
 */
public class ChatServer extends Thread {
    int serverPort;
    int maxClientsReached = 0;
    ChatServerController chatServerController;
    boolean running;

    /**
     * Chatserver constructor.
     * @param serverPort The server port
     * @param chatServerController The server controller
     */
    public ChatServer(int serverPort, ChatServerController chatServerController){
        this.serverPort = serverPort;
        this.chatServerController = chatServerController;
    }

    /**
     * The chat server threads run method will keep accepting clients in an infinite loop as long as
     * the max client limit is not reached. For each client it will create new thread and add the client
     * to an arraylist and increase the clients connected variable.
     */
    @Override
    public void run() {
        try {
            if (chatServerController.getModel().receiversConnected == null){
                chatServerController.getModel().receiversConnected = new ArrayList<>();
            }
            System.setProperty("javax.net.ssl.keyStore", Env.SSLKeyStore);
            System.setProperty("javax.net.ssl.keyStorePassword", Env.SSLKeyStorePass);
            chatServerController.getModel().ss = SSLServerSocketFactory.getDefault().createServerSocket(serverPort);
            System.out.println("Started main chat server on port " + serverPort);
            chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Started main server on port " + serverPort, "BLUE");
            for (int i = 0; i >= 0; i++) {
                running = true;
                if (Info.clientsConnected < Info.maxClientsConnected) {
                    Socket socket = chatServerController.getModel().ss.accept();
                    System.out.println("Accepted connection : " + socket);
                    chatServerController.appendToPane(new Date(System.currentTimeMillis()) + ": Accepted connection: " + socket, "GREEN");
                    chatServerController.getModel().chatMainReceiver = new ChatServerMainReceiver(Info.clientsConnected, socket, chatServerController);
                    chatServerController.getModel().chatMainReceiver.setPriority(10);
                    chatServerController.getModel().chatMainReceiver.start();
                    Info.clientsConnected++;
                    chatServerController.getModel().receiversConnected.add(chatServerController.getModel().chatMainReceiver);
                    maxClientsReached = 0;
                    System.err.println("Clients now connected: " + Info.clientsConnected);
                } else {
                    if (maxClientsReached < 1) {
                        System.err.println("Max clients reached: " + Info.clientsConnected + "/" + Info.maxClientsConnected);
                        maxClientsReached++;
                    }
                }
                }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ChatServer Exception: " + e.toString());
            running = false;
        }
    }
}
