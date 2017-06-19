package com.tokopedia.profilecompletion.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.source.CloudGetUserInfoSource;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfileSourceFactory {

    private Context context;
    private final AccountsService accountsService;
    private GetUserInfoMapper getUserInfoMapper;

    public ProfileSourceFactory(Context context,
                                AccountsService accountsService,
                                GetUserInfoMapper getUserInfoMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUserInfoMapper = getUserInfoMapper;
    }

    public CloudGetUserInfoSource createCloudGetUserInfoSource() {
        return new CloudGetUserInfoSource(context, accountsService, getUserInfoMapper);
    }

}
