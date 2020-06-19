package com.goldze.mvvmhabit.entity;

import lombok.Data;

@Data
public class UserInfoEntity {
    private String userId;//	number
//    非必须
//            用户ID
//    mock: 1

    private String phone;//	string
//    非必须
//            手机号
//    mock: 123456789456

    private String phoneAreaCode;//	string
//    非必须
//            区号
//    mock: +86

    private String email;//	string
//    非必须
//            邮箱
//    mock: 123456789@qq.com

    private String inviteCode;//	string
//    非必须
//            邀请码
//    mock: M168700

    private String iconUrl;//	string
//    非必须
//            头像URL
//    mock: www.baidu.com

    private String realName;//	string
//    非必须
//            真实姓名
//    mock: 阿尔法

    private String token;//	null
//    非必须
//            token
//    mock: 1

    private String userTradeStatus;//	number
//    非必须
//            用户交易状态
//    mock: 1

    private String userLoginStatus;//	number
//    非必须
//            用户登录状态
//    mock: 1

    private String uid;//	string
//    非必须
//            Uid
//    mock: 1886001

    private String verifiedAudit;//	number



}
