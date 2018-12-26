package com.ioter.warehouse.bean;

import java.io.Serializable;

public class EPC implements Serializable{
    String epc;

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }
}
