package com.goldze.mvvmhabit.entity;

import java.util.Map;

import lombok.Data;

@Data
public class WsSbTpEntity<T> {

    private String id;
    private String cmd;
    private String topic;
    private T data;

    public WsSbTpEntity() {
    }

    public WsSbTpEntity(String id, String topic) {
        this.id = id;
        this.topic = topic;
        this.data= (T) new Object();
    }
}
