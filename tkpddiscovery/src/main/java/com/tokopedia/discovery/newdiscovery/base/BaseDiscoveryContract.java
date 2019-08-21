package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;

/**
 * Created by hangnadi on 9/26/17.
 */

public interface BaseDiscoveryContract {

    interface View {

        void onHandleResponseHotlist(String url, String query);

        void onHandleImageResponseSearch(ProductViewModel productViewModel);

        void onHandleImageSearchResponseError();

        void onHandleResponseIntermediary(String departmentId);

        void onHandleResponseUnknown();

        void onHandleResponseCatalog(String redirectUrl);

        void onHandleResponseError();

        void onHandleInvalidImageSearchResponse();

        void showErrorNetwork(String message);

        void showTimeoutErrorNetwork(String message);

        void showImageNotSupportedError();
    }

    interface Presenter<D extends View> {

        void setDiscoveryView(D discoveryView);

        void requestImageSearch(String filePath);
    }
}
