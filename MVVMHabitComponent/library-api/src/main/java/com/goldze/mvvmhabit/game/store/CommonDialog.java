package com.goldze.mvvmhabit.game.store;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.mounts.DialogFragmentConvert;
import com.goldze.mvvmhabit.game.role.BaseDF;

public class CommonDialog extends BaseDF {

    public CommonDialog(int mLayoutResId){
        this.mLayoutResId = mLayoutResId;
    }

    public CommonDialog show(FragmentManager manager) {
        super.showDialog(manager);
        return this;
    }

    public CommonDialog setmDimAmount(float mDimAmount) {
        this.mDimAmount = mDimAmount;
        return this;
    }

    public CommonDialog setmGravity(int mGravity) {
        this.mGravity = mGravity;
        return this;
    }

    public CommonDialog setmMargin(int mMargin) {
        this.mMargin = mMargin;
        return this;
    }

    public CommonDialog setmAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
        return this;
    }

    public CommonDialog setmCancel(boolean mCancel) {
        this.mCancel = mCancel;
        return this;
    }

    public CommonDialog setmWidth(int mWidth) {
        this.mWidth = mWidth;
        return this;
    }

    public CommonDialog setmHeight(int mHeight) {
        this.mHeight = mHeight;
        return this;
    }

    public CommonDialog setConvert(DialogFragmentConvert convert) {
        this.convert = convert;
        return this;
    }

}
