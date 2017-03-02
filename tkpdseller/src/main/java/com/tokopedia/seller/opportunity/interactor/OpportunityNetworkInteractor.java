package com.tokopedia.seller.opportunity.interactor;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 3/2/17.
 */

public interface OpportunityNetworkInteractor {
    boolean isRequesting();

    Observable<Response<TkpdResponse>> getOpportunity(TKPDMapParam<String, String> param);

    void setIsRequesting(boolean isRequesting);
}
