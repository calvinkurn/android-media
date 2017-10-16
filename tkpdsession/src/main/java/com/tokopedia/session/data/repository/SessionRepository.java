package com.tokopedia.session.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public interface SessionRepository {


    Observable<DiscoverViewModel> getDiscoverFromCloud();

    Observable<DiscoverViewModel> getDiscoverFromLocal();

    Observable<TokenViewModel> getAccessToken(RequestParams params);
}
