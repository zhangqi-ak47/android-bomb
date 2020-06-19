package com.goldze.mvvmhabit.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EntrustHistoryEntity {

    private int type;
    private String coinMarket;
    private int status;
    private long createTime;
    private String price;
    private String averagePrice;
    private String totalTurnover;
    private String turnover;
    private String volume;
    private long id;
    private String entrustNo;

    public String getAveragePrice() {
        return new BigDecimal(averagePrice).toPlainString();
    }

    public String getTotalTurnover() {
        return new BigDecimal(totalTurnover).toPlainString();
    }

    public String getTurnover() {
        return new BigDecimal(turnover).toPlainString();
    }

    public String getVolume() {
        return new BigDecimal(volume).toPlainString();
    }
}
