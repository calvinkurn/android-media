package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.discovery.imagesearch.domain.usecase.NewImageSearchResponse;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

/**
 * Created by hangnadi on 9/26/17.
 */

public interface BaseDiscoveryContract {

    interface View {

        boolean isForceSearch();

        void setForceSearch(boolean forceSearch);

        boolean isRequestOfficialStoreBanner();

        void setRequestOfficialStoreBanner(boolean requestOfficialStoreBanner);

        void onHandleResponseHotlist(String url, String query);

        void onHandleResponseSearch(ProductViewModel productViewModel);

        void onHandleImageSearchResponse(NewImageSearchResponse imageSearchResponse);

        void onHandleResponseIntermediary(String departmentId);

        void onHandleOfficialStorePage();

        void onHandleResponseUnknown();

        void onHandleResponseCatalog(String redirectUrl);

        void onHandleResponseError();
    }

    interface Presenter<D extends View> {

        void setDiscoveryView(D discoveryView);

        void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStoreBanner);

        void requestImageSearchProduct(SearchParameter imageSearchProductParameter);

        void requestImageSearch(byte[] imageByteArray);

    }
}
