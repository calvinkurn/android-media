package com.tokopedia.tokocash.pendingcashback.data;

import com.tokopedia.tokocash.activation.presentation.model.PendingCashback;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.pendingcashback.domain.IPendingCashbackRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/8/18.
 */

public class PendingCashbackRepository implements IPendingCashbackRepository {

    private PendingCashbackMapper pendingCashbackMapper;
    private PendingCashbackCloudDataStore pendingCashbackCloudDataStore;

    @Inject
    public PendingCashbackRepository(WalletApi walletApi, PendingCashbackMapper pendingCashbackMapper) {
        this.pendingCashbackCloudDataStore = new PendingCashbackCloudDataStore(walletApi);
        this.pendingCashbackMapper = pendingCashbackMapper;
    }

    @Override
    public Observable<PendingCashback> getPendingCashback(HashMap<String, String> mapParam) {
        return pendingCashbackCloudDataStore.getPendingCashback(mapParam)
                .map(pendingCashbackMapper);
    }
}
