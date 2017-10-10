package com.tokopedia.tkpdreactnative.react.data;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkBearerDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkWsAuthDataSource;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;

import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkRepositoryImpl implements UnifyReactNetworkRepository {
    private UnifyReactNetworkDataSource unifyReactNetworkDataSource;
    private UnifyReactNetworkWsAuthDataSource unifyReactNetworkWsAuthDataSource;
    private UnifyReactNetworkBearerDataSource unifyReactNetworkBearerDataSource;

    public UnifyReactNetworkRepositoryImpl(UnifyReactNetworkDataSource unifyReactNetworkDataSource,
                                           UnifyReactNetworkWsAuthDataSource unifyReactNetworkWsAuthDataSource,
                                           UnifyReactNetworkBearerDataSource unifyReactNetworkBearerDataSource) {
        this.unifyReactNetworkDataSource = unifyReactNetworkDataSource;
        this.unifyReactNetworkWsAuthDataSource = unifyReactNetworkWsAuthDataSource;
        this.unifyReactNetworkBearerDataSource = unifyReactNetworkBearerDataSource;
    }

    @Override
    public Observable<String> request(ReactNetworkingConfiguration configuration) {
        switch (configuration.getAuthorizationMode()) {
            case ReactConst.Networking.WSAUTH:
                return unifyReactNetworkWsAuthDataSource.request(configuration);
            case ReactConst.Networking.BEARER:
                return unifyReactNetworkBearerDataSource.request(configuration);
            default:
                return unifyReactNetworkDataSource.request(configuration);
        }
    }
}
