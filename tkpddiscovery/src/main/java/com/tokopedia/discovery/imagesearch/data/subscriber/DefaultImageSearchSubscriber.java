package com.tokopedia.discovery.imagesearch.data.subscriber;

import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract;
import com.tokopedia.discovery.newdiscovery.base.DefaultSearchSubscriber;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 1/18/18.
 */

public class DefaultImageSearchSubscriber<D2 extends BaseDiscoveryContract.View>
        extends DefaultSearchSubscriber {

    List<String> productIDList = new ArrayList<>();

    public DefaultImageSearchSubscriber(D2 discoveryView) {
        super(null, false, discoveryView, true);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof GetImageSearchUseCase.GetImageSearchException) {
            discoveryView.onHandleInvalidImageSearchResponse();
        } else if (throwable instanceof GetImageSearchUseCase.HandleImageSearchResponseError) {
            discoveryView.onHandleImageSearchResponseError();
        } else {
            discoveryView.onHandleResponseError();
        }
        throwable.printStackTrace();
    }

    @Override
    protected void onHandleSearch(SearchResultModel searchResult) {
        ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPage(searchResult);
        SearchParameter imageSearchProductParameter = new SearchParameter();
        imageSearchProductParameter.setStartRow(searchResult.getProductList().size());
        imageSearchProductParameter.setQueryKey(searchResult.getQuery());
        imageSearchProductParameter.setSource("imagesearch");
        model.setSearchParameter(imageSearchProductParameter);
        model.setForceSearch(forceSearch);
        model.setImageSearch(imageSearch);
        discoveryView.onHandleImageSearchResponseSuccess();
        discoveryView.onHandleResponseSearch(model);
    }
}
