package com.tokopedia.seller.opportunity.data.source.local;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.source.CloudGetFilterOpportunitySource;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by nisie on 3/23/17.
 */

public class LocalGetFilterOpportunitySource {

    private GlobalCacheManager globalCacheManager;


    public LocalGetFilterOpportunitySource(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<OpportunityCategoryModel> getFilter() {

        return Observable.just(GetOpportunityFilterUseCase.FILTER_CACHE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .map(new Func1<String, OpportunityCategoryModel>() {
                    @Override
                    public OpportunityCategoryModel call(String key) {
                        CommonUtils.dumper("NISNIS GET FILTER CACHE");
                        try {
                            if (getCache(key) != null)
                                return CacheUtil.convertStringToModel(getCache(key),
                                        new TypeToken<OpportunityCategoryModel>() {
                                        }.getType());
                            else return null;
                        } catch (RuntimeException e) {
                            return null;
                        }
                    }
                });
    }

    private String getCache(String key) {
        return globalCacheManager.getValueString(key);
    }
}
