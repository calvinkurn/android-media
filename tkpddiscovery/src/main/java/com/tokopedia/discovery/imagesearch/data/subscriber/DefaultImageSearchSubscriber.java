package com.tokopedia.discovery.imagesearch.data.subscriber;

import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemResponse;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by sachinbansal on 1/18/18.
 */

public class DefaultImageSearchSubscriber<D2 extends BaseDiscoveryContract.View>
        extends Subscriber<ImageSearchItemResponse> {

    private D2 discoveryView;
    List<String> productIDList = new ArrayList<>();

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
    public void onNext(ImageSearchItemResponse imageSearchResponse) {

        if (imageSearchResponse == null || imageSearchResponse.getAuctionsArrayList() == null) {
            discoveryView.onHandleInvalidImageSearchResponse();
            return;
        }

        int productCount = imageSearchResponse.getAuctionsArrayList().size();
        StringBuilder productIDs = new StringBuilder();

        productIDList.clear();

        for (int i = 0; i < productCount; i++) {
            String itemId = imageSearchResponse.getAuctionsArrayList().get(i).getItemId();
            productIDList.add(itemId);
            productIDs.append(itemId);
            if (i != productCount - 1) {
                productIDs.append(",");
            }
        }

        if (StringUtils.isNotBlank(productIDs.toString())) {
            discoveryView.onHandleImageSearchResponseSuccess(productIDList, productIDs.toString());

        } else {
            discoveryView.onHandleImageSearchResponseError();
        }

    }
}
