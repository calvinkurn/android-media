package com.tokopedia.tokocash.historytokocash.data.repository;

import com.tokopedia.tokocash.historytokocash.data.datasource.WalletDataSourceFactory;
import com.tokopedia.tokocash.historytokocash.data.mapper.HelpHistoryDataMapper;
import com.tokopedia.tokocash.historytokocash.data.mapper.TokoCashHistoryMapper;
import com.tokopedia.tokocash.historytokocash.domain.IWalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import java.util.HashMap;
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
    public Observable<TokoCashHistoryData> getTokoCashHistoryData(HashMap<String, String> mapParams) {
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
    public Observable<Boolean> submitHelpHistory(HashMap<String, String> mapParams) {
        return walletDataSourceFactory.create().submitHelpTokoCash(mapParams);
    }
}
