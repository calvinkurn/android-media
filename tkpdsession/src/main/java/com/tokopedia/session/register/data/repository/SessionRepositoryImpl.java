package com.tokopedia.session.register.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.data.factory.SessionFactory;
import com.tokopedia.session.register.data.model.DiscoverViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public class SessionRepositoryImpl implements SessionRepository {

    SessionFactory sessionFactory;

    public SessionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Observable<DiscoverViewModel> getDiscoverFromCloud() {
        return sessionFactory.createCloudDiscoverDataSource().getDiscover();
    }

    @Override
    public Observable<DiscoverViewModel> getDiscoverFromLocal() {
        return sessionFactory.createLocalDiscoverDataSource().getDiscover();
    }

    @Override
    public Observable<TokenViewModel> getAccessToken(RequestParams params) {
        return sessionFactory.createCloudTokenDataSource().getAccessToken(params);
    }
}
