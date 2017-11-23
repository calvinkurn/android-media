package com.tokopedia.profilecompletion.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;

import rx.Observable;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileSourceFactory profileSourceFactory;

    public ProfileRepositoryImpl(ProfileSourceFactory profileSourceFactory) {
        this.profileSourceFactory = profileSourceFactory;
    }


    @Override
    public Observable<GetUserInfoDomainModel> getUserInfo(TKPDMapParam<String, Object> parameters) {
        return profileSourceFactory.createCloudGetUserInfoSource().getUserInfo(parameters);
    }

    @Override
    public Observable<EditUserInfoDomainModel> editUserInfo(TKPDMapParam<String, Object> parameters) {
        return profileSourceFactory.createCloudEditUserInfoSource().editUserInfo(parameters);
    }
}
