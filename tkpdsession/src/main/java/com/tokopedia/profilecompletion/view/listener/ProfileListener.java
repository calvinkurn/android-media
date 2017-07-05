package com.tokopedia.profilecompletion.view.listener;

import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainData;

/**
 * Created by stevenfredian on 6/22/17.
 */

public interface ProfileListener {

    interface Get {
        void onGetUserInfo(GetUserInfoDomainData getUserInfoDomainData);
    }

    interface Set {
        boolean onSetUserInfo();
    }
}
