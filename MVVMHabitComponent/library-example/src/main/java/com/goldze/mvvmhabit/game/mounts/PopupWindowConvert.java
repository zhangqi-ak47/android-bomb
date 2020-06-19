package com.goldze.mvvmhabit.game.mounts;

import android.widget.PopupWindow;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public interface PopupWindowConvert {

    void viewCreate(BaseViewHolder viewHolder, PopupWindow popupWindow);
    void onDismiss();
}
