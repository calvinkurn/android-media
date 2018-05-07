package com.tokopedia.tokocash.accountsetting.data;

import com.tokopedia.tokocash.accountsetting.data.mapper.OAuthInfoMapper;
import com.tokopedia.tokocash.accountsetting.domain.IAccountSettingRepository;
import com.tokopedia.tokocash.accountsetting.presentation.model.OAuthInfo;
import com.tokopedia.tokocash.network.api.WalletApi;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class AccountSettingRepository implements IAccountSettingRepository {

    private AccountSettingDataStore accountSettingDataStore;
    private OAuthInfoMapper oAuthInfoMapper;

    @Inject
    public AccountSettingRepository(WalletApi walletApi, OAuthInfoMapper oAuthInfoMapper) {
        this.accountSettingDataStore = new AccountSettingCloudDataStore(walletApi);
        this.oAuthInfoMapper = oAuthInfoMapper;
    }

    @Override
    public Observable<OAuthInfo> getOAuthInfo(HashMap<String, String> mapParam) {
        return accountSettingDataStore.getOAuthInfo()
                .map(oAuthInfoMapper);
    }

    @Override
    public Observable<Boolean> unlinkAccountTokoCash(HashMap<String, String> mapParam) {
        return accountSettingDataStore.unlinkAccountTokoCash(mapParam);
    }
}
