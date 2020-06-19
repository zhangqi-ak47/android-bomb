package com.goldze.mvvmhabit.entity;

import lombok.Data;

@Data
public class QueryTransferEntity {


    /**
     * id : 202005122014485270
     * type : 2
     * coinId : 4
     * coinName : USDT
     * amount : 11
     * tradeStatus : 0
     * fromUserId : 3
     * fromAddress :
     * toUserId : 0
     * toAddress : 0x123456ewqefds
     * fee : 1.1
     * confirmTime : 2020-05-12T20:14:49
     * version : 0
     * remark :
     * status : 1
     * updateTime : 2020-05-18T16:01:13
     * createTime : 2020-05-12T20:14:49
     * isOwn : 0
     * relateRecdId :
     * txId :
     * userRemark :
     * sysRemark :
     * collectStatus : 0
     * chainName : omni
     * collectTxId :
     * uid : 1886002
     * firstAuditUserId : null
     * firstAuditUserName :
     * firstAuditTime : null
     * reviewAuditUserId : null
     * reviewAuditUserName :
     * reviewAuditTime : null
     * reviewRemark :
     * firstRemark :
     */

    private String id;
    private int type;
    private int coinId;
    private String coinName;
    private int amount;
    private int tradeStatus;
    private int fromUserId;
    private String fromAddress;
    private int toUserId;
    private String toAddress;
    private double fee;
    private String confirmTime;
    private int version;
    private String remark;
    private int status;
    private String updateTime;
    private String createTime;
    private int isOwn;
    private String relateRecdId;
    private String txId;
    private String userRemark;
    private String sysRemark;
    private int collectStatus;
    private String chainName;
    private String collectTxId;
    private int uid;
    private String firstAuditUserId;
    private String firstAuditUserName;
    private String firstAuditTime;
    private String reviewAuditUserId;
    private String reviewAuditUserName;
    private String reviewAuditTime;
    private String reviewRemark;
    private String firstRemark;

}
