package core;

import config.Env;
import controllers.ChatServerController;
import models.ChatServerModel;
import models.ChatServer;
import models.DB;
import views.ChatServerView;

import javax.swing.*;
import java.awt.*;

public class MainServer {
    static ChatServer chatServer;
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }


                DB db = new DB();
                if (!db.initDB()) {
                    JOptionPane.showMessageDialog(null, "Error connecting to database.", Env.DBMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                ChatServerView ChatView = new ChatServerView();
                ChatServerModel ChatModel = new ChatServerModel();
                ChatServerController ChatController = new ChatServerController(ChatModel,ChatView);
                ChatView.setVisible(true);
            }
        });

    }
}
