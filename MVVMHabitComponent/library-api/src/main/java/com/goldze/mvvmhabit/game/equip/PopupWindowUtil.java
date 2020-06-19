package com.goldze.mvvmhabit.game.equip;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.game.mounts.PopupWindowConvert;

public class PopupWindowUtil {

//    public static void createPopupWindow(Activity activity, View parentView, int width, int x, int y, int layoutId, PopupWindowConvert convert) {
    public static PopupWindow createPopupWindow(Activity activity, int width, int layoutId, PopupWindowConvert convert) {
        PopupWindow popupWindow = new PopupWindow(activity);
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        if (convert!=null){
            convert.viewCreate(new BaseViewHolder(view), popupWindow);
        }

        popupWindow.setContentView(view);
        popupWindow.setWidth(width==0?ViewGroup.LayoutParams.WRAP_CONTENT:width);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (convert!=null){
                    convert.onDismiss();
                }
            }
        });
        return popupWindow;
//        popupWindow.showAsDropDown(parentView,x,y);
    }

    public static void setWindowShade(Activity activity, float f){
        WindowManager.LayoutParams lp =activity.getWindow().getAttributes();
        lp.alpha = f;
        activity.getWindow().setAttributes(lp);
    }

}
