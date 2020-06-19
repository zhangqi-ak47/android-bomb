package com.goldze.mvvmhabit.app;

import android.content.Context;
import android.view.Gravity;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.goldze.mvvmhabit.BuildConfig;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.boss.BossActivity;
import com.goldze.mvvmhabit.game.store.WebSocketHelper;
import com.goldze.mvvmhabit.utils.RetrofitClient;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2017/7/16.
 */

public class AppApplication extends BaseApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(BuildConfig.DEBUG);
        Utils.init(this);
        initCrash();
        GM.initHeader(AppUtils.getAppVersionName());
        RetrofitClient.baseUrl = BuildConfig.DEBUG?NPC.DEBUG_URL:NPC.URL;
        WebSocketHelper.WS_URL=BuildConfig.DEBUG?NPC.DEBUG_WS_URL:NPC.WS_URL;
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
        initX5();
    }

    private void initX5(){
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                KLog.e("app", " onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(BossActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
}
