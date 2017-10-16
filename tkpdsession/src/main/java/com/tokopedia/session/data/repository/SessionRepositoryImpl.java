package com.tokopedia.session.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.session.data.source.CreatePasswordDataSource;
import com.tokopedia.session.data.source.CloudDiscoverDataSource;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.data.source.LocalDiscoverDataSource;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public class SessionRepositoryImpl implements SessionRepository {


    private final CloudDiscoverDataSource cloudDiscoverDataSource;
    private final LocalDiscoverDataSource localDiscoverDataSource;
    private final GetTokenDataSource getTokenDataSource;
    private final CreatePasswordDataSource createPasswordDataSource;

    public SessionRepositoryImpl(CloudDiscoverDataSource cloudDiscoverDataSource,
                                 LocalDiscoverDataSource localDiscoverDataSource,
                                 GetTokenDataSource getTokenDataSource,
                                 CreatePasswordDataSource createPasswordDataSource) {
        this.cloudDiscoverDataSource = cloudDiscoverDataSource;
        this.localDiscoverDataSource = localDiscoverDataSource;
        this.getTokenDataSource = getTokenDataSource;
        this.createPasswordDataSource = createPasswordDataSource;
    }

    @Override
    public Observable<DiscoverViewModel> getDiscoverFromCloud() {
        return cloudDiscoverDataSource.getDiscover();
    }

    @Override
    public Observable<DiscoverViewModel> getDiscoverFromLocal() {
        return localDiscoverDataSource.getDiscover();
    }

    @Override
    public Observable<TokenViewModel> getAccessToken(RequestParams params) {
        return getTokenDataSource.getAccessToken(params);
    }

    @Override
    public Observable<CreatePasswordViewModel> createPassword(RequestParams params) {
        return createPasswordDataSource.createPassword(params);
    }
}
