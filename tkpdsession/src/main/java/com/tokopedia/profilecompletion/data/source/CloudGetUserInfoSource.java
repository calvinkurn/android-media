package com.tokopedia.profilecompletion.data.source;

import android.content.Context;

import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 6/19/17.
 */

public class CloudGetUserInfoSource {
    private final Context context;
    private final AccountsService accountsService;
    private final GetUserInfoMapper getUserInfoMapper;
    private final SessionHandler sessionHandler;

    public CloudGetUserInfoSource(Context context,
                                  AccountsService accountsService,
                                  GetUserInfoMapper getUserInfoMapper,
                                  SessionHandler sessionHandler) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUserInfoMapper = getUserInfoMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<GetUserInfoDomainModel> getUserInfo(Map<String, Object> parameters) {
        return accountsService.getApi()
                .getUserInfo(parameters)
                .map(getUserInfoMapper)
                .doOnNext(saveToCache());
    }

    private Action1<GetUserInfoDomainModel> saveToCache() {
        return new Action1<GetUserInfoDomainModel>() {
            @Override
            public void call(GetUserInfoDomainModel getUserInfoDomainModel) {
                UserSessionInterface userSession = new UserSession(context);
                if (!userSession.isLoggedIn()) {
                    sessionHandler.setTempLoginSession(String.valueOf(getUserInfoDomainModel
                            .getGetUserInfoDomainData().getUserId()));
                    sessionHandler.setTempPhoneNumber(getUserInfoDomainModel.getGetUserInfoDomainData
                            ().getPhone());
                    sessionHandler.setTempLoginName(getUserInfoDomainModel
                            .getGetUserInfoDomainData().getFullName());
                    sessionHandler.setTempLoginEmail(getUserInfoDomainModel
                            .getGetUserInfoDomainData().getEmail());
                }
                sessionHandler.setHasPassword(getUserInfoDomainModel.getGetUserInfoDomainData()
                        .isCreatedPassword());
                sessionHandler.setProfilePicture(getUserInfoDomainModel
                        .getGetUserInfoDomainData().getProfilePicture());
            }
        };
    }
}
