package com.tokopedia.discovery.imagesearch.data.subscriber;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorRequestDeniedException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorMaintenanceException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorTimeZoneException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.discovery.imagesearch.search.exception.ImageNotSupportedException;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract;
import com.tokopedia.discovery.newdiscovery.base.DefaultSearchSubscriber;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
    public void onError(Throwable e) {

        if (e instanceof ImageNotSupportedException) {
            discoveryView.showImageNotSupportedError();
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            discoveryView.showTimeoutErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            discoveryView.showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseDataNullException) {
            discoveryView.showErrorNetwork(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            discoveryView.showErrorNetwork(e.getMessage());
        } else if (e instanceof ServerErrorRequestDeniedException) {
            ServerErrorHandler.sendForceLogoutAnalytics(
                    ((ServerErrorRequestDeniedException) e).getUrl()
            );
            ServerErrorHandler.showForceLogoutDialog();
        } else if (e instanceof ServerErrorMaintenanceException) {
            ServerErrorHandler.showMaintenancePage();
        } else if (e instanceof ServerErrorTimeZoneException) {
            ServerErrorHandler.showTimezoneErrorSnackbar();
        } else {
            discoveryView.onHandleInvalidImageSearchResponse();
        }

        e.printStackTrace();
    }

    @Override
    protected void onHandleSearch(SearchResultModel searchResult) {
        ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPage(searchResult);
        SearchParameter imageSearchProductParameter = new SearchParameter();
        imageSearchProductParameter.set(SearchApiConst.START, String.valueOf(searchResult.getProductList().size()));
        imageSearchProductParameter.setSearchQuery(searchResult.getQuery());
        imageSearchProductParameter.set(SearchApiConst.SOURCE, "imagesearch");

        model.setSearchParameter(imageSearchProductParameter);
        model.setForceSearch(forceSearch);
        model.setImageSearch(imageSearch);

        if (model.getProductList() == null || model.getProductList().size() == 0) {
            discoveryView.onHandleImageSearchResponseError();
            return;
        }

        discoveryView.onHandleImageSearchResponseSuccess();
        discoveryView.onHandleImageResponseSearch(model);
    }
}