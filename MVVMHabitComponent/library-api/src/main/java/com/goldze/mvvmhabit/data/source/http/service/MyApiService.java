package com.goldze.mvvmhabit.data.source.http.service;

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
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.PagingResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyApiService {

    @POST(NPC.HEAD_URL+"/user/registered/send/verification-code")
    Observable<CommonResponse<Map>> verificationCode(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/find/Password/send/verification-code")
    Observable<CommonResponse<Map>> findVerificationCode(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/registered")
    Observable<CommonResponse<Map>> registered(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/login")
    Observable<CommonResponse<Map>> login(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/update/funds-password")
    Observable<CommonResponse<Map>> fundsPassword(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/update/login-password")
    Observable<CommonResponse<Map>> loginPassword(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/verified")
    Observable<CommonResponse<Map>> verified(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/info")
    Observable<CommonResponse<UserInfoEntity>> info(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/check/verification-code")
    Observable<CommonResponse<Map>> checkVerificationCode(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/sign-out")
    Observable<CommonResponse<Map>> signOut(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/update/phone-or-email")
    Observable<CommonResponse<Map>> phoneOrEmail(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/upload-file")
    Observable<CommonResponse<Map>> uploadFile(@Body RequestBody requestBody);

    @POST(NPC.HEAD_URL+"/user/infoBO")
    Observable<CommonResponse<Map>> infoBO(@Body Map map);

    @POST(NPC.HEAD_URL+"/user/infoByToken")
    Observable<CommonResponse<Map>> infoByToken(@Body Map map);

    @POST(NPC.HEAD_URL+"/market/tickersOnTop")
    Observable<CommonResponse<List<TickersOnTopEntity>>> tickersOnTop(@Body Map map);

    @POST(NPC.HEAD_URL+"/market/tickers")
    Observable<CommonResponse<List<TickersOnTopEntity>>> tickers(@Body Map map);

    @POST(NPC.HEAD_URL+"/account/query/sum")
    Observable<CommonResponse<Map>> querySum(@Body Map map);

    @POST(NPC.HEAD_URL+"/account/query/info")
    Observable<CommonResponse<QueryInfoEntity>> queryInfo(@Body Map map);

    @POST(NPC.HEAD_URL+"/market/depth")
    Observable<CommonResponse<DepthEntity>> depth(@Body Map map);

    @POST(NPC.HEAD_URL+"/order/user-entrust-list")
    Observable<CommonResponse<List<UserEntrustListEntity>>> userEntrustList(@Body Map map);

    @POST(NPC.HEAD_URL+"/account/query/account/trading-pair")
    Observable<CommonResponse<TradingPairEntity>> tradingPair(@Body Map map);

    @POST(NPC.HEAD_URL+"/order/add/user-entrust")
    Observable<CommonResponse<UserEntrustListEntity>> userEntrust(@Body Map map);

    @POST(NPC.HEAD_URL+"/order/cancel/entrust")
    Observable<CommonResponse<Map>> cancelEntrust(@Body List<Map<String, String>> map);

    @POST(NPC.HEAD_URL+"/order/entrust-history-pagination")
    Observable<CommonResponse<PagingResponse<EntrustHistoryEntity>>> entrustHistoryPagination(@Body Map map);

    @POST(NPC.HEAD_URL+"/market/trade")
    Observable<CommonResponse<List<MarketTradeEntity>>> marketTrade(@Body Map map);

    @POST(NPC.HEAD_URL+"/account/query/coin-introduction")
    Observable<CommonResponse<CoinIntroductionEntity>> coinIntroduction(@Body Map map);

    @POST(NPC.HEAD_URL+"/account/query/transfer")
    Observable<CommonResponse<PagingResponse<QueryTransferEntity>>> queryTransfer(@Body Map map);
}
