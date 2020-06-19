package com.goldze.mvvmhabit.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.goldze.mvvmhabit.data.source.MyHttpDataSource;
import com.goldze.mvvmhabit.data.source.MyLocalDataSource;
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
import me.goldze.mvvmhabit.base.BaseModel;
import okhttp3.RequestBody;

public class MyRepository extends BaseModel implements MyHttpDataSource, MyLocalDataSource {
    private volatile static MyRepository INSTANCE = null;
    private final MyHttpDataSource mHttpDataSource;

    private final MyLocalDataSource mLocalDataSource;

    private MyRepository(@NonNull MyHttpDataSource httpDataSource,
                           @NonNull MyLocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static MyRepository getInstance(MyHttpDataSource httpDataSource,
                                           MyLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (MyRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<CommonResponse<Map>> verificationCode(Map map) {
        return mHttpDataSource.verificationCode(map);
    }

    @Override
    public Observable<CommonResponse<Map>> findVerificationCode(Map map) {
        return mHttpDataSource.findVerificationCode(map);
    }

    @Override
    public Observable<CommonResponse<Map>> registered(Map map) {
        return mHttpDataSource.registered(map);
    }

    @Override
    public Observable<CommonResponse<Map>> login(Map map) {
        return mHttpDataSource.login(map);
    }

    @Override
    public Observable<CommonResponse<Map>> fundsPassword(Map map) {
        return mHttpDataSource.fundsPassword(map);
    }

    @Override
    public Observable<CommonResponse<Map>> loginPassword(Map map) {
        return mHttpDataSource.loginPassword(map);
    }

    @Override
    public Observable<CommonResponse<Map>> verified(Map map) {
        return mHttpDataSource.verified(map);
    }

    @Override
    public Observable<CommonResponse<UserInfoEntity>> info(Map map) {
        return mHttpDataSource.info(map);
    }

    @Override
    public Observable<CommonResponse<Map>> checkVerificationCode(Map map) {
        return mHttpDataSource.checkVerificationCode(map);
    }

    @Override
    public Observable<CommonResponse<Map>> signOut(Map map) {
        return mHttpDataSource.signOut(map);
    }

    @Override
    public Observable<CommonResponse<Map>> phoneOrEmail(Map map) {
        return mHttpDataSource.phoneOrEmail(map);
    }

    @Override
    public Observable<CommonResponse<Map>> uploadFile(RequestBody requestBody) {
        return mHttpDataSource.uploadFile(requestBody);
    }

    @Override
    public Observable<CommonResponse<Map>> infoBO(Map map) {
        return mHttpDataSource.infoBO(map);
    }

    @Override
    public Observable<CommonResponse<Map>> infoByToken(Map map) {
        return mHttpDataSource.infoByToken(map);
    }

    @Override
    public Observable<CommonResponse<List<TickersOnTopEntity>>> tickersOnTop(Map map) {
        return mHttpDataSource.tickersOnTop(map);
    }

    @Override
    public Observable<CommonResponse<List<TickersOnTopEntity>>> tickers(Map map) {
        return mHttpDataSource.tickers(map);
    }

    @Override
    public Observable<CommonResponse<Map>> querySum(Map map) {
        return mHttpDataSource.querySum(map);
    }

    @Override
    public Observable<CommonResponse<QueryInfoEntity>> queryInfo(Map map) {
        return mHttpDataSource.queryInfo(map);
    }

    @Override
    public Observable<CommonResponse<DepthEntity>> depth(Map map) {
        return mHttpDataSource.depth(map);
    }

    @Override
    public Observable<CommonResponse<List<UserEntrustListEntity>>> userEntrustList(Map map) {
        return mHttpDataSource.userEntrustList(map);
    }

    @Override
    public Observable<CommonResponse<TradingPairEntity>> tradingPair(Map map) {
        return mHttpDataSource.tradingPair(map);
    }

    @Override
    public Observable<CommonResponse<UserEntrustListEntity>> userEntrust(Map map) {
        return mHttpDataSource.userEntrust(map);
    }

    @Override
    public Observable<CommonResponse<Map>> cancelEntrust(List<Map<String, String>> map) {
        return mHttpDataSource.cancelEntrust(map);
    }

    @Override
    public Observable<CommonResponse<PagingResponse<EntrustHistoryEntity>>> entrustHistoryPagination(Map map) {
        return mHttpDataSource.entrustHistoryPagination(map);
    }

    @Override
    public Observable<CommonResponse<List<MarketTradeEntity>>> marketTrade(Map map) {
        return mHttpDataSource.marketTrade(map);
    }

    @Override
    public Observable<CommonResponse<CoinIntroductionEntity>> coinIntroduction(Map map) {
        return mHttpDataSource.coinIntroduction(map);
    }

    @Override
    public Observable<CommonResponse<PagingResponse<QueryTransferEntity>>> queryTransfer(Map map) {
        return mHttpDataSource.queryTransfer(map);
    }
}