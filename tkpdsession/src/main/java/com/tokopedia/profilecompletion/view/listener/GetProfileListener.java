package com.tokopedia.profilecompletion.view.listener;

import com.tokopedia.core.profile.model.GetUserInfoDomainData;

/**
 * Created by stevenfredian on 6/22/17.
 */

public interface GetProfileListener {
    void onGetUserInfo(GetUserInfoDomainData getUserInfoDomainData);
}
