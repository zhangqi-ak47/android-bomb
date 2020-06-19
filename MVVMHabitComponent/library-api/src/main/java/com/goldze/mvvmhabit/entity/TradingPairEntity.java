package com.goldze.mvvmhabit.entity;

import lombok.Data;

@Data
public class TradingPairEntity {


    /**
     * tradingPair : BTC/USDT
     * baseCoinAmount : 2,990.0000
     * marketCoinAmount : 64,216.0000
     */

    private String tradingPair;
    private String baseCoinAmount;
    private String marketCoinAmount;

}
