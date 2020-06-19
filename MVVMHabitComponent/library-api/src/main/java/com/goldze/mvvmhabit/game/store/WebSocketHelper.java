package com.goldze.mvvmhabit.game.store;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.goldze.mvvmhabit.entity.DepthDetailEntity;
import com.goldze.mvvmhabit.entity.DepthEntity;
import com.goldze.mvvmhabit.entity.MarketTradeEntity;
import com.goldze.mvvmhabit.entity.TickersOnTopEntity;
import com.goldze.mvvmhabit.entity.UserEntrustListEntity;
import com.goldze.mvvmhabit.entity.WsMsgEntity;
import com.goldze.mvvmhabit.entity.WsSbTpEntity;
import com.goldze.mvvmhabit.game.bazzi.CommonBusEvent;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.equip.GZipUtil;
import com.goldze.mvvmhabit.game.mounts.CommonCallback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.goldze.mvvmhabit.bus.RxBus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketHelper {

    public static String WS_URL;
    private WebSocket mWebSocket;
    private int readTimeout = 10;
    private int writeTimeout = 10;
    private int connectTimeout = 10;
    private int beatTim = 2500;
    private long systemTime;
    //    private String beatStr="";
    private CommonCallback commonCallback;
    private Timer timer;
    private OkHttpClient mOkHttpClient;
    private Request request;
    private EchoWebSocketListener socketListener;
    private boolean openBus = true;
    private boolean autoConnect = true;
    private Map<String, String> paramsMap = new ArrayMap<>();
    private Gson gson = new Gson();
    private Queue<WsSbTpEntity> queue = new LinkedList<WsSbTpEntity>();
//    private WsMsgEntity<String> wsMsgEntity=new WsMsgEntity<>();

    private static WebSocketHelper instance;

    public static WebSocketHelper getInstance() {
        if (instance == null) {
            synchronized (WebSocketHelper.class) {  //1
                if (instance == null)          //2
                    instance = new WebSocketHelper();  //3
            }
        }
        return instance;
    }

    private WebSocketHelper() {

    }

    public void initSocket() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        LogUtils.d("WS_URL->" + WS_URL + NPC.HEAD_WS_URL);
        request = new Request.Builder().url(WS_URL + NPC.HEAD_WS_URL).build();
        socketListener = new EchoWebSocketListener();
        mOkHttpClient.newWebSocket(request, socketListener);
        mOkHttpClient.dispatcher().executorService().shutdown();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            initSocket();
        }
    };

    public void setBus(boolean bus) {
        this.openBus = bus;
    }

    public void sendData(String data) {
        if (mWebSocket != null) {
            if (!data.contains("pong"))
                LogUtils.d("sendData:" + data);
            mWebSocket.send(data);
        }
    }

    public void subscribeData(WsSbTpEntity data) {
        data.setCmd("sub");
        if (mWebSocket != null) {
            LogUtils.d("subscribeData:" + data);
            mWebSocket.send(gson.toJson(data));
        } else {

        }
    }

    public void cancelSubscribe(WsSbTpEntity data) {
        data.setCmd("unsub");
        if (mWebSocket != null) {
            LogUtils.d("cancelSubscribe:" + data);
            mWebSocket.send(gson.toJson(data));
        } else {

        }
    }

    public void setCallback(CommonCallback commonCallback) {
        this.commonCallback = commonCallback;
    }

    public void removeCallback() {
        this.commonCallback = null;
    }

    public void closeWebSocket() {
        if (mWebSocket != null) {
            mWebSocket.close(-101, "主动优雅的关闭连接");
//            mWebSocket.cancel();暴力关闭
        }
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    private void initBeatTim() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                LogUtils.d("initBeatTim");
                long currentTime = System.currentTimeMillis();
                if (currentTime - systemTime > beatTim * 2) {
                    closeWebSocket();
                } else {
                    systemTime = currentTime;
                }

            }
        }, beatTim);
    }

    private void closeBatTim() {
        if (timer != null) {
            LogUtils.d("closeBatTim");
            timer.cancel();
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            LogUtils.d("onOpen");
            mWebSocket = webSocket;
            systemTime = System.currentTimeMillis();
            initBeatTim();
            if (commonCallback != null) {
                commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 1, response);
            }
            if (openBus) {
                RxBus.getDefault().post(new CommonBusEvent<Response>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 1, response));
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
//            LogUtils.d("onMessage bytes:" + bytes.hex());
//            if (commonCallback != null) {
//                commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 2, bytes);
//            }
//            if (openBus) {
//                RxBus.getDefault().post(new CommonBusEvent<ByteString>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 2, bytes));
//            }
            try {
                String text= GZipUtil.uncompressToString(bytes.toByteArray());
                if (text.contains("ping")) {
                    JsonParser parser = new JsonParser();
                    JsonObject object = parser.parse(text).getAsJsonObject();
                    String pong = object.get("ping").getAsString();
//                    paramsMap.clear();
                    paramsMap.put("pong", pong);
                    sendData(gson.toJson(paramsMap));
                    return;
                }
                LogUtils.d("onMessage text:" + text);
                WsMsgEntity entity = gson.fromJson(text, WsMsgEntity.class);
                if (commonCallback != null) {
                    commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 3, entity);
                }
                if (openBus) {
                    if (NPC.HOME_WS_CMD.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        TickersOnTopEntity data = new Gson().fromJson(json, TickersOnTopEntity.class);
                        RxBus.getDefault().post(data);
                        return;
                    }else if (NPC.DEAL_WS_CMD.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        DepthEntity data = new Gson().fromJson(json, DepthEntity.class);
                        RxBus.getDefault().post(data);
                    }else if (NPC.DEAL_WS_CMD2.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        UserEntrustListEntity data = new Gson().fromJson(json, UserEntrustListEntity.class);
                        RxBus.getDefault().post(data);
                    }else if (NPC.KCHART_PLATE_WS_CMD.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        DepthDetailEntity data = new Gson().fromJson(json, DepthDetailEntity.class);
                        RxBus.getDefault().post(data);
                    }else if (NPC.KCHART_MAKE_WS_CMD.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        MarketTradeEntity data = new Gson().fromJson(json, MarketTradeEntity.class);
                        RxBus.getDefault().post(data);
                    }

//                    RxBus.getDefault().post(new CommonBusEvent<WsMsgEntity>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 3, entity));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            try {
                if (text.contains("ping")) {
                    JsonParser parser = new JsonParser();
                    JsonObject object = parser.parse(text).getAsJsonObject();
                    String pong = object.get("ping").getAsString();
//                    paramsMap.clear();
                    paramsMap.put("pong", pong);
                    sendData(gson.toJson(paramsMap));
                    return;
                }
                LogUtils.d("onMessage text:" + text);
                WsMsgEntity entity = gson.fromJson(text, WsMsgEntity.class);
                if (commonCallback != null) {
                    commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 3, entity);
                }
                if (openBus) {
                    if (NPC.HOME_WS_CMD.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        TickersOnTopEntity data = new Gson().fromJson(json, TickersOnTopEntity.class);
                        RxBus.getDefault().post(data);
                        return;
                    }else if (NPC.DEAL_WS_CMD.equals(entity.getTopic()) && entity.getData() != null){
                        String json = gson.toJson(entity.getData());
                        DepthEntity data = new Gson().fromJson(json, DepthEntity.class);
                        RxBus.getDefault().post(data);
                    }
                    RxBus.getDefault().post(new CommonBusEvent<WsMsgEntity>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, 3, entity));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            LogUtils.d("onClosed:code->" + code + " reason->" + reason);
            if (commonCallback != null) {
                commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, -1, code);
            }
            if (openBus) {
                RxBus.getDefault().post(new CommonBusEvent<Integer>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, -1, code));
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            LogUtils.d("onClosing:code->" + code + " reason->" + reason);
            if (commonCallback != null) {
                commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, -2, code);
            }
            if (openBus) {
                RxBus.getDefault().post(new CommonBusEvent<Integer>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, -2, code));
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            LogUtils.d("onFailure:" + t.getMessage());
            closeBatTim();
            if (commonCallback != null) {
                commonCallback.onCommon(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, -3, response);
            }
            if (openBus) {
                RxBus.getDefault().post(new CommonBusEvent<Response>(NPC.WEBSOCKET_CALL_ACTIVITY_TAG, -3, response));
            }
            if (autoConnect) {
                handler.sendEmptyMessageDelayed(1, 500);
            }
        }
    }
}
