package core;

import config.Env;
import controllers.ChatServerController;
import controllers.LoginController;
import models.ChatServerModel;
import models.DB;
import models.LoginModel;
import views.ChatServerView;
import views.LoginView;

import javax.swing.*;
import java.awt.*;

/**
 * Class to start the client java application.
 */
public class MainClient {
    /**
     * Starts the client java application, first connecting to database and showing login gui.
     * @param args
     */
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
                LoginView loginView = new LoginView();
                LoginModel loginModel = new LoginModel();
                LoginController loginController = new LoginController(loginView, loginModel);
                loginView.getFrame().setVisible(true);
                loginView.getTxtUsername().requestFocus();
            }
        });
    }
}
