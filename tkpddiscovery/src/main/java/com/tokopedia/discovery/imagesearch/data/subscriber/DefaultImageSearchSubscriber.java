package com.tokopedia.discovery.imagesearch.data.subscriber;

import com.tokopedia.core.network.entity.discovery.ImageSearchResponse;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract;

import rx.Subscriber;

/**
 * Created by sachinbansal on 1/18/18.
 */

public class DefaultImageSearchSubscriber<D2 extends BaseDiscoveryContract.View>
        extends Subscriber<ImageSearchResponse> {


    private D2 discoveryView;

    public DefaultImageSearchSubscriber(D2 discoveryView) {
        this.discoveryView = discoveryView;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        discoveryView.onHandleResponseError();
        throwable.printStackTrace();
    }

    @Override
    public void onNext(ImageSearchResponse imageSearchResultModel) {

        discoveryView.onHandleImageSearchResponse(imageSearchResultModel);
    }
}
