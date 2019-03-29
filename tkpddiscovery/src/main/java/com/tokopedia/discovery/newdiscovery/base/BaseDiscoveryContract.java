package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

/**
 * Created by hangnadi on 9/26/17.
 */

public interface BaseDiscoveryContract {

    interface View {

        boolean isForceSearch();

        void setForceSearch(boolean forceSearch);

        boolean isRequestOfficialStoreBanner();

        void setRequestOfficialStoreBanner(boolean requestOfficialStoreBanner);

        void onHandleApplink(String applink);

        void onHandleResponseHotlist(String url, String query);

        void onHandleResponseSearch(ProductViewModel productViewModel);

        void onHandleImageResponseSearch(ProductViewModel productViewModel);

        void onHandleImageSearchResponseError();

        void onHandleResponseIntermediary(String departmentId);

        void onHandleOfficialStorePage();

        void onHandleResponseUnknown();

        void onHandleResponseCatalog(String redirectUrl);

        void onHandleResponseError();

        void onHandleInvalidImageSearchResponse();

        void showErrorNetwork(String message);

        void showTimeoutErrorNetwork(String message);

        void onHandleImageSearchResponseSuccess();

        void showImageNotSupportedError();
    }

    interface Presenter<D extends View> {

        void setDiscoveryView(D discoveryView);

        void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStoreBanner);

        void requestImageSearch(String filePath);

    }
}
