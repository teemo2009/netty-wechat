package com.chat.entity;

import lombok.Data;

@Data
public class IOBean {
    private byte head;
    private int length;
    private short version;
    private String data;

    public IOBean(int length, short version, String data) {
        //固定头
        this.head=0x20;
        this.length = length;
        this.version = version;
        this.data = data;
    }

}
