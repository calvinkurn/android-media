package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 3/6/17.
 */
public class CloudGetFilterOpportunitySource {

    private static final int DURATION_CACHE = 86400;
    private final Context context;
    private final OpportunityService opportunityService;
    private final OpportunityFilterMapper mapper;
    private final GlobalCacheManager cacheManager;

    public CloudGetFilterOpportunitySource(Context context,
                                           OpportunityService opportunityService,
                                           OpportunityFilterMapper mapper,
                                           GlobalCacheManager cacheManager) {
        this.context = context;
        this.opportunityService = opportunityService;
        this.mapper = mapper;
        this.cacheManager = cacheManager;
    }

    public Observable<OpportunityFilterModel> getFilter(TKPDMapParam<String, Object> params) {
        return opportunityService.getApi().getOpportunityCategory(AuthUtil.generateParamsNetwork2(context, params))
                .map(mapper)
                .doOnNext(saveToCache());
    }

    private Action1<OpportunityFilterModel> saveToCache() {
        return new Action1<OpportunityFilterModel>() {
            @Override
            public void call(OpportunityFilterModel opportunityFilterModel) {
                cacheManager.setKey(GetOpportunityFilterUseCase.FILTER_CACHE)
                        .setCacheDuration(DURATION_CACHE)
                        .setValue(CacheUtil.convertModelToString(opportunityFilterModel,
                                new TypeToken<OpportunityFilterModel>() {
                                }.getType()))
                        .store();
            }
        };
    }


}
