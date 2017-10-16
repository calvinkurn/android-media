package com.tokopedia.session.register.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.data.model.DiscoverViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public interface SessionRepository {


    Observable<DiscoverViewModel> getDiscoverFromCloud();

    Observable<DiscoverViewModel> getDiscoverFromLocal();

    Observable<TokenViewModel> getAccessToken(RequestParams params);
}
