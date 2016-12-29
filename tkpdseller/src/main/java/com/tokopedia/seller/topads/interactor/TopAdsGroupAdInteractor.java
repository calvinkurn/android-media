package com.tokopedia.seller.topads.interactor;

import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface TopAdsGroupAdInteractor {

    void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<List<GroupAd>> listener);

    void bulkAction(DataRequest<GroupAdBulkAction> bulkActionDataRequest, final ListenerInteractor<GroupAdBulkAction> listener);
}
