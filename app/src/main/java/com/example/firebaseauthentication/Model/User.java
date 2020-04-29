package com.example.firebaseauthentication.Model;
import java.util.HashMap;
public class User {
    public String UserId;
    public String UserName;
    public String Email;
    public String Password;
    private HashMap<String, String> dataMap = new HashMap<String, String>();
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String UserId,String UserName, String Email,String Password) {
        this.UserId = UserId;
        this.UserName = UserName;
        this.Email = Email;
        this.Password=Password;
    }
    public HashMap<String, String> fireebaseMap(){
        return dataMap;
    }
}
