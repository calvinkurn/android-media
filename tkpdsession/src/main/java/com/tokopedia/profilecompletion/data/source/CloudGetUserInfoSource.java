package com.tokopedia.profilecompletion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 6/19/17.
 */

public class CloudGetUserInfoSource {
    private final Context context;
    private final AccountsService accountsService;
    private final GetUserInfoMapper getUserInfoMapper;

    public CloudGetUserInfoSource(Context context,
                                  AccountsService accountsService,
                                  GetUserInfoMapper getUserInfoMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUserInfoMapper = getUserInfoMapper;
    }

    public Observable<GetUserInfoDomainModel> getUserInfo(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .getUserInfo(parameters)
                .map(getUserInfoMapper)
                .doOnNext(saveToCache());
    }

    private Action1<GetUserInfoDomainModel> saveToCache() {
        return new Action1<GetUserInfoDomainModel>() {
            @Override
            public void call(GetUserInfoDomainModel getUserInfoDomainModel) {

            }
        };
    }
}
