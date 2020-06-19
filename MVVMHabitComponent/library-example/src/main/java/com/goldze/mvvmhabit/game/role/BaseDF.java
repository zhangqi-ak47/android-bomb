package com.goldze.mvvmhabit.game.role;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.game.mounts.DialogFragmentConvert;

public class BaseDF extends DialogFragment {

    @LayoutRes
    protected int mLayoutResId = 0;
    protected Context mContext;
    protected float mDimAmount = 0.5f;
    protected int mGravity = Gravity.CENTER;
    protected int mMargin = 0;
    protected int mAnimStyle = 0;
    protected boolean mCancel = true;
    protected int mWidth = 0;
    protected int mHeight = 0;

    protected DialogFragmentConvert convert;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutResId == 0) {
            dismiss();
            return null;
        }
        return inflater.inflate(mLayoutResId, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (convert!=null){
            convert.viewCreate(new BaseViewHolder(view), this, getChildFragmentManager());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = mDimAmount;
            params.gravity = mGravity;
            if (mWidth == 0) {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (mWidth == -1) {
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
            } else if (mWidth == -2) {
                params.width = getScreenWidth(getContext()) - 2 * dp2px(getContext(), mMargin);
            } else if (mWidth > 0) {
                params.width = dp2px(getContext(), mWidth);
            }
            if (mHeight == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (mHeight == -1) {
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
            } else if (mHeight == -2) {
                params.height = getScreenWidth(getContext()) - 2 * dp2px(getContext(), mMargin);
            } else if (mHeight > 0) {
                params.height = dp2px(getContext(), mWidth);
            }
            if (mAnimStyle != 0) {
                window.setWindowAnimations(mAnimStyle);
            }
            window.setAttributes(params);
        }
        setCancelable(mCancel);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (convert!=null){
            convert.onDismiss(this);
        }
    }

    public void showDialog(FragmentManager manager) {
        show(manager, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag).addToBackStack(null);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public int dp2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
