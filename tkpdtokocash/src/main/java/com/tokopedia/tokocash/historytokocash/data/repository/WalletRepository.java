package com.tokopedia.tokocash.historytokocash.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.historytokocash.data.datasource.WalletDataSourceFactory;
import com.tokopedia.tokocash.historytokocash.data.entity.OAuthInfoEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.ResponseHelpHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;
import com.tokopedia.tokocash.historytokocash.data.mapper.HelpHistoryDataMapper;
import com.tokopedia.tokocash.historytokocash.data.mapper.TokoCashHistoryMapper;
import com.tokopedia.tokocash.historytokocash.domain.IWalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class WalletRepository implements IWalletRepository {

    private final WalletDataSourceFactory walletDataSourceFactory;

    @Inject
    public WalletRepository(WalletDataSourceFactory walletDataSourceFactory) {
        this.walletDataSourceFactory = walletDataSourceFactory;
    }

    @Override
    public Observable<TokoCashHistoryData> getTokoCashHistoryData(TKPDMapParam<String, Object> mapParams) {
        return walletDataSourceFactory.create()
                .getTokoCashHistoryData(mapParams)
                .map(new TokoCashHistoryMapper());
    }

    @Override
    public Observable<List<HelpHistoryTokoCash>> getHelpHistoryData() {
        return walletDataSourceFactory.create().getHelpHistoryData()
                .map(new HelpHistoryDataMapper());
    }

    @Override
    public Observable<ResponseHelpHistoryEntity> submitHelpHistory(String subject, String message, String category, String transactionId) {
        return walletDataSourceFactory.create().submitHelpHistory(subject, message, category, transactionId);
    }

    @Override
    public Observable<WithdrawSaldoEntity> moveToSaldo(String url, ParamsActionHistory paramsActionHistory) {
        return walletDataSourceFactory.create().moveToSaldo(url, paramsActionHistory);
    }

    @Override
    public Observable<OAuthInfoEntity> getOAuthInfo() {
        return walletDataSourceFactory.create().getOAuthInfo();
    }

    @Override
    public Observable<Boolean> unlinkAccountTokoCash(String refreshToken, String identifier, String identifierType) {
        return walletDataSourceFactory.create().unlinkAccountTokoCash(refreshToken, identifier, identifierType);
    }
}
