package com.tokopedia.tokocash.activation.data;

import com.tokopedia.tokocash.activation.data.mapper.ActivateTokoCashMapper;
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
    private ActivateTokoCashMapper mapper;

    @Inject
    public ActivateRepository(TokoCashApi tokoCashApi, ActivateTokoCashMapper mapper) {
        this.activateTokoCashCloudDataStore = new ActivateTokoCashCloudDataStore(tokoCashApi);
        this.mapper = mapper;
    }

    @Override
    public Observable<ActivateTokoCashData> requestOTPWallet() {
        return activateTokoCashCloudDataStore.requestOTPWallet()
                .map(mapper);
    }

    @Override
    public Observable<ActivateTokoCashData> linkedWalletToTokoCash(HashMap<String, String> mapParam) {
        return activateTokoCashCloudDataStore.linkedWalletToTokoCash(mapParam)
                .map(mapper);
    }
}
