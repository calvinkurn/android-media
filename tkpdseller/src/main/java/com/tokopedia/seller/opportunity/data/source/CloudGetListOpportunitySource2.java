package com.tokopedia.seller.opportunity.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementApi;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudGetListOpportunitySource2 {
    private ReplacementApi replacementApi;

    @Inject
    public CloudGetListOpportunitySource2(ReplacementApi replacementApi) {
        this.replacementApi = replacementApi;
    }

    public Observable<OpportunityModel> getOpportunityList(RequestParams requestParams) {
        return replacementApi
                .getOpportunityList(requestParams.getParamsAllValueInString())
                .map(new OpportunityListMapper());
    }

    public Observable<String> getOpportunityNewPrice(RequestParams requestParams){
        return replacementApi
                .getOpportunityPriceInfo(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<String>, String>() {
                    @Override
                    public String call(Response<String> stringResponse) {
                        return stringResponse.body();
                    }
                });
    }
}
