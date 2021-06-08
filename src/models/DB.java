package models;


import config.Env;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

public class DB {
    static Connection connection;
    public DB(){

    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DB.connection = connection;
    }

    public boolean loadConfig(){
        Properties config = new Properties();
        boolean mysqlUserLoaded = false;
        boolean mysqlPassLoaded = false;
        boolean mysqlDbLoaded = false;
        boolean mysqlPortLoaded = false;
        try {
            config.load(new FileInputStream("config.cfg"));
            Enumeration<Object> en = config.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                /**
                 * MYSQL_USERNAME=
                 * MYSQL_PASSWORD=
                 * MYSQL_DATABASE_NAME=
                 * MYSQL_PORT=
                 */
                if (key.equals("MYSQL_USERNAME")) {
                    Env.user = (String) config.get(key);
                    if (!Env.user.trim().equals("")) {
                        mysqlUserLoaded = true;
                    }
                }
                if (key.equals("MYSQL_PASSWORD")) {
                    Env.pass = (String) config.get(key);
                    if (!Env.pass.trim().equals("")) {
                        mysqlPassLoaded = true;
                    }
                }
                if (key.equals("MYSQL_DATABASE_NAME")){
                    Env.dbName = (String) config.get(key);
                    if (!Env.dbName.trim().equals("")) {
                        mysqlDbLoaded = true;
                    }
                }
                if (key.equals("MYSQL_PORT")){
                    String mysqlport = (String)config.get(key);
                    Env.port = mysqlport;
                    if (!mysqlport.trim().equals("")) {
                        mysqlPortLoaded = true;
                    }
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        if (mysqlDbLoaded & mysqlPassLoaded & mysqlPortLoaded & mysqlUserLoaded){
            Env.conURL = "jdbc:mysql://localhost:" + Env.port + "/" + Env.dbName + "?characterEncoding=latin1";
            System.out.println("Set con url: " + Env.conURL);
            return true;
        }
        return false;
    }

    public boolean initDB(){
        if (!loadConfig()){
            JOptionPane.showMessageDialog(null, "Error loading config. Fill in mysql properties in config.cfg file.", Env.DBMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean success = true;

        String driverName = Env.driverName;
        String conURL = Env.conURL;
        String user = Env.user;
        String pass = Env.pass;

        try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            success = false;
            JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
        }

        try {   
            connection = DriverManager.getConnection(conURL, user, pass);
        } catch (SQLException e) {
            System.out.println("Maybe DB not exist, creating db, then trying to connect again...");
            try {
                String sqlURL = "jdbc:mysql://localhost:3306/?user=" + user + "&password=" + pass + "&characterEncoding=latin1";
                //använder detta om man har ett % eller annat regex tecken i sitt db lösenord
                connection = DriverManager.getConnection(sqlURL.replaceAll("%(?![0-9a-fA-F]{2})", "%25"));

                Statement s=connection.createStatement();
                int result =s.executeUpdate("CREATE DATABASE " + Env.dbName);
                connection.close();
                connection = DriverManager.getConnection(conURL, user, pass);
                String create_users_table ="CREATE TABLE users ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "USERNAME varchar(255) DEFAULT NULL,"
                        + "PASSWORD varchar(255) DEFAULT NULL,"
                        + "ADMIN varchar(255) DEFAULT NULL)";
                        //+ "FIRST_NAME varchar(255) DEFAULT NULL,"
                        //+ "LAST_NAME varchar(255) DEFAULT NULL,"
                        //+ "CREATED_AT DATE DEFAULT NULL,"
                        //+ "ACCOUNT_ID int DEFAULT NULL)";
                String create_connections_table="CREATE TABLE connections ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "IP_ADDRESS varchar(255) DEFAULT NULL,"
                        + "DATE_TIME DATE DEFAULT NULL)";
                String create_chat_messages_table="CREATE TABLE chatmessages ("
                        + "ID int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + "SENDER varchar(255) DEFAULT NULL,"
                        + "BODY varchar(8000) DEFAULT NULL,"
                        + "RECEIVER varchar(255) DEFAULT NULL,"
                        + "IMG_BASE64 text DEFAULT NULL,"
                        + "DATE_TIME varchar(255) DEFAULT NULL)";

                s = connection.createStatement();
                s.executeUpdate(create_chat_messages_table);
                s = connection.createStatement();
                s.executeUpdate(create_connections_table);
                s = connection.createStatement();
                s.executeUpdate(create_users_table);
            } catch (SQLException ex) {
                ex.printStackTrace();
                if (!ex.toString().contains("com.mysql.cj.jdbc.exceptions.CommunicationsException")) {
                    success = false;
                } else {
                    success = true;
                }
                JOptionPane.showMessageDialog(null, "InitDB SQL Error: " + e.toString());
            }
        }
        return success;
    }
}
