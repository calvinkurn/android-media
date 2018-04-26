package com.tokopedia.tokocash.historytokocash.data.repository;

import com.tokopedia.tokocash.historytokocash.data.datasource.WalletDataSourceFactory;
import com.tokopedia.tokocash.historytokocash.data.mapper.HelpHistoryDataMapper;
import com.tokopedia.tokocash.historytokocash.data.mapper.TokoCashHistoryMapper;
import com.tokopedia.tokocash.historytokocash.data.mapper.WithdrawSaldoMapper;
import com.tokopedia.tokocash.historytokocash.domain.IWalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WithdrawSaldo;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class WalletRepository implements IWalletRepository {

    private final WalletDataSourceFactory walletDataSourceFactory;
    private TokoCashHistoryMapper tokoCashHistoryMapper;
    private HelpHistoryDataMapper helpHistoryDataMapper;
    private WithdrawSaldoMapper withdrawSaldoMapper;

    @Inject
    public WalletRepository(WalletDataSourceFactory walletDataSourceFactory,
                            TokoCashHistoryMapper tokoCashHistoryMapper,
                            HelpHistoryDataMapper helpHistoryDataMapper,
                            WithdrawSaldoMapper withdrawSaldoMapper) {
        this.walletDataSourceFactory = walletDataSourceFactory;
        this.tokoCashHistoryMapper = tokoCashHistoryMapper;
        this.helpHistoryDataMapper = helpHistoryDataMapper;
        this.withdrawSaldoMapper = withdrawSaldoMapper;
    }

    @Override
    public Observable<TokoCashHistoryData> getTokoCashHistoryData(HashMap<String, String> mapParams) {
        return walletDataSourceFactory.create()
                .getTokoCashHistoryData(mapParams)
                .map(tokoCashHistoryMapper);
    }

    @Override
    public Observable<List<HelpHistoryTokoCash>> getHelpHistoryData() {
        return walletDataSourceFactory.create().getHelpHistoryData()
                .map(helpHistoryDataMapper);
    }

    @Override
    public Observable<Boolean> submitHelpHistory(HashMap<String, String> mapParams) {
        return walletDataSourceFactory.create().submitHelpTokoCash(mapParams);
    }

    @Override
    public Observable<WithdrawSaldo> withdrawTokoCashToSaldo(String url, HashMap<String, String> mapParams) {
        return walletDataSourceFactory.create().withdrawTokoCashToSaldo(url, mapParams)
                .map(withdrawSaldoMapper);
    }
}
