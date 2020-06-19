package com.goldze.mvvmhabit.game.role;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public abstract class BaseLF1<V extends ViewDataBinding, VM extends BaseVM> extends BaseF<V, VM> {

    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isInit=true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**初始化的时候去加载数据**/
        isCanLoadData();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            if (isFirst){
                isFirst=false;
                initLazyFlow();
            }
            if (!isLoad){
                isLoad = true;
                resumeLoad();
            }
        } else {
            if (isLoad) {
                isLoad = false;
                pauseLoad();
            }
        }
    }

    @Override
    public int initFlow() {
        return 0;
    }


    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract int initLazyFlow();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    protected void pauseLoad() {
    }

    /**
     * 当视图已经对用户可见并且加载过数据，如果需要在切换到其他页面时恢复加载数据，可以调用此方法
     */
    protected void resumeLoad() {
    }

}
