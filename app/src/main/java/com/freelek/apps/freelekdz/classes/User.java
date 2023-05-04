package com.freelek.apps.freelekdz.classes;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject {


    public static String MANGER = "manager";
    public static String ADMIN = "admin";

    public static String TYPE_LOGGED = "LOGGED";
    public static String TYPE_POTER = "POTER";
    @PrimaryKey
    private int id;
    private String name;


    //private String senderid;
    private String username;
    private String password;
    private String job;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String token;
    private String auth;
    private Images images;
    private String status;
    private boolean followed = false;
    private Double distance;
    private String tokenGCM = "";
    private String type = TYPE_POTER;
    private boolean online;

    @Ignore
    private boolean withHeader = false;
    private String aboutJson;
    private boolean blocked;
    private int phone_verified;



    public boolean isWithHeader() {
        return withHeader;
    }

    public void setWithHeader(boolean withHeader) {
        this.withHeader = withHeader;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    /*public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String telephone) {
        this.phone = telephone;
    }

    public int getPhone_verified() {
        return phone_verified;
    }

    public void setPhone_verified(int phone_verified) {
        this.phone_verified = phone_verified;
    }


    public static class DefaultValues {
    }

    public static class Tags {

    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {

        try {
            JSONObject tokenObj =  new JSONObject(token);
            token = tokenObj.getString("id");
        } catch (JSONException ex) {
            this.token = token;
        }


    }


}
