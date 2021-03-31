package models;

import config.Env;
import controllers.ChatServerController;

import javax.net.ssl.SSLServerSocketFactory;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ChatServer extends Thread {
    int serverPort;
    int maxClientsReached = 0;
    ChatServerController chatServerController;
    boolean running;
    public ChatServer(int serverPort, ChatServerController chatServerController){
        this.serverPort = serverPort;
        this.chatServerController = chatServerController;
    }

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
