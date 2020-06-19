package com.goldze.mvvmhabit.game.bazzi;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.balysv.materialripple.MaterialRippleLayout;

public class ViewBindingAdapter {

    @BindingAdapter("android:src")
    public static void setSrc(ImageButton view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:textColor")
    public static void setTextColor(TextView view, int colorId) {
        view.setTextColor(view.getContext().getResources().getColor(colorId));
    }

    @BindingAdapter("android:background")
    public static void setEditBackground(EditText view, int resId) {
        int left = view.getPaddingLeft();
        int right = view.getPaddingRight();
        int top = view.getPaddingTop();
        int bottom = view.getPaddingBottom();
        view.setBackgroundResource(resId);
        view.setPadding(left, top, right, bottom);
    }

    @BindingAdapter("clickEffect")
    public static void setClickEffect(View view, String colorId) {
        if(!(view.getParent() instanceof MaterialRippleLayout)){
            MaterialRippleLayout.on(view)
                    .rippleColor(Color.parseColor(colorId))
                    .rippleAlpha(0.08f)
                    .rippleOverlay(true)
                    .create();
        }
    }

}
