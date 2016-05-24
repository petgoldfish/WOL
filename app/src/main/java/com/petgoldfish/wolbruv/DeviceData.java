package com.petgoldfish.wolbruv;

public class DeviceData  {

    String MAC;
    String IP;
    String alias;
    int id;

    public DeviceData(String MAC, String IP, String alias) {
        this.MAC = MAC;
        this.IP = IP;
        this.alias = alias;
    }

    public DeviceData(int id,String MAC, String IP, String alias) {
        this.id = id;
        this.MAC = MAC;
        this.IP = IP;
        this.alias = alias;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public DeviceData() {

    }


    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }
}
