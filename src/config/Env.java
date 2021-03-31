package config;

public class Env {
    public static String dbName = "chatprogram";
    public static String driverName ="com.mysql.cj.jdbc.Driver";
    public static String conURL = "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=latin1";
    public static String user = "root";
    public static String pass = "Cde4io4hy";
    public static String SSLKeyStore = "sslkeystore";
    public static String SSLKeyStorePass = "123456";
    public static String ChatServerMessageBoxTitle = "ChatServer GUI";
    public static String ChatClientMessageBoxTitle = "ChatClient GUI";
    public static String ConfigMessageBoxTitle = "Config GUI";
    public static String LoginMessageBoxTitle = "Login GUI";
    public static String RegisterMessageBoxTitle = "Register GUI";
    public static String DBMessageBoxTitle = "DBManager GUI";
    public static String messageColor = "BLUE";
}
