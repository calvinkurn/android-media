package com.tokopedia.tokocash.autosweepmf.domain.repository;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;

import rx.Observable;

/**
 * Data repository interface, It should be implemented by data layer
 */
public interface AutoSweepRepository {
    Observable<AutoSweepDetailDomain> getAutoSweepDetail();

    Observable<AutoSweepLimitDomain> postAutoSweepLimit(JsonObject body);
}
