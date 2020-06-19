package com.goldze.mvvmhabit.game.mounts;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public interface RvAdapterConvert<T> {

    void convert(BaseViewHolder viewHolder, T item);

}
