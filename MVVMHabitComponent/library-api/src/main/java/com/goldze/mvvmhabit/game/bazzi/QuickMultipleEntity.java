package com.goldze.mvvmhabit.game.bazzi;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class QuickMultipleEntity implements MultiItemEntity {

    // 你的数据内容
    public final static int TYPE_LAYOUT1=1;
    public final static int TYPE_LAYOUT2=2;
    public final static int TYPE_LAYOUT3=3;

    private int itemType;

    /**
     * 实现此方法，返回类型
     */
    @Override
    public int getItemType() {
        return itemType;
    }
}