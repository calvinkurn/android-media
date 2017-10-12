package com.tokopedia.session.register.data.repository;

import com.google.android.gms.auth.TokenData;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.domain.model.TokenViewModel;
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
