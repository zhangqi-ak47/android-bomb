package com.goldze.mvvmhabit.entity;

import com.goldze.mvvmhabit.game.equip.ArithUtils;
import com.google.android.material.internal.DescendantOffsetUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;
import me.goldze.mvvmhabit.utils.StringUtils;

@Data
public class TickersOnTopEntity {


    /**
     * lastPrice : 1413.417
     * lastPriceCny : null
     * rangeAbility : 0.0546
     * rangeAbilityAmount : 73.237
     * turnover : 1.1047393169494592E8
     * coinMarket : ETH/CNHT
     * amount : 88139.319548
     * highest : 1499.977
     * lowest : 1300.016
     * open : 1340.18
     * close : 1413.417
     * change : true
     */

    private double lastPrice;
    private double lastPriceCny;
    private double rangeAbility;
    private int amountPrecision;
    private int pricePrecision;
    private double rangeAbilityAmount;
    private double turnover;
    private String coinMarket;
    private double amount;
    private double highest;
    private double lowest;
    private double open;
    private double close;
    private boolean change;

    public double getRangeAbility(){
        return rangeAbility*100;
    }

    public String toCoinMarket(boolean bl) {
        return toCmUnit(bl, coinMarket);
    }

    public static String toCmUnit(boolean bl, String coinMarket) {
        if (!StringUtils.isEmpty(coinMarket)) {
            String[] str = coinMarket.split("/");
            if (str != null) {
                if (bl) {
                    if (str.length >= 1) {
                        return str[0];
                    }
                } else {
                    if (str.length >= 2) {
                        return "/" + str[1];
                    }
                }
            }
        }
        return "";
    }

    public String toDF2(double db) {
        String s=String.format("%.2f", db);
//        if (!StringUtils.isEmpty(s) && s.contains(".")){
//            if ("0.00".equals(s)) return s;
//            if (s.endsWith("0")){
//                s=s.substring(0, s.length()-1);
//            }
//            if (s.endsWith("0")){
//                s=s.substring(0, s.length()-2);
//            }
//        }

        return s;
    }

    public String toDFSL(double db) {
        String s=String.format("%."+amountPrecision+"f", db);
        return s;
    }

    public String toDFJG(double db) {
        String s=String.format("%."+pricePrecision+"f", db);
        return s;
    }

    public String toPercentage(double db) {
        String s = toDF2(db) + "%";
        return db > 0 ? "+" + s : s;
    }

    public String toCNY(double db) {
        return "≈" + toDFJG(db) + "CNY";
    }

    public String toCNY2(double db) {
        return "¥" + toDFJG(db);
    }
    public String toCNY3(double db) {
        return "≈￥" + toDFJG(db);
    }

    public String toCNY4(double db) {
        return "≈¥ " + toDFJG(db);
    }

    public String toGmv(String a, String b, String unit){
        try {
            if (!StringUtils.isEmpty(a) && !StringUtils.isEmpty(b)){
                double d=ArithUtils.mul(Double.parseDouble(a), Double.parseDouble(b));
                return d+" "+unit;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "-- "+unit;
    }

    public String toRatio(String db){
        try {
            int p=toPrecision(db);
            double d=Double.parseDouble(db);
//            return "≈¥ " + ((lastPriceCny/lastPrice)*d);
            BigDecimal bd = new BigDecimal((lastPriceCny/lastPrice)*d);
            bd = bd.setScale(p, RoundingMode.HALF_UP);
            return "≈¥ " + bd.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "≈¥ --";
    }

    public static int toPrecision(String s){
        try {
            String s1[]=s.split("\\.");
            return s1[1].length();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public double toCalc(int a){
        int b=1;
        for (int i=0; i<a; i++){
            b*=10;
        }
        return ArithUtils.div(1, b, a);

//        try {
//            if (a<=0){
//                return 1;
//            }else {
//                StringBuffer sb=new StringBuffer();
//                sb.append("0.");
//                for (int i=0; i<a-1; i++){
//                    sb.append("0");
//                }
//                sb.append("1");
//                return Double.parseDouble(sb.toString());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return 0;
    }

    public String toTurnover(double db) {
        String str;
        if (turnover>=1000){
            double dbl=db/1000.0;
            str=toDFSL(dbl)+"K";
        }else if (turnover>=10000){
            double dbl=db/10000.0;
            str=toDFSL(dbl)+"W";
        }else if (turnover>=1000000){
            double dbl=db/100000.0;
            str=toDFSL(dbl)+"M";
        }else {
            str=toDFSL(turnover);
        }

        return "24H量"+str;
    }
}
