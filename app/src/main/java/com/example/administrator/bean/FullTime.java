package com.example.administrator.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/3.
 */

public class FullTime implements Serializable{

    private String recruittype = "fulltime";
    private String userid;
    private String token;
    private String name;
    private String wages;
    private String address;
    private String content;
    private String welfare;
    private String linkphone;
    private String city;
    private String date;

    public String getRecruittype() {
        return recruittype;
    }

    public void setRecruittype(String recruittype) {
        this.recruittype = recruittype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getLinkphone() {
        return linkphone;
    }

    public void setLinkphone(String linkphone) {
        this.linkphone = linkphone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
