package com.goldze.mvvmhabit.game.store;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.game.mounts.DialogBottomConvert;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomDialog {
    public BottomSheetDialog mBottomSheetDialog;
    public Activity activity;

    public BottomDialog(Activity activity) {
        this.activity = activity;
        mBottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialog);
    }

    public BottomDialog setView(int layoutId, DialogBottomConvert convert) {
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        mBottomSheetDialog.setContentView(view);
        if (convert != null) {
            convert.viewCreate(new BaseViewHolder(view), mBottomSheetDialog);
        }
        return this;
    }

    public BottomDialog show() {
        mBottomSheetDialog.show();
        return this;
    }

    public void dismiss() {
        mBottomSheetDialog.dismiss();
    }
}
