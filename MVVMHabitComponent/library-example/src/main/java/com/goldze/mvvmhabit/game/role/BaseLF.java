package com.goldze.mvvmhabit.game.role;

import androidx.databinding.ViewDataBinding;

public abstract class BaseLF<V extends ViewDataBinding, VM extends BaseVM> extends BaseF<V, VM> {

    public boolean isFirstLoad = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
            initLazyFlow();
        }
    }

    @Override
    public int initFlow() {
        return 0;
    }

    public abstract int initLazyFlow();

}
