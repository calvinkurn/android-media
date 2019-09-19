package com.tokopedia.discovery.newdiscovery.base;

/**
 * Created by hangnadi on 9/26/17.
 */

public interface BaseDiscoveryContract {

    interface View {
        void onHandleResponseError();
    }

    interface Presenter<D extends View> {

        void setDiscoveryView(D discoveryView);
    }
}
