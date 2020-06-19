package com.goldze.mvvmhabit.data.source.local;

import com.goldze.mvvmhabit.data.source.MyLocalDataSource;

public class MyLocalDataSourceImpl implements MyLocalDataSource {
    private volatile static MyLocalDataSourceImpl INSTANCE = null;

    public static MyLocalDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (MyLocalDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyLocalDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private MyLocalDataSourceImpl() {
        //数据库Helper构建
    }

}

