package com.goldze.mvvmhabit.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;

@Data
public class QueryInfoEntity {


    /**
     * sumAmount : 150
     * type : 1
     * coinAccountList : [{"coinId":1,"coinName":"USDT","amount":100,"frozenAmount":10,"convertCnyAmount":100,"position":1,"tranOutFee":null},{"coinId":2,"coinName":"BTC","amount":50,"frozenAmount":30,"convertCnyAmount":50,"position":2,"tranOutFee":null}]
     */

    private int sumAmount;
    private int type;
    private List<CoinAccountListBean> coinAccountList;

    @Data
    public static class CoinAccountListBean implements Parcelable {
        /**
         * coinId : 1
         * coinName : USDT
         * amount : 100
         * frozenAmount : 10
         * convertCnyAmount : 100
         * position : 1
         * tranOutFee : null
         */

        private int coinId;
        private String coinName;
        private int amount;
        private int frozenAmount;
        private int convertCnyAmount;
        private int position;
        private double tranOutFee;

        public String toConvertCnyAmount(){
            return "¥"+convertCnyAmount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.coinId);
            dest.writeString(this.coinName);
            dest.writeInt(this.amount);
            dest.writeInt(this.frozenAmount);
            dest.writeInt(this.convertCnyAmount);
            dest.writeInt(this.position);
            dest.writeDouble(this.tranOutFee);
        }

        protected CoinAccountListBean(Parcel in) {
            this.coinId = in.readInt();
            this.coinName = in.readString();
            this.amount = in.readInt();
            this.frozenAmount = in.readInt();
            this.convertCnyAmount = in.readInt();
            this.position = in.readInt();
            this.tranOutFee = in.readDouble();
        }

        public static final Parcelable.Creator<CoinAccountListBean> CREATOR = new Parcelable.Creator<CoinAccountListBean>() {
            @Override
            public CoinAccountListBean createFromParcel(Parcel source) {
                return new CoinAccountListBean(source);
            }

            @Override
            public CoinAccountListBean[] newArray(int size) {
                return new CoinAccountListBean[size];
            }
        };
    }
}
