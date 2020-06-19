package com.goldze.mvvmhabit.entity;

import com.goldze.mvvmhabit.game.GM;

import lombok.Data;

@Data
public class UserEntrustListEntity {

    /**
     * id : 138824070186930176
     * entrustNo : 138824070186930177
     * type : 2
     * coinMarket : BTC/ALPT
     * amount : 10.0000000000000000
     * price : 100.00000000
     * dealAmount : 0
     * createTime : 1589249927
     */

    private long id;
    private String entrustNo;
    private int type;
    private String coinMarket;
    private String amount;
    private String price;
    private String dealAmount;
    private long createTime;

    public String toPrice(){
        try {
            return GM.DF_ZERO.format(Double.parseDouble(price))+"";
        }catch (Exception e){
            e.printStackTrace();
        }
        return price;
    }

    public String toAmount(){
        try {
            return GM.DF_ZERO.format(Double.parseDouble(amount))+"";
        }catch (Exception e){
            e.printStackTrace();
        }
        return amount;
    }

    public String toDealAmount(){
        try {
            return GM.DF_ZERO.format(Double.parseDouble(dealAmount))+"";
        }catch (Exception e){
            e.printStackTrace();
        }
        return dealAmount;
    }

}
