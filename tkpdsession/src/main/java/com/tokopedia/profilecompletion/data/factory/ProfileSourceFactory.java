package com.tokopedia.profilecompletion.data.factory;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.source.CloudEditUserInfoSource;
import com.tokopedia.profilecompletion.data.source.CloudGetUserInfoSource;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfileSourceFactory {

    private Context context;
    private final AccountsService accountsService;
    private GetUserInfoMapper getUserInfoMapper;
    private EditUserInfoMapper editUserInfoMapper;
    private final SessionHandler sessionHandler;

    public ProfileSourceFactory(Context context,
                                AccountsService accountsService,
                                GetUserInfoMapper getUserInfoMapper,
                                EditUserInfoMapper editUserInfoMapper,
                                SessionHandler sessionHandler) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUserInfoMapper = getUserInfoMapper;
        this.editUserInfoMapper = editUserInfoMapper;
        this.sessionHandler = sessionHandler;
    }

    public CloudGetUserInfoSource createCloudGetUserInfoSource() {
        return new CloudGetUserInfoSource(context, accountsService, getUserInfoMapper, sessionHandler);
    }

    public CloudEditUserInfoSource createCloudEditUserInfoSource() {
        return new CloudEditUserInfoSource(context, accountsService, editUserInfoMapper);
    }
}
