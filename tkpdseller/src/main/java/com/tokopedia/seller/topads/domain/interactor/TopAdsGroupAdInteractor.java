package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.PageDataResponse;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface TopAdsGroupAdInteractor {

    void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<GroupAd>>> listener);

    void bulkAction(DataRequest<GroupAdBulkAction> bulkActionDataRequest, final ListenerInteractor<GroupAdBulkAction> listener);

    void unSubscribe();
}
