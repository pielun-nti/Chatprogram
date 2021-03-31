package models;

import config.Env;
import controllers.ChatClientController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

public class ChatClientMainReceiver extends Thread {
    ChatClientController chatClientController;
    Socket socket;
    PassUtil passUtil;
    String message;
    String clientIP;
    String username;
    String country;
    public ChatClientMainReceiver(Socket socket) {
        this.socket = socket;
        passUtil = new PassUtil();
    }

    public ChatClientMainReceiver(Socket socket, ChatClientController chatClientController) {
        this.socket = socket;
        this.chatClientController = chatClientController;
        passUtil = new PassUtil();
    }


    /**
     * Reads the stream until the first /r/n
     */
    private String readFromStream(BufferedReader stream) //Read string line from the stream
    {
        try {
            return stream.readLine(); //Read the stream until the first \r\n
        } catch (Exception ex) {
            System.err.println("readFromStream Error: " + ex);

            return null;
        }
    }


    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            while (!socket.isClosed()) {
                String hexMsg = readFromStream(br);
                if (hexMsg == null) {
                    continue;
                }
                message = passUtil.hexToString(hexMsg);
                if (message == null) {
                    continue;
                }
                clientIP = socket.getInetAddress().getHostAddress();
                if (message.startsWith("msg|split|")){
                    String[] data = message.split("\\|split\\|");
                    username = data[1];
                    String msg = data[2];
                    System.err.println("RECEIVED: " + new Date(System.currentTimeMillis()) + ": " + username + ": " + msg);
                    if (chatClientController != null) {
                        chatClientController.appendToPane(new Date(System.currentTimeMillis()) + ": " + username + ": " + msg, Env.messageColor);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Processor completed ");
    }

}

