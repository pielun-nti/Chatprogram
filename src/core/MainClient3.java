package core;

import config.Env;
import controllers.LoginController;
import models.DB;
import models.LoginModel;
import views.LoginView;

import javax.swing.*;
import java.awt.*;

/**
 * Another client class identical to test that the server handles multi thread correct and can handle multiple clients
 * at the same time.
 */
public class MainClient3 {
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
