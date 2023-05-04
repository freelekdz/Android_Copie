package com.freelek.apps.freelekdz.classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Session extends RealmObject {


    @PrimaryKey
    private int sessionId;
    private User user;
    private String token;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

}
