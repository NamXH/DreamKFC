package gamecore;

import java.io.Serializable;

public class User implements Serializable {
    private String userName = "";
    private String password = "";
    private int level = 0;

    public User(){
    }

    public void initUser(String name, String pass, int currentLevel){
        this.userName = name;
        this.level = currentLevel;
        this.password = pass;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int aLevel) {
        this.level = aLevel;
    }
}
