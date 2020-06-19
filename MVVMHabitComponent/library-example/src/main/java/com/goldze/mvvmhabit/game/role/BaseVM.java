package com.goldze.mvvmhabit.game.role;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.goldze.mvvmhabit.data.MyRepository;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.CommonUI;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.RxUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class BaseVM extends BaseViewModel<MyRepository> {

    public int actType;

    public UIChangeObservable _ui = new UIChangeObservable();
    public ServerRequestObservable _response = new ServerRequestObservable();

    private List<Disposable> rxBusList = new ArrayList<>();
    public Handler handler;

    public class UIChangeObservable {
        public SingleLiveEvent<CommonUI> updateEvent = new SingleLiveEvent<>();
    }

    public class ServerRequestObservable {
        public SingleLiveEvent<CommonResponse> responseEvent = new SingleLiveEvent<>();
    }

    public BaseVM(@NonNull Application application) {
        super(application);
    }

    public BaseVM(@NonNull Application application, MyRepository model) {
        super(application, model);
    }

    public abstract void initActType(int actType);

    @Override
    public void registerRxBus() {
        super.registerRxBus();
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        for (Disposable disposable : rxBusList) {
            RxSubscriptions.remove(disposable);
        }
        rxBusList.clear();
    }

    @SuppressLint("CheckResult")
    public void registerBus(Class cls, Consumer consumer) {
        Disposable disposable = RxBus.getDefault().toObservable(cls.getClass())
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

    //网络请求
    public <T extends BaseM> void addReuest(int type, boolean showLoading, boolean showError, Observable request) {
        if (!NetworkUtil.isNetworkAvailable(getApplication())) {
//            请检查网络是否连接
            _response.responseEvent.setValue(new CommonResponse().type(type).code(-101));
            _ui.updateEvent.setValue(new CommonUI(101, type, NPC.SERVER_RESPONSE_TAG));
            return;
        }
        addSubscribe(request
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showLoadingBar();
                        if (showLoading) {
                            _ui.updateEvent.setValue(new CommonUI(104, type, NPC.SERVER_RESPONSE_TAG));
                        }
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
//                        hideLoadingBar();
                        if (showLoading) {
                            _ui.updateEvent.setValue(new CommonUI(105, type, NPC.SERVER_RESPONSE_TAG));
                        }
                    }
                })
                .subscribe(new Consumer<CommonResponse<T>>() {
                    @Override
                    public void accept(CommonResponse<T> response) throws Exception {
                        //成功
                        if (!response.success() && showError) {
                            _ui.updateEvent.setValue(new CommonUI(103, type, NPC.SERVER_RESPONSE_TAG, response.getMessage()));
                        }
                        _response.responseEvent.setValue(response.type(type));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //错误
                        throwable.printStackTrace();
                        _response.responseEvent.setValue(new CommonResponse().type(type).code(-102));
                        _ui.updateEvent.setValue(new CommonUI(102, type, NPC.SERVER_RESPONSE_TAG));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //完成
                        GM.Log.d("完成");
                    }
                }));
    }

    //文件上传
    public static RequestBody buidUploadBody(Map<String, String> pathMap){
        if (pathMap==null && pathMap.size()==0) return null;
        MultipartBody.Builder builder=new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry: pathMap.entrySet()){
            File file=new File(entry.getValue());
            if (!file.exists()) return null;
            builder.addFormDataPart(entry.getKey(), URLEncoder.encode(file.getName()), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        return builder.build();
    }

    //文件下载
    public static void downloadFile(){
        //统一用系统的downloadManager
    }

    public Map<String, Object> setParams(Object... params){
        Map<String, Object> mapData=new HashMap<>();
        if (params!=null){
            String key="";
            for (int i=1; i<=params.length; i++){
                if (i%2==0)
                    if (params[i-1]==null) continue;
                    else mapData.put(key, params[i-1]);
                else key= (String) params[i-1];

            }
        }
        return mapData;
    }

    public void showMsg(int code, String msg) {
        if (code==0) {
            GM.Toast.setGravity(Gravity.CENTER, 0, 0);
            GM.Toast.showLong(msg);
        }
    }
}
