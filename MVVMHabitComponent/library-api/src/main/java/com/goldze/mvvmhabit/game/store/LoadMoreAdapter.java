package com.goldze.mvvmhabit.game.store;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class LoadMoreAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> implements LoadMoreModule {

    public LoadMoreAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public LoadMoreAdapter(int layoutResId) {
        super(layoutResId);
    }
}