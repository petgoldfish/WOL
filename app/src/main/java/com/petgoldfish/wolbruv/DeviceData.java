package com.petgoldfish.wolbruv;

import com.orm.SugarRecord;

public class DeviceData extends SugarRecord {

    String MAC;
    String IP;
    String alias;

    public DeviceData() {

    }

    public DeviceData(String MAC, String IP, String alias) {
        this.MAC = MAC;
        this.IP = IP;
        this.alias = alias;
    }

}
