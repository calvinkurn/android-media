package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.apiservice.WalletService;
import com.tokopedia.tokocash.historytokocash.data.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.OAuthInfoEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.ResponseHelpHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;
import com.tokopedia.tokocash.historytokocash.presentation.Util;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class CloudWalletDataSource implements WalletDataSource {

    private static final String SUBJECT = "subject";
    private static final String MESSAGE = "message";
    private static final String CATEGORY = "category";
    private static final String TRANSACTION_ID = "transaction_id";

    private static final String REFUND_ID = "refund_id";
    private static final String REFUND_TYPE = "refund_type";

    private static final String REVOKE_TOKEN = "revoke_token";
    private static final String IDENTIFIER = "identifier";
    private static final String IDENTIFIER_TYPE = "identifier_type";

    private WalletService walletService;
    private Gson gson;

    public CloudWalletDataSource(WalletService walletService) {
        this.walletService = walletService;
        this.gson = new Gson();
    }

    @Override
    public Observable<TokoCashHistoryEntity> getTokoCashHistoryData(HashMap<String, Object> mapParams) {
        return walletService.getApi().getHistoryTokocash(mapParams)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<TokoCashHistoryEntity>>() {
                    @Override
                    public Observable<TokoCashHistoryEntity> call(Response<TkpdDigitalResponse> response) {
                        return Observable
                                .just(response.body().convertDataObj(TokoCashHistoryEntity.class));
                    }
                });
    }

    @Override
    public Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData() {
        String helpHistoryList = Util.loadJSONFromAsset("help_history_tokocash.json");
        return Observable.just(Arrays.asList((HelpHistoryTokoCashEntity[]) gson.fromJson(helpHistoryList,
                HelpHistoryTokoCashEntity[].class)));
    }

    @Override
    public Observable<ResponseHelpHistoryEntity> submitHelpHistory(String subject, String message, String category, String transactionId) {
        HashMap tkpdMapParam = new HashMap<>();
        tkpdMapParam.put(SUBJECT, subject);
        tkpdMapParam.put(MESSAGE, message);
        tkpdMapParam.put(CATEGORY, category);
        tkpdMapParam.put(TRANSACTION_ID, transactionId);
        return walletService.getApi().postHelpHistory(tkpdMapParam)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<ResponseHelpHistoryEntity>>() {
                    @Override
                    public Observable<ResponseHelpHistoryEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable
                                .just(tkpdDigitalResponseResponse.body().convertDataObj(ResponseHelpHistoryEntity.class));
                    }
                });
    }

    @Override
    public Observable<WithdrawSaldoEntity> moveToSaldo(String url, ParamsActionHistory paramsActionHistory) {
        TKPDMapParam tkpdMapParam = new TKPDMapParam();
        tkpdMapParam.put(REFUND_ID, paramsActionHistory.getRefundId());
        tkpdMapParam.put(REFUND_TYPE, paramsActionHistory.getRefundType());
        return walletService.getApi().withdrawSaldoFromTokocash(url, tkpdMapParam)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<WithdrawSaldoEntity>>() {
                    @Override
                    public Observable<WithdrawSaldoEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable.just(tkpdDigitalResponseResponse.body().convertDataObj(WithdrawSaldoEntity.class));
                    }
                });
    }

    @Override
    public Observable<OAuthInfoEntity> getOAuthInfo() {
        return walletService.getApi().getOAuthInfoAccount()
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<OAuthInfoEntity>>() {
                    @Override
                    public Observable<OAuthInfoEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return Observable.just(tkpdDigitalResponseResponse.body().convertDataObj(OAuthInfoEntity.class));
                    }
                });
    }

    @Override
    public Observable<Boolean> unlinkAccountTokoCash(String refreshToken, String identifier, String identifierType) {
        TKPDMapParam tkpdMapParam = new TKPDMapParam();
        tkpdMapParam.put(REVOKE_TOKEN, refreshToken);
        tkpdMapParam.put(IDENTIFIER, identifier);
        tkpdMapParam.put(IDENTIFIER_TYPE, identifierType);
        return walletService.getApi().revokeAccessAccountTokoCash(tkpdMapParam)
                .map(new Func1<Response<String>, Boolean>() {
                    @Override
                    public Boolean call(Response<String> stringResponse) {
                        return stringResponse.isSuccessful();
                    }
                });
    }
}