package com.goldze.mvvmhabit.entity;

import lombok.Data;

@Data
public class MarketTradeEntity {


    /**
     * coinMarket : ETH/CNHT
     * time : 1589016620
     * direction : 2
     * price : 1398.263
     * amount : 3.794377
     */

    private String coinMarket;
    private long time;
    private int direction;
    private String price;
    private String amount;

}
