package com.goldze.mvvmhabit.module.router;

/**
 * 用于组件开发中，ARouter单Activity跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * Created by goldze on 2018/6/21
 */

public class RouterActivityPath {
    /**
     * 主业务组件
     */
    public static class Main {
        private static final String MAIN = "/main";

        public static final String PAGER_MAIN = MAIN +"/Main";
    }

    public static class App {
        private static final String APP = "/app";

        public static final String PAGER_LOGIN = APP +"/Login";
    }
}
