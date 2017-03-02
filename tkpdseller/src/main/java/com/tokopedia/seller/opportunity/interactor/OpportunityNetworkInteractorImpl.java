package com.tokopedia.seller.opportunity.interactor;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityNetworkInteractorImpl implements OpportunityNetworkInteractor {

    private boolean isRequesting = false;

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }

    @Override
    public Observable<Response<TkpdResponse>> getOpportunity(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
