package com.victor.openvpn3;

import java.io.Serializable;

public class Server implements Serializable {

    public int id;
    private String country;
    private String flagUrl;
    private String ovpn;
    private String ovpnUserName;
    private String ovpnUserPassword;

    private int latency;
    private  int port;


    public Server() {
    }

    public Server(String country, String flagUrl, String ovpn) {
        this.country = country;
        this.flagUrl = flagUrl;
        this.ovpn = ovpn;
    }

    public Server(String country, String flagUrl, String ovpn, String ovpnUserName, String ovpnUserPassword) {
        this.country = country;
        this.flagUrl = flagUrl;
        this.ovpn = ovpn;
        this.ovpnUserName = ovpnUserName;
        this.ovpnUserPassword = ovpnUserPassword;
    }


    public Server(int id , int latency, String country, String flagUrl, String ovpn, String ovpnUserName, String ovpnUserPassword, int port) {
        this.id = id;
        this.latency = latency;
        this.country = country;
        this.flagUrl = flagUrl;
        this.ovpn = ovpn;
        this.ovpnUserName = ovpnUserName;
        this.ovpnUserPassword = ovpnUserPassword;
        this.port = port;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getOvpn() {
        return ovpn;
    }

    public void setOvpn(String ovpn) {
        this.ovpn = ovpn;
    }

    public String getOvpnUserName() {
        return ovpnUserName;
    }

    public void setOvpnUserName(String ovpnUserName) {
        this.ovpnUserName = ovpnUserName;
    }

    public String getOvpnUserPassword() {
        return ovpnUserPassword;
    }

    public void setOvpnUserPassword(String ovpnUserPassword) {
        this.ovpnUserPassword = ovpnUserPassword;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", flagUrl='" + flagUrl + '\'' +
                ", ovpn='" + ovpn + '\'' +
                ", ovpnUserName='" + ovpnUserName + '\'' +
                ", ovpnUserPassword='" + ovpnUserPassword + '\'' +
                ", latency=" + latency +
                ", port=" + port +
                '}';
    }
}
