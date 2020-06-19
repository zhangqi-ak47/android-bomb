package com.goldze.mvvmhabit.game.role;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.goldze.mvvmhabit.BR;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.app.AppViewModelFactory;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.equip.StatusBarUtil;
import com.goldze.mvvmhabit.game.mounts.CommonCallback;
import com.goldze.mvvmhabit.game.mounts.EmptyViewConvert;
import com.goldze.mvvmhabit.game.mounts.RvAdapterConvert;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.CommonUI;
import com.goldze.mvvmhabit.game.store.CustomLoadMoreView;
import com.goldze.mvvmhabit.game.store.LoadMoreAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

public abstract class BaseA<V extends ViewDataBinding, VM extends BaseVM> extends BaseActivity<V, VM> implements CommonCallback, SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;
    private KProgressHUD kProgressHUD;

    private int loadNum = 0;
    public Intent aIntent;
//    public LoadMoreAdapter mAdapter;
    public SwipeRefreshLayout refreshLayout;
    public Handler handler;

    private List<Disposable> rxBusList = new ArrayList<>();

    public void removeRxBus() {
        for (Disposable disposable : rxBusList) {
            RxSubscriptions.remove(disposable);
        }
        rxBusList.clear();
    }

    @SuppressLint("CheckResult")
    public void registerBus(Class cls, Consumer consumer) {
        Disposable disposable = RxBus.getDefault().toObservable(cls)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
        RxSubscriptions.add(disposable);
        rxBusList.add(disposable);
    }

    public void sendBus(Object obj) {
        RxBus.getDefault().post(obj);
    }

    public void sendStickyBus(Object obj) {
        RxBus.getDefault().postSticky(obj);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        removeRxBus();
        closeLoading(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return super.findViewById(id);
        }
        return (T) v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return initLayout(savedInstanceState);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void initParam() {
        super.initParam();
        aIntent = getIntent();
        if (aIntent != null) {
//          aIntent.getStringExtra(NPC.ARG_PARAM1);
//          aIntent.getStringExtra(NPC.ARG_PARAM2);
            initArgument();
        }
    }

    @Override
    public void initData() {
        super.initData();
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        GM.StatusBar.setTranslucentStatus(this);
        if (!GM.StatusBar.setStatusBarDarkTheme(this, true)) {
            GM.StatusBar.setStatusBarColor(this, 0x55000000);
        }
        setScreenDirection(0);

        initFlow();
    }

    public void setStatuBarColor(int code, int color) {
        if (code == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                GM.StatusBar.setStatusBarColor(this, R.color.transparent);
            } else {
                GM.StatusBar.setStatusBarColor(this, R.color._55000000);
            }
        } else if (code == 1) {
            GM.StatusBar.setStatusBarColor(this, color);
        }
    }

    public void setStatuBarVisibility(int code) {
        if (code == 0) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//全部显示出
        } else if (code == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void setScreenDirection(int code) {
        if (code == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        } else if (code == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        }
    }

    public void setStatuBarPading(int code) {
        View parent = findViewById(R.id.layout_parent);
        if (parent == null) return;
        if (code == 0) {
            parent.setFitsSystemWindows(false);
        } else if (code == 1) {
            parent.setFitsSystemWindows(true);
        }
    }

    public int getStatuBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel._ui.updateEvent.observe(this, new Observer<CommonUI>() {
            @Override
            public void onChanged(CommonUI common) {
                try {
                    int code = initLayoutUpdate(common);
                    if (code >= 0) {
                        if (NPC.SERVER_RESPONSE_TAG.equals(common._tag)) {
                            switch (common._code) {
                                case 101:
                                    showMsg(0, "请检查网络是否连接");
                                    break;
                                case 102:
                                    showMsg(0, "请求失败，请稍后重试");
                                    break;
                                case 103:
                                    showMsg(0, (String) common._obj);
                                    break;
                                case 104:
                                    showLoading(0, "网络请求中。。。");
                                    break;
                                case 105:
                                    hideLoading(0);
                                    break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewModel._response.responseEvent.observe(this, new Observer<CommonResponse>() {
            @Override
            public void onChanged(CommonResponse common) {
                try {
                    int code = initServerResponse(common);
                    if (code >= 0) {
                        if (common.success()) {
                            switch (common._type) {
                                case 1:
//                                    BaseM baseM= (BaseM) common.getResult();
                                    break;
                            }
                        } else if (common.getCode() > 1) {
                            //服务器返回的问题
                        } else {
                            //自身的问题
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public VM initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        Class<VM> vm = initVM();
        return ViewModelProviders.of(this, factory).get(vm == null ? defaultVM() : vm);
    }

    public Class<VM> defaultVM() {
        return (Class<VM>) BaseVM.class;
    }

    public abstract int initLayout(Bundle savedInstanceState);

    public abstract int initArgument();

    public abstract int initFlow();

    public void onClickView(View view){

    };
    public abstract int initLayoutUpdate(CommonUI common);

    public abstract int initServerResponse(CommonResponse common);

    public abstract Class<VM> initVM();

    public Activity getActivity(){
        return this;
    }

    public void onFragmentClickView(View view){

    };

    @SuppressLint("HandlerLeak")
    public void initHandler() {
        if (handler != null) return;
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }
        };
    }

    public void handleMsg(Message msg) {

    }

    @SuppressLint("CheckResult")
    private void requestPermissions(Consumer consumer, String... authority) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(authority)
                .subscribe(consumer);
    }

    public Intent newInstance(Context context, Object... objs) {
//        Intent intent=new Intent(context, BaseA.class);
//        intent.putExtra(NPC.ARG_PARAM1, param1);
//        intent.putExtra(NPC.ARG_PARAM2, param2);
//        return intent;
        return null;
    }

    public <T> LoadMoreAdapter initRvAdapter(int layoutId, List<T> data, RvAdapterConvert<T> rvAdapterConvert) {
        LoadMoreAdapter mAdapter = new LoadMoreAdapter<T, BaseViewHolder>(layoutId, data) {
            @Override
            protected void convert(BaseViewHolder viewHolder, T item) {
                if (rvAdapterConvert != null) {
                    rvAdapterConvert.convert(viewHolder, item);
                }
            }
        };
        return mAdapter;
    }

    public <T> BaseQuickAdapter initQkAdapter(int layoutId, List<T> data, RvAdapterConvert<T> rvAdapterConvert) {
        BaseQuickAdapter adapter = new BaseQuickAdapter<T, BaseViewHolder>(layoutId, data) {
            @Override
            protected void convert(BaseViewHolder viewHolder, T item) {
                if (rvAdapterConvert != null) {
                    rvAdapterConvert.convert(viewHolder, item);
                }
            }
        };
        return adapter;
    }

    public void initOnRefresh() {
        refreshLayout = findViewById(R.id.layout_swipe);
        if (refreshLayout == null) return;
        refreshLayout.setColorSchemeResources(R.color.black);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
    }

    public void onRefreshData() {

    }

    public void setRefreshStatu(boolean statu) {
        if (refreshLayout == null || statu == refreshLayout.isRefreshing()) return;
        refreshLayout.setRefreshing(statu);
    }

    public void setRefreshEnabled(boolean statu) {
        if (refreshLayout == null) return;
        refreshLayout.setEnabled(statu);
    }

    public void initOnLoadMore(LoadMoreAdapter mAdapter, int loadType) {
        if (mAdapter == null || mAdapter.getLoadMoreModule() == null) return;
        mAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(true);
        mAdapter.getLoadMoreModule().setPreLoadNumber(1);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoreData(loadType);
            }
        });
    }

    public void onLoadMoreData(int loadType) {

    }

    public View setAdapterEmptyView(int layoutId, EmptyViewConvert convert){
        View emptyView = getLayoutInflater().inflate(layoutId, null);
        if (convert!=null){
            convert.viewConvert(new BaseViewHolder(emptyView));
        }
        return emptyView;
    }

    public void setLoadMoreStatu(LoadMoreAdapter mAdapter, int statu) {
        if (mAdapter == null || mAdapter.getLoadMoreModule() == null) return;
        switch (statu) {
            case -2:
                mAdapter.getLoadMoreModule().setEnableLoadMore(false);
                break;
            case -1:
                mAdapter.getLoadMoreModule().setEnableLoadMore(true);
                break;
            case 1:
                mAdapter.getLoadMoreModule().loadMoreComplete();
                break;
            case 2:
                mAdapter.getLoadMoreModule().loadMoreEnd();
                break;
            case 3:
                mAdapter.getLoadMoreModule().loadMoreFail();
                break;
            case 4:
                if (!mAdapter.getLoadMoreModule().isLoading()) {
                    mAdapter.getLoadMoreModule().loadMoreToLoading();
                }
                break;
        }
    }

    public void setRvAdapterClickListenerExample() {
//        mAdapter.setOnItemClickListener(((adapter, view, position) -> {
//
//        }));
//
//        mAdapter.setOnItemChildClickListener(((adapter, view, position) -> {
//
//        }));
    }

    public void addFragment(int frameId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void replaceFragment(int frameId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, fragment);
        fragmentTransaction.commit();
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showLoading(int code, String msg) {
        if (kProgressHUD != null) {
            if (!kProgressHUD.isShowing()){
                loadNum=1;
                if (!isDestroyed()){
                    kProgressHUD.show();
                }
            }else {
                ++loadNum;
            }
        } else {
            loadNum=1;
            View view=LayoutInflater.from(this).inflate(R.layout.loading_layout, null);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.loading_rote);
            LinearInterpolator lir=new LinearInterpolator();
            anim.setInterpolator(lir);
            view.findViewById(R.id.ldi_img).startAnimation(anim);

            if (!isDestroyed()){
                kProgressHUD = KProgressHUD.create(this)
                        .setCustomView(view)
                        .setBackgroundColor(c(R.color.transparent))
                        .setCancellable(true)
                        .show();
            }
        }
    }

    public void hideLoading(int code) {
        --loadNum;
        if (loadNum == 0) {
            if (kProgressHUD != null && kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
        }
    }

    public void closeLoading(int code) {
        loadNum = 1;
        hideLoading(code);
    }

    public void showPageLoading(int code, String msg) {

    }

    public void hidePageLoading(int code) {

    }

    public void showPageError(int code, String msg) {

    }

    public void hidePageError(int code) {

    }

    public void showMsg(int code, String msg) {
        if (code == 0) {
            GM.Toast.setGravity(Gravity.CENTER, 0, 0);
            GM.Toast.showLong(msg);
        }
    }

    public int c(int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    public int c(String color) {
        return Color.parseColor(color);
    }

    public String s(int stringId) {
        return getString(stringId);
    }

    public void setNaviTitle(int code, String title) {
        if (code<0) return;
        if (code<100){
            TextView textView = findViewById(R.id.navi1_title);
            textView.setText(title);
            if (code == 0) {
                setNaviLeftClick(1, null);
            }
        }else {
            findViewById(R.id.layout_navi1).setVisibility(View.GONE);
            findViewById(R.id.layout_navi2).setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.navi2_title);
            if (code==101){
                textView.setText(Html.fromHtml(title));
            }else {
                textView.setText(title);
            }
            setNaviLeftClick(101, null);
        }

    }

    public void setNaviLeftClick(int code, CommonCallback callback) {
        if (code<100){
            ImageButton imageButton = findViewById(R.id.navi1_back);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (code==0 && callback!=null){
                        callback.onCommon(NPC.NAVIGATION_CLICK_TAG, code, null);
                    }else if (code==1){
                        finish();
                    }
                }
            });
        }else {
            ImageButton imageButton = findViewById(R.id.navi2_back);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (code==100 && callback!=null){
                        callback.onCommon(NPC.NAVIGATION_CLICK_TAG, code, null);
                    }else if (code==101){
                        finish();
                    }
                }
            });
        }

    }

    public void setNaviRightClick(int code, CommonCallback callback) {
        if (code==101){
            ImageButton imageButton = findViewById(R.id.navi2_full);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback!=null){
                        callback.onCommon(NPC.NAVIGATION_CLICK_TAG, code, null);
                    }
                }
            });
        }
    }

    public void setNaviLeftImg(int code, int img) {
        ImageButton imageButton = findViewById(R.id.navi1_back);
        imageButton.setImageResource(img);
    }

    public void setNaviLeftTxt(int code, String txt) {

    }

    public void setNaviRightImg(int code, int img) {

    }

    public void setNaviRightTxt(int code, String txt) {

    }

    public void setClickEffect(View... views){
        if (views!=null && views.length>0){
            for (View view:views){
                if(!(view.getParent() instanceof MaterialRippleLayout)){
                    MaterialRippleLayout.on(view)
                            .rippleColor(Color.parseColor("#000000"))
                            .rippleAlpha(0.08f)
                            .rippleOverlay(true)
                            .create();
                }
            }
        }
    }
}
