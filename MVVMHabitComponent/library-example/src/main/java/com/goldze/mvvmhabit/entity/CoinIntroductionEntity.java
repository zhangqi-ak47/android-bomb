package com.goldze.mvvmhabit.entity;

import lombok.Data;

@Data
public class CoinIntroductionEntity {


    /**
     * coinId : 1
     * coinName : USDT
     * chineseDesc : USDT
     * englishDesc : USDT
     * issuePrice : 0
     * totalIssuance : 0
     * totalCirculation : 0
     * whitePaper : erewqrewq
     * officialWebsite : www.dsadsad.com
     * coinIntroduction : dhfjskahfjkdsahfjbnmfbdsafdsaf
     * coinIntroductionEnglish :
     * blockchainBrowser :
     * publishTime : 2020-05-09 12:16:46
     */

    private int coinId;
    private String coinName;
    private String chineseDesc;
    private String englishDesc;
    private int issuePrice;
    private int totalIssuance;
    private int totalCirculation;
    private String whitePaper;
    private String officialWebsite;
    private String coinIntroduction;
    private String coinIntroductionEnglish;
    private String blockchainBrowser;
    private String publishTime;

}
