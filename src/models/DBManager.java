package models;


import config.Env;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager {
    DB db;
    Connection connection;
    
    public DBManager(){
       /* db = new DB();
        if (!db.initDB()){
            JOptionPane.showMessageDialog(null, "Init DB Error!", Env.DBMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
            System.exit(2);
        }*/
        if (DB.getConnection() != null){
            connection = DB.getConnection();
        } else {
            
        }
    }

    public DBManager(DB db){
        this.db = db;
        if (DB.getConnection() == null) {
            if (!db.initDB()) {
                JOptionPane.showMessageDialog(null, "Init DB Error!", Env.DBMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
                System.exit(2);
            }
        } else {
            connection = DB.getConnection();
        }
    }

    public boolean executeUpdate(DB db, String query){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean executeUpdate(String query){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet executeQuery(DB db, String query){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet executeQuery(String query){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public DB getDb() {
        return db;
    }

    public void setDB(DB db){
        this.db = db;
    }

    public int deleteAll(DB db, String table){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "DELETE from " + table;
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            int deletedRows = stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return deletedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteAll(String table){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "DELETE from " + table;
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            int deletedRows = stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return deletedRows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean delete(DB db, String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues){
        Statement stmt = null;
        try {
            stmt = (Statement) connection.createStatement();
            String query = "delete from " + table + " where ";

            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if (i == (filtercolumns.size() - 1)){
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues){
        Statement stmt = null;
        try {
            stmt = (Statement) connection.createStatement();
            String query = "delete from " + table + " where ";

            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if (i == (filtercolumns.size() - 1)){
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet selectAll(DB db, String table){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "select * from " + table;
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectAll(String table){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "select * from " + table;
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectAllWhere(DB db, String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "select * from " + table + " where ";
            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if ((filtercolumns.size() - 1) == i) {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else{
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectAllWhere(String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "select * from " + table + " where ";
            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if ((filtercolumns.size() - 1) == i) {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else{
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectColumn(DB db, String table, ArrayList<String> columns){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "select ";
            for (int i = 0; i < columns.size(); i++){
                if (columns.get(i) != null){
                    if (i == 0) {
                        if (i == (columns.size() - 1)){
                            query += columns.get(i) + " from " + table;
                        }else {
                            query += columns.get(i);
                        }
                    }else if (i < (columns.size() - 1)){
                        query += ", " + columns.get(i);
                    } else if (i == (columns.size() - 1)){
                        query += ", " + columns.get(i) + " from " + table;
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectColumn(String table, ArrayList<String> columns){
        Statement stmt= null;
        try {
            stmt = connection.createStatement();
            String query = "select ";
            for (int i = 0; i < columns.size(); i++){
                if (columns.get(i) != null){
                    if (i == 0) {
                        if (i == (columns.size() - 1)){
                            query += columns.get(i) + " from " + table;
                        }else {
                            query += columns.get(i);
                        }
                    }else if (i < (columns.size() - 1)){
                        query += ", " + columns.get(i);
                    } else if (i == (columns.size() - 1)){
                        query += ", " + columns.get(i) + " from " + table;
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean edit(DB db, String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues, ArrayList<String> setcolumns, ArrayList<String> setvalues){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "update " + table + " set ";

            for (int i = 0; i < setcolumns.size(); i++){
                if (setcolumns.get(i) != null){
                    if (i == 0){
                        if ((setcolumns.size() - 1) == i) {
                            query += setcolumns.get(i) + " = '" + setvalues.get(i) + "'";
                        }else{
                            query += setcolumns.get(i) + " = '" + setvalues.get(i);
                        }
                    }else if (i < (setcolumns.size() - 1)){
                        query += "' and " + setcolumns.get(i) + " = '" + setvalues.get(i);
                    }else if (i == (setcolumns.size() - 1)){
                        query += "' and " + setcolumns.get(i) + " = '" + setvalues.get(i) + "'";
                    }
                }
            }
            query += " where ";
            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if ((filtercolumns.size() - 1) == i) {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else{
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean edit(String table, ArrayList<String> filtercolumns, ArrayList<String> filtervalues, ArrayList<String> setcolumns, ArrayList<String> setvalues){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "update " + table + " set ";

            for (int i = 0; i < setcolumns.size(); i++){
                if (setcolumns.get(i) != null){
                    if (i == 0){
                        if ((setcolumns.size() - 1) == i) {
                            query += setcolumns.get(i) + " = '" + setvalues.get(i) + "'";
                        }else{
                            query += setcolumns.get(i) + " = '" + setvalues.get(i);
                        }
                    }else if (i < (setcolumns.size() - 1)){
                        query += "' and " + setcolumns.get(i) + " = '" + setvalues.get(i);
                    }else if (i == (setcolumns.size() - 1)){
                        query += "' and " + setcolumns.get(i) + " = '" + setvalues.get(i) + "'";
                    }
                }
            }
            query += " where ";
            for (int i = 0; i < filtercolumns.size(); i++){
                if (filtercolumns.get(i) != null){
                    if (i == 0){
                        if ((filtercolumns.size() - 1) == i) {
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                        }else{
                            query += filtercolumns.get(i) + " = '" + filtervalues.get(i);
                        }
                    }else if (i < (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i);
                    }else if (i == (filtercolumns.size() - 1)){
                        query += "' and " + filtercolumns.get(i) + " = '" + filtervalues.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkDuplicate(DB db, String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT * from " + table + " where ";

            for (int i = 0; i < columns.size(); i++){
                if (columns.get(i) != null){
                    if (i == 0){
                        query += columns.get(i) + " = '" + values.get(i);
                    }else if (i < (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i);
                    }else if (i == (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            if (rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkDuplicate(String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT * from " + table + " where ";

            for (int i = 0; i < columns.size(); i++){
                if (columns.get(i) != null){
                    if (i == 0){
                        query += columns.get(i) + " = '" + values.get(i);
                    }else if (i < (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i);
                    }else if (i == (columns.size() - 1)){
                        query += "' and " + columns.get(i) + " = '" + values.get(i) + "'";
                    }
                }
            }
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            ResultSet rs=stmt.executeQuery(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            if (rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean insert(DB db, String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "INSERT INTO " + table + " (";
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null) {
                    if (i == (columns.size() - 1)){
                        query += columns.get(i);
                    }else {
                        query += columns.get(i) + ", ";
                    }
                }
            }
            query += ")VALUES (";
            for (int i = 0; i < values.size(); i++){
                if (i == (values.size() - 1)){
                    query += "'" + values.get(i) + "'";
                }else {
                    query += "'" + values.get(i) + "', ";
                }
            }
            query += ")";
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    public boolean insert(String table, ArrayList<String> columns, ArrayList<String> values){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "INSERT INTO " + table + " (";
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) != null) {
                    if (i == (columns.size() - 1)){
                        query += columns.get(i);
                    }else {
                        query += columns.get(i) + ", ";
                    }
                }
            }
            query += ")VALUES (";
            for (int i = 0; i < values.size(); i++){
                if (i == (values.size() - 1)){
                    query += "'" + values.get(i) + "'";
                }else {
                    query += "'" + values.get(i) + "', ";
                }
            }
            query += ")";
            if (query.contains("password")){
                System.out.println("Executing query: " + "Password protected");
            }else {
                System.out.println("Executing query: " + query);
            }
            stmt.executeUpdate(query);
            if (query.contains("password")) {
                System.out.println("Successfully Executed Query: " + "Password protected");
            } else {
                System.out.println("Successfully Executed Query: " + query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
