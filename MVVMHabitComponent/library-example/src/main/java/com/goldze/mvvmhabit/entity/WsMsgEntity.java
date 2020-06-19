package com.goldze.mvvmhabit.entity;

import lombok.Data;

@Data
public class WsMsgEntity {

    private String cmd;
    private Object data;
    private int pushMessageType;
    private String time;
    private String topic;

}
