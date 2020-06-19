package com.goldze.mvvmhabit.entity;

import java.util.List;

import lombok.Data;

@Data
public class DepthEntity {


    /**
     * coinMarket : ETH/CNHT
     * asks : [{"price":"0","amount":"0"}]
     * bids : [{"price":"0","amount":"0"}]
     * type : detail
     */

    private String coinMarket;
    private String type;
    private List<AsksBean> asks;
    private List<AsksBean> bids;

    @Data
    public static class AsksBean {
        /**
         * price : 0
         * amount : 0
         */

        private String price;
        private String amount;

        public double toPrice(){
            try {
                return Double.parseDouble(price);
            }catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }
    }
}
