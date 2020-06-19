package com.goldze.mvvmhabit.game.mounts;

public interface CommonCallback {

    default String onCommon(String tag, int type, Object obj){return tag;};

}
