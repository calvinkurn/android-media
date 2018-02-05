package com.tokopedia.tokocash.activation.data;

import com.tokopedia.tokocash.activation.domain.IActivateRepository;
import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;
import com.tokopedia.tokocash.network.api.TokoCashApi;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public class ActivateRepository implements IActivateRepository {

    private ActivateTokoCashCloudDataStore activateTokoCashCloudDataStore;

    @Inject
    public ActivateRepository(TokoCashApi tokoCashApi) {
        this.activateTokoCashCloudDataStore = new ActivateTokoCashCloudDataStore(tokoCashApi);
    }

    @Override
    public Observable<ActivateTokoCashData> requestOTPWallet() {
        return activateTokoCashCloudDataStore.requestOTPWallet()
                .map(new ActivateTokoCashMapper());
    }

    @Override
    public Observable<ActivateTokoCashData> linkedWalletToTokoCash(HashMap<String, String> mapParam) {
        return activateTokoCashCloudDataStore.linkedWalletToTokoCash(mapParam)
                .map(new ActivateTokoCashMapper());
    }
}
