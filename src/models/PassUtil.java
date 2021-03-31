package models;


import config.Env;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;

public class PassUtil {
    DBManager dbManager;
    public PassUtil(){
        dbManager = new DBManager();
    }
    public boolean passwordProtect(AncestorListener ancestorListener, BufferedImage resizedLogo, DBManager dbManager){
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        JLabel jl = new JLabel(
                "<html>Enter <font color=red><b>Password</font> To Continue:"
                        + "</b><br><br></html>");
        Font font = new Font("Arial", Font.PLAIN, 14);
        JPasswordField txt = new JPasswordField();
        txt.setFocusable(true);
        txt.setEditable(true);
        txt.setToolTipText("Enter password to continue");
        txt.setSelectedTextColor(Color.RED);
        txt.setForeground(Color.BLUE);
        jl.setFont(font);
        txt.setFont(font);
        jp.add(jl, BorderLayout.NORTH);
        jp.add(txt);
        txt.addAncestorListener(ancestorListener);
        if (JOptionPane.showConfirmDialog(null, jp, Env.ChatServerMessageBoxTitle,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(resizedLogo))  == 0) {
            if (txt.getText() != null) {
                if (!txt.getText().trim().equals("")) {
                    ArrayList<String> col = new ArrayList<>();
                    ArrayList<String> val = new ArrayList<>();
                    col.add("password");
                    val.add(txt.getText().trim());
                    try {
                        ResultSet rs = dbManager.selectAllWhere("users", col, val);
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, "Invalid password. Access denied.", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        return true;
                    }catch (SQLException ex){
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Text cannot be null!", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error with input text!", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            System.out.println("Input Cancelled");
        }
        return false;
    }

    public boolean passwordProtect(AncestorListener ancestorListener, BufferedImage resizedLogo){
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        JLabel jl = new JLabel(
                "<html>Enter <font color=red><b>Password</font> To Continue:"
                        + "</b><br><br></html>");
        Font font = new Font("Arial", Font.PLAIN, 14);
        JPasswordField txt = new JPasswordField();
        txt.setFocusable(true);
        txt.setEditable(true);
        txt.setToolTipText("Enter password to continue");
        txt.setSelectedTextColor(Color.RED);
        txt.setForeground(Color.BLUE);
        jl.setFont(font);
        txt.setFont(font);
        jp.add(jl, BorderLayout.NORTH);
        jp.add(txt);
        txt.addAncestorListener(ancestorListener);
        if (JOptionPane.showConfirmDialog(null, jp, Env.ChatServerMessageBoxTitle,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(resizedLogo))  == 0) {
            if (txt.getText() != null) {
                if (!txt.getText().trim().equals("")) {
                    ArrayList<String> col = new ArrayList<>();
                    ArrayList<String> val = new ArrayList<>();
                    col.add("password");
                    val.add(txt.getText().trim());
                    try {
                        ResultSet rs = dbManager.selectAllWhere("users", col, val);
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, "Invalid password. Access denied.", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        return true;
                    }catch (SQLException ex){
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Text cannot be null!", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error with input text!", Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            System.out.println("Input Cancelled");
        }
        return false;
    }

    public byte[] getSHA256(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Incorrect algoritm: " + e);
        }
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public String toHexString(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public String toHexString(String str){
        StringBuffer sb = new StringBuffer();
        char ch[] = str.toCharArray();
        for(int i = 0; i < ch.length; i++) {
            String hexString = Integer.toHexString(ch[i]);
            sb.append(hexString);
        }
        return sb.toString();
    }
    public String hexToString(String hexString){
        try {
            char[] charArray = hexString.toCharArray();
            String result = new String();
            for (int i = 0; i < charArray.length; i = i + 2) {
                try {
                    String st = "" + charArray[i] + "" + charArray[i + 1];
                    char ch = (char) Integer.parseInt(st, 16);
                    result = result + ch;
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            return result;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;

    }
}
