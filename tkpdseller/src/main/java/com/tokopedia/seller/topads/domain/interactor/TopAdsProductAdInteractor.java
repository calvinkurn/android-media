package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.seller.topads.domain.model.data.Cell;
import com.tokopedia.seller.topads.domain.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.domain.model.data.ProductAd;
import com.tokopedia.seller.topads.domain.model.request.DataRequest;
import com.tokopedia.seller.topads.domain.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.domain.model.request.StatisticRequest;
import com.tokopedia.seller.topads.domain.model.response.PageDataResponse;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface TopAdsProductAdInteractor {

    void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<ProductAd>>> listener);

    void bulkAction(DataRequest<ProductAdBulkAction> bulkActionDataRequest, final ListenerInteractor<ProductAdBulkAction> listener);

    void getDetailProductAd(ListenerInteractor<List<ProductAd>> listenerInteractor);

    void getStatistic(StatisticRequest statisticRequest, ListenerInteractor<List<Cell>> listener);

    void unSubscribe();
}
