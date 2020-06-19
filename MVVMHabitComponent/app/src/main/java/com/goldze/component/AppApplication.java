package com.goldze.component;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.goldze.base.config.ModuleLifecycleConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * Created by goldze on 2018/6/21 0021.
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
        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //....

        Bugly.init(this, "", BuildConfig.DEBUG);
        CrashReport.initCrashReport(getApplicationContext(), "", BuildConfig.DEBUG);

        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);
    }
}
