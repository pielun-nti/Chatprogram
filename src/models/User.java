package models;

public class User {
    String username;
    boolean admin;
    public User(){
        this.username = "Unknown";
        this.admin = false;
    }
    public User(String username, boolean admin){
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
