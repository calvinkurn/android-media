package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityNewPriceMapper;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementApi;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudGetListOpportunitySource {

    private ReplacementApi replacementApi;
    private OpportunityListMapper opportunityListMapper;
    private OpportunityNewPriceMapper opportunityNewPriceMapper;
    private Context context;

    @Inject
    public CloudGetListOpportunitySource(ReplacementApi replacementApi, OpportunityListMapper opportunityListMapper,
                                         OpportunityNewPriceMapper opportunityNewPriceMapper,
                                         @ApplicationContext Context context) {
        this.replacementApi = replacementApi;
        this.opportunityListMapper = opportunityListMapper;
        this.opportunityNewPriceMapper = opportunityNewPriceMapper;
        this.context = context;
    }

    public Observable<OpportunityModel> getOpportunityList(RequestParams requestParams) {
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(context));
        return replacementApi
                .getOpportunityList(requestParams.getParamsAllValueInString())
                .map(opportunityListMapper);
    }

    public Observable<OpportunityNewPriceData> getOpportunityNewPrice(RequestParams requestParams){
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(context));
        return replacementApi
                .getOpportunityPriceInfo(requestParams.getParamsAllValueInString())
                .map(opportunityNewPriceMapper);
    }
}
