package com.goldze.mvvmhabit.data.source.http;

import com.goldze.mvvmhabit.data.source.MyHttpDataSource;
import com.goldze.mvvmhabit.data.source.http.service.MyApiService;
import com.goldze.mvvmhabit.entity.CoinIntroductionEntity;
import com.goldze.mvvmhabit.entity.DepthEntity;
import com.goldze.mvvmhabit.entity.EntrustHistoryEntity;
import com.goldze.mvvmhabit.entity.MarketTradeEntity;
import com.goldze.mvvmhabit.entity.QueryInfoEntity;
import com.goldze.mvvmhabit.entity.QueryTransferEntity;
import com.goldze.mvvmhabit.entity.TickersOnTopEntity;
import com.goldze.mvvmhabit.entity.TradingPairEntity;
import com.goldze.mvvmhabit.entity.UserEntrustListEntity;
import com.goldze.mvvmhabit.entity.UserInfoEntity;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.PagingResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class MyHttpDataSourceImpl implements MyHttpDataSource {
    private MyApiService apiService;
    private volatile static MyHttpDataSourceImpl INSTANCE = null;

    public static MyHttpDataSourceImpl getInstance(MyApiService apiService) {
        if (INSTANCE == null) {
            synchronized (MyHttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyHttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private MyHttpDataSourceImpl(MyApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<CommonResponse<Map>> verificationCode(Map map) {
        return apiService.verificationCode(map);
    }

    @Override
    public Observable<CommonResponse<Map>> findVerificationCode(Map map) {
        return apiService.findVerificationCode(map);
    }

    @Override
    public Observable<CommonResponse<Map>> registered(Map map) {
        return apiService.registered(map);
    }

    @Override
    public Observable<CommonResponse<Map>> login(Map map) {
        return apiService.login(map);
    }

    @Override
    public Observable<CommonResponse<Map>> fundsPassword(Map map) {
        return apiService.fundsPassword(map);
    }

    @Override
    public Observable<CommonResponse<Map>> loginPassword(Map map) {
        return apiService.loginPassword(map);
    }

    @Override
    public Observable<CommonResponse<Map>> verified(Map map) {
        return apiService.verified(map);
    }

    @Override
    public Observable<CommonResponse<UserInfoEntity>> info(Map map) {
        return apiService.info(map);
    }

    @Override
    public Observable<CommonResponse<Map>> checkVerificationCode(Map map) {
        return apiService.checkVerificationCode(map);
    }

    @Override
    public Observable<CommonResponse<Map>> signOut(Map map) {
        return apiService.signOut(map);
    }

    @Override
    public Observable<CommonResponse<Map>> phoneOrEmail(Map map) {
        return apiService.phoneOrEmail(map);
    }

    @Override
    public Observable<CommonResponse<Map>> uploadFile(RequestBody requestBody) {
        return apiService.uploadFile(requestBody);
    }

    @Override
    public Observable<CommonResponse<Map>> infoBO(Map map) {
        return apiService.infoBO(map);
    }

    @Override
    public Observable<CommonResponse<Map>> infoByToken(Map map) {
        return apiService.infoByToken(map);
    }

    @Override
    public Observable<CommonResponse<List<TickersOnTopEntity>>> tickersOnTop(Map map) {
        return apiService.tickersOnTop(map);
    }

    @Override
    public Observable<CommonResponse<List<TickersOnTopEntity>>> tickers(Map map) {
        return apiService.tickers(map);
    }

    @Override
    public Observable<CommonResponse<Map>> querySum(Map map) {
        return apiService.querySum(map);
    }

    @Override
    public Observable<CommonResponse<QueryInfoEntity>> queryInfo(Map map) {
        return apiService.queryInfo(map);
    }

    @Override
    public Observable<CommonResponse<DepthEntity>> depth(Map map) {
        return apiService.depth(map);
    }

    @Override
    public Observable<CommonResponse<List<UserEntrustListEntity>>> userEntrustList(Map map) {
        return apiService.userEntrustList(map);
    }

    @Override
    public Observable<CommonResponse<TradingPairEntity>> tradingPair(Map map) {
        return apiService.tradingPair(map);
    }

    @Override
    public Observable<CommonResponse<UserEntrustListEntity>> userEntrust(Map map) {
        return apiService.userEntrust(map);
    }

    @Override
    public Observable<CommonResponse<Map>> cancelEntrust(List<Map<String, String>> map) {
        return apiService.cancelEntrust(map);
    }

    @Override
    public Observable<CommonResponse<PagingResponse<EntrustHistoryEntity>>> entrustHistoryPagination(Map map) {
        return apiService.entrustHistoryPagination(map);
    }

    @Override
    public Observable<CommonResponse<List<MarketTradeEntity>>> marketTrade(Map map) {
        return apiService.marketTrade(map);
    }

    @Override
    public Observable<CommonResponse<CoinIntroductionEntity>> coinIntroduction(Map map) {
        return apiService.coinIntroduction(map);
    }

    @Override
    public Observable<CommonResponse<PagingResponse<QueryTransferEntity>>> queryTransfer(Map map) {
        return apiService.queryTransfer(map);
    }
}