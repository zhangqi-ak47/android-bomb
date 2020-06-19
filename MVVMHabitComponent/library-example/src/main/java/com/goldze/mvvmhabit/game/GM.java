package com.goldze.mvvmhabit.game;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.goldze.mvvmhabit.entity.UserInfoEntity;
import com.goldze.mvvmhabit.game.equip.GlideUtils;
import com.goldze.mvvmhabit.game.equip.StatusBarUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//项目管理员
public class GM {

    public static LogUtils Log;

    public static ToastUtils Toast;

    public static GlideUtils Glide;

    public static SPUtils Asp = SPUtils.getInstance(NPC.SP_APP_CATCH);

    public static SPUtils Usp = SPUtils.getInstance(NPC.SP_USER_CATCH);

    public static EncryptUtils Encrypt;

    public static FileUtils File;

    public static ImageUtils Image;

    public static KeyboardUtils Keyboard;

    public static NetworkUtils Network;

    public static ObjectUtils Objects;

    public static PermissionUtils Permission;

    public static PhoneUtils Phone;

    public static RegexUtils Regex;

    public static ScreenUtils Screen;

    public static SizeUtils Size;

    public static StringUtils Strings;

    public static ThreadUtils Threads;

    public static TimeUtils Time;

    public static FragmentUtils Fragments;

    public static StatusBarUtil StatusBar;

    public static UserInfoEntity UserInfo;

    public static void saveUserInfo(UserInfoEntity entity) {
        UserInfo = entity;
        Usp.put(NPC.USER_INFO, new Gson().toJson(entity));
    }

    public static void initHeader(String version) {
        try {
            String header = Asp.getString(NPC.MY_HEADER, "");
//            if (StringUtils.isEmpty(header)) {
//                MyHeader myHeader = new MyHeader();
//                myHeader.setVersion(version);
//                Asp.put(NPC.MY_HEADER, new Gson().toJson(myHeader));
//                BaseInterceptor.myHeader = myHeader;
//            } else {
//                MyHeader myHeader = new Gson().fromJson(header, MyHeader.class);
//                myHeader.setVersion(version);
//                Asp.put(NPC.MY_HEADER, new Gson().toJson(myHeader));
//                BaseInterceptor.myHeader = myHeader;
//            }
//            NPC.HEAD_WS_URL=StringUtils.isEmpty(BaseInterceptor.myHeader.getToken())?"f":BaseInterceptor.myHeader.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setToken(String token) {
//        BaseInterceptor.myHeader.setToken(token);
//        NPC.HEAD_WS_URL=StringUtils.isEmpty(token)?"f":token;
//        Asp.put(NPC.MY_HEADER, new Gson().toJson(BaseInterceptor.myHeader));
    }

    public static void logout() {
        UserInfo = null;
        Usp.clear();
        setToken("");
    }

    public static void login(String token) {
        GM.Usp.put(NPC.IS_HIDE_MONEY, false);
        setToken(token);
    }

    public static DecimalFormat DF_ZERO = new DecimalFormat("###################.###################");

    //    public static DecimalFormat DF_2 = new DecimalFormat("0.00");诡异的四舍五入
    public static List<String> testList = new ArrayList<String>() {
        {
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
            add("https://mochaintest.oss-cn-shenzhen.aliyuncs.com/aerfa/test/banner2.png");
        }};
}
