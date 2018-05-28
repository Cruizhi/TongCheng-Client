package com.example.administrator.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/14.
 */

public class Recruit implements Serializable {

    private String recruittype;
    private String userid;
    private String token;
    private String type;
    private String name;
    private String number;
    private String duration;
    private String period;
    private String wages;
    private String payroll;
    private String address;
    private String welfare;
    private String content;
    private String linkman;
    private String linkphone;
    private String company;
    private String date;
    private String city;

    public Recruit() {
    }

    public Recruit(String recruittype, String userid, String token, String type, String name, String number, String duration, String period, String wages, String payroll, String address, String welfare, String content, String linkman, String linkphone, String company, String date, String city) {
        this.recruittype = recruittype;
        this.userid = userid;
        this.token = token;
        this.type = type;
        this.name = name;
        this.number = number;
        this.duration = duration;
        this.period = period;
        this.wages = wages;
        this.payroll = payroll;
        this.address = address;
        this.welfare = welfare;
        this.content = content;
        this.linkman = linkman;
        this.linkphone = linkphone;
        this.company = company;
        this.date = date;
        this.city = city;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public String getPayroll() {
        return payroll;
    }

    public void setPayroll(String payroll) {
        this.payroll = payroll;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkphone() {
        return linkphone;
    }

    public void setLinkphone(String linkphone) {
        this.linkphone = linkphone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
