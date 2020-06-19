package com.goldze.mvvmhabit.game.mounts;

import androidx.fragment.app.FragmentManager;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.game.role.BaseDF;

public interface DialogFragmentConvert {

    void viewCreate(BaseViewHolder viewHolder, BaseDF dialog, FragmentManager fm);
    default void onDismiss(BaseDF dialog){

    }
}
