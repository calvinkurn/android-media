package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.DbRecentProduct;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    public Observable<OpportunityCategoryModel> getFilter(TKPDMapParam<String, Object> params) {
        return opportunityService.getApi().getOpportunityCategory(AuthUtil.generateParamsNetwork2(context, params))
                .map(mapper)
                .doOnNext(saveToCache());
    }

    private Action1<OpportunityCategoryModel> saveToCache() {
        return new Action1<OpportunityCategoryModel>() {
            @Override
            public void call(OpportunityCategoryModel opportunityCategoryModel) {
                CommonUtils.dumper("NISNIS saveToCache");
                cacheManager.setKey(GetOpportunityFilterUseCase.FILTER_CACHE)
                        .setCacheDuration(DURATION_CACHE)
                        .setValue(CacheUtil.convertModelToString(opportunityCategoryModel,
                                new TypeToken<OpportunityCategoryModel>() {
                                }.getType()))
                        .store();
            }
        };
    }


}
