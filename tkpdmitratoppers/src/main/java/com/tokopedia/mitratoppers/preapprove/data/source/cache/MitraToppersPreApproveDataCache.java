package com.tokopedia.mitratoppers.preapprove.data.source.cache;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.GlobalAbsCacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

public class MitraToppersPreApproveDataCache {
    public static final String MITRA_TOPPERS_CACHE_KEY = "seller_mitra_toppers_preapprove_cache";
    public static final long MITRA_TOPPERS_CACHE_DURATION_SECONDS = TimeUnit.HOURS.toSeconds(24);
    private GlobalAbsCacheManager globalAbsCacheManager;

    @Inject
    public MitraToppersPreApproveDataCache(GlobalAbsCacheManager globalAbsCacheManager) {
        this.globalAbsCacheManager = globalAbsCacheManager;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        String jsonString = globalAbsCacheManager.get(MITRA_TOPPERS_CACHE_KEY);
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
            globalAbsCacheManager.save(MITRA_TOPPERS_CACHE_KEY, jsonString, MITRA_TOPPERS_CACHE_DURATION_SECONDS);
        }
    }

}
