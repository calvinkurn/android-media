package com.tokopedia.tkpdreactnative.react.domain;

import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;

import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public interface UnifyReactNetworkRepository {
    Observable<String> request(ReactNetworkingConfiguration configuration);
}
