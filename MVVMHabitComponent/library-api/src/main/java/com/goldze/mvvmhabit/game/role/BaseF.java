package com.goldze.mvvmhabit.game.role;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import com.goldze.mvvmhabit.game.mounts.CommonCallback;
import com.goldze.mvvmhabit.game.mounts.EmptyViewConvert;
import com.goldze.mvvmhabit.game.mounts.RvAdapterConvert;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.CommonUI;
import com.goldze.mvvmhabit.game.store.LoadMoreAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;

public abstract class BaseF<V extends ViewDataBinding, VM extends BaseVM> extends BaseFragment<V, VM> implements CommonCallback {

    public String title;
    private int loadNum = 0;

    public Context mContext;
    public Activity mActivity;
    private KProgressHUD kProgressHUD;

    public CommonCallback fCallback;
    public Bundle aBundle;
    public LoadMoreAdapter mAdapter;
    public SwipeRefreshLayout refreshLayout;
    public Handler handler;
    private List<Disposable> rxBusList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CommonCallback) {
            fCallback = (CommonCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CommonCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
    }

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
    public void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        removeRxBus();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initLayout(inflater, container, savedInstanceState);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        aBundle = getArguments();
        if (aBundle != null) {
//            aBundle.getString(NPC.ARG_PARAM1);
//            aBundle.getString(NPC.ARG_PARAM2);
            initArgument();
        }
    }

    @Override
    public void initData() {
        super.initData();
        initFlow();

//        fCallback.onCommon(NPC.FRAGMENT_CALL_ACTIVITY_TAG, 0, null);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel._ui.updateEvent.observe(this, new Observer<CommonUI>() {
            @Override
            public void onChanged(CommonUI common) {
                try {
                    if (initLayoutUpdate(common) == 0) {
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
                    if (initServerResponse(common) == 0) {
                        if (common.success()) {
                            switch (common._type) {
                                case 1:
//                                    BaseM baseM= (BaseM) common.getResult();
                                    break;
                            }
                        } else if (common.getCode() > 0) {
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
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        Class<VM> vm = initVM();
        return ViewModelProviders.of(this, factory).get(vm == null ? defaultVM() : vm);
    }

    public Class<VM> defaultVM() {
        return (Class<VM>) BaseVM.class;
    }

    public abstract int initLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract int initArgument();

    public abstract int initFlow();

    public abstract int initLayoutUpdate(CommonUI common);

    public abstract int initServerResponse(CommonResponse common);

    public abstract Class<VM> initVM();

    public void onClickView(View view){

    };

    public void setStatuBarPading(int code) {
        View parent = mActivity.findViewById(R.id.layout_parent);
        if (parent == null) return;
        if (code == 0) {
            parent.setFitsSystemWindows(false);
        } else if (code == 1) {
            parent.setFitsSystemWindows(true);
        }
    }

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

    public Intent newInstance(Object... objs) {
//        BaseF fragment = new BaseF();
//        Bundle args = new Bundle();
//        args.putString(NPC.ARG_PARAM1, param1);
//        args.putString(NPC.ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
        return null;
    }

    public <T> LoadMoreAdapter initRvAdapter(int layoutId, List<T> data, RvAdapterConvert<T> rvAdapterConvert) {
        mAdapter = new LoadMoreAdapter<T, BaseViewHolder>(layoutId, data) {
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
        refreshLayout = mActivity.findViewById(R.id.layout_swipe);
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

    public void initOnLoadMore() {
        if (mAdapter == null || mAdapter.getLoadMoreModule() == null) return;
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(true);
        mAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(true);
        mAdapter.getLoadMoreModule().setPreLoadNumber(1);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoreData();
            }
        });
    }

    public void onLoadMoreData() {

    }

    public View setAdapterEmptyView(int layoutId, EmptyViewConvert convert){
        View emptyView = getLayoutInflater().inflate(layoutId, null);
        if (convert!=null){
            convert.viewConvert(new BaseViewHolder(emptyView));
        }
        return emptyView;
    }

   public void setLoadMoreStatu(int statu) {
        if (mAdapter == null || mAdapter.getLoadMoreModule() == null) return;
        switch (statu){
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
                if (!mAdapter.getLoadMoreModule().isLoading()){
                    mAdapter.getLoadMoreModule().loadMoreToLoading();
                }
                break;
        }
    }

    public void setRvAdapterClickListenerExample() {
//        quickAdapter.setOnItemClickListener(((adapter, view, position) -> {
//
//        }));
//
//        quickAdapter.setOnItemChildClickListener(((adapter, view, position) -> {
//
//        }));
    }

    public void startActivity(Intent intent) {
        if (mActivity != null)
            mActivity.startActivity(intent);
    }

    public void showLoading(int code, String msg) {
        if (kProgressHUD != null) {
            if (!kProgressHUD.isShowing()){
                loadNum=1;
                kProgressHUD.show();
            }else {
                ++loadNum;
            }
        } else {
            loadNum=1;
            View view=LayoutInflater.from(mActivity).inflate(R.layout.loading_layout, null);
            Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.loading_rote);
            LinearInterpolator lir=new LinearInterpolator();
            anim.setInterpolator(lir);
            view.findViewById(R.id.ldi_img).startAnimation(anim);

            kProgressHUD = KProgressHUD.create(mActivity)
                    .setCustomView(view)
                    .setBackgroundColor(c(R.color.transparent))
                    .setCancellable(true)
                    .show();
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
        if (code==0) {
            GM.Toast.setGravity(Gravity.CENTER, 0, 0);
            GM.Toast.showLong(msg);
        }
    }

    public Drawable d(int drawableId){
        return ContextCompat.getDrawable(getContext(), drawableId);
    }

    public int c(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    public int c(String color) {
        return Color.parseColor(color);
    }

    public String s(int stringId) {
        return getString(stringId);
    }

    public void setNaviTitle(int code, String title) {
        if (code<0) return;
        TextView textView = mActivity.findViewById(R.id.navi1_title);
        textView.setText(title);
        if (code == 0) {
            setNaviLeftClick(1, null);
        }
    }

    public void setNaviLeftClick(int code, CommonCallback callback) {
        ImageButton imageButton = mActivity.findViewById(R.id.navi1_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code==0 && callback!=null){
                    callback.onCommon(NPC.NAVIGATION_CLICK_TAG, code, null);
                }else if (code==1){
                    mActivity.finish();
                }
            }
        });
    }

    public void setNaviRightClick(int code, CommonCallback callback) {

    }

    public void setNaviLeftImg(int code, int img) {
        ImageButton imageButton = mActivity.findViewById(R.id.navi1_back);
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
