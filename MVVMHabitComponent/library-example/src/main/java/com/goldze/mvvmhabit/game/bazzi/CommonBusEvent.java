package com.goldze.mvvmhabit.game.bazzi;

import lombok.Data;

@Data
public class CommonBusEvent<T> {

    private String tag;
    private int type;
    private T data;

    public CommonBusEvent() {
    }

    public CommonBusEvent(String tag, int type, T data) {
        this.tag = tag;
        this.type = type;
        this.data = data;
    }
}
