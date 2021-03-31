package models;


import config.Env;
import controllers.ChatClientController;
import views.ChatClientView;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterModel {

    DBManager dbManager;
    User user;
    PassUtil passUtil;
    public RegisterModel(){
        dbManager = new DBManager();
        passUtil = new PassUtil();
    }

    public boolean Register(String username, String password){
        if (username == null || password == null){
            JOptionPane.showMessageDialog(null, "Username or password cannot be null.", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.trim().equals("") || password.trim().equals("")){
            JOptionPane.showMessageDialog(null, "Username and password must have an value.", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            ArrayList<String> col = new ArrayList<>();
            ArrayList<String> val = new ArrayList<>();
            col.add("username");
            val.add(username);
            ResultSet rs2 = dbManager.selectAllWhere("users", col, val);
            if (rs2.next()){
                JOptionPane.showMessageDialog(null, "That username is already taken. Try another one..", Env.RegisterMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> col = new ArrayList<>();
        ArrayList<String> val = new ArrayList<>();
        col.add("username");
        col.add("password");
        val.add(username);
        val.add(passUtil.toHexString(passUtil.getSHA256(password)));
            dbManager.insert("users", col, val);
            user = new User(username, false);
        ChatClientView ChatView = new ChatClientView(user);
        ChatClientModel ChatModel = new ChatClientModel(user);
        ChatClientController ChatController = new ChatClientController(ChatModel,ChatView, user);
        ChatView.setVisible(true);
            return true;
    }
}
