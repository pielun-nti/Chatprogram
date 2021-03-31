package models;


import config.Env;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBManagerTest {
    public static void main(String[] args){
        DBManager dbManager = new DBManager();
        String table = "logs";
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("author");
        columns.add("body");
        columns.add("editors");
        ArrayList<String> values = new ArrayList<>();
        values.add("testinsertauthor");
        values.add("testinsertbody");
        values.add("testinserteditors");
        if (dbManager.insert(table, columns, values)){
            JOptionPane.showMessageDialog(null, "Successfully inserted using db manager", "DBManagerTest", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Failed to insert using db manager", "DBManagerTest", JOptionPane.ERROR_MESSAGE);
        }
        if (dbManager.checkDuplicate(table, columns, values)){
            JOptionPane.showMessageDialog(null, "Duplicate exists", "DBManagerTest", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Duplicate does not exist", "DBManagerTest", JOptionPane.ERROR_MESSAGE);
        }
        ArrayList<String> filtervalues = new ArrayList<>();
        ArrayList<String> filtercolumns = new ArrayList<>();
        filtercolumns.add("author");
        filtervalues.add("Pierre");
        ArrayList<String> setcolumns = new ArrayList<>();
        ArrayList<String> setvalues = new ArrayList<>();
        setcolumns.add("author");
        setvalues.add("editedbydbmanager");
        if (dbManager.edit(table, columns, values, setcolumns, setvalues)){
            JOptionPane.showMessageDialog(null, "Successfully edited using db manager", "DBManagerTest", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Failed to edit using db manager", "DBManagerTest", JOptionPane.ERROR_MESSAGE);
        }
        try {
        ResultSet all = dbManager.selectAll(table);
        if (all != null) {
            while (all.next()) {
                System.out.println("Select all: " + all.getString(2));
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ArrayList<String> col = new ArrayList<>();
            col.add("author");
            ResultSet authors = dbManager.selectColumn(table, col);
            if (authors != null) {
                while (authors.next()) {
                    System.out.println("Select authors: " + authors.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dbManager.delete(table, setcolumns, setvalues)){
            JOptionPane.showMessageDialog(null, "Successfully deleted using db manaager", "DBManagerTest", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Failed to delete using db manager", "DBManagerTest", JOptionPane.ERROR_MESSAGE);

        }
        int deletedRows = dbManager.deleteAll(table);
        if (deletedRows > 0){
            JOptionPane.showMessageDialog(null, "Successfully deleted all using db manager", "DBManagerTest", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Failed to delete all using db manager", "DBManagerTest", JOptionPane.ERROR_MESSAGE);
        }
        ArrayList<String> c = new ArrayList<>();
        ArrayList<String> v = new ArrayList<>();
        c.add("id");
        v.add("1");
        ResultSet rs = dbManager.selectAllWhere("logs", columns, values);
        try {
            if (!rs.next()){
                JOptionPane.showMessageDialog(null, "No log with that ID was found", Env.DBMessageBoxTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
