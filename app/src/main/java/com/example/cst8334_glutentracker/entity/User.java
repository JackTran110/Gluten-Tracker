package com.example.cst8334_glutentracker.entity;

public class User {

    private long userID;
    private String userName;
    private String loginName;
    private String email;
    private String password;

    public User(long userID, String userName, String loginName, String email, String password){
        setUserID(userID)
                .setUserName(userName)
                .setLoginName(loginName)
                .setEmail(email)
                .setPassword(password);
    }

    public long getUserID(){
        return userID;
    }

    public User setUserID(long userID){
        this.userID = userID;
        return this;
    }

    public String getUserName(){
        return userName;
    }

    public User setUserName(String userName){
        this.userName = userName;
        return this;
    }

    public String getLoginName(){
        return loginName;
    }

    public User setLoginName(String loginName){
        this.loginName = loginName;
        return this;
    }

    public String getEmail(){
        return this.email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}
