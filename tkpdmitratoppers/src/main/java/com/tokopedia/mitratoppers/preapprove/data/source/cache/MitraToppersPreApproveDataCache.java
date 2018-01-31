package com.tokopedia.mitratoppers.preapprove.data.source.cache;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.GlobalCacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

public class MitraToppersPreApproveDataCache {
    public static final String MITRA_TOPPERS_CACHE_KEY = "seller_mitra_toppers_preapprove_cache";
    public static final long MITRA_TOPPERS_CACHE_DURATION_SECONDS = TimeUnit.HOURS.toSeconds(24);
    private GlobalCacheManager globalCacheManager;

    @Inject
    public MitraToppersPreApproveDataCache(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        String jsonString = globalCacheManager.get(MITRA_TOPPERS_CACHE_KEY);
        if (TextUtils.isEmpty(jsonString)) {
            return Observable.just(null);
        } else {
            ResponsePreApprove responsePreApprove = CacheUtil.convertStringToModel(jsonString,
                    new TypeToken<ResponsePreApprove>(){}.getType());
            return Observable.just(responsePreApprove);
        }
    }

    public void savePreApproveBalance(ResponsePreApprove responsePreApprove) {
        if (responsePreApprove!= null) {
            String jsonString = CacheUtil.convertModelToString(
                    responsePreApprove, new TypeToken<ResponsePreApprove>(){}.getType());
            globalCacheManager.save(MITRA_TOPPERS_CACHE_KEY, jsonString, MITRA_TOPPERS_CACHE_DURATION_SECONDS);
        }
    }

}
