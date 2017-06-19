package com.tokopedia.profilecompletion.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainModel;

import rx.Observable;

/**
 * @author by nisie on 6/19/17.
 */

public interface ProfileRepository {

    Observable<GetUserInfoDomainModel> getUserInfo(TKPDMapParam<String, Object> parameters);
}
