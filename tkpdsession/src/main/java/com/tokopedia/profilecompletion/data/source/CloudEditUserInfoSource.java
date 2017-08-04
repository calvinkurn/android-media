package com.tokopedia.profilecompletion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;

import rx.Observable;

/**
 * @author by nisie on 7/3/17.
 */

public class CloudEditUserInfoSource {
    private final Context context;
    private final AccountsService accountsService;
    private final EditUserInfoMapper editUserInfoMapper;

    public CloudEditUserInfoSource(Context context,
                                   AccountsService accountsService,
                                   EditUserInfoMapper editUserInfoMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.editUserInfoMapper = editUserInfoMapper;
    }

    public Observable<EditUserInfoDomainModel> editUserInfo(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .editProfile(parameters)
                .map(editUserInfoMapper);
    }

}
