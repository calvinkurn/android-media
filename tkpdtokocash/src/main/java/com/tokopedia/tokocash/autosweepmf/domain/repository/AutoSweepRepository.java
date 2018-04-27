package com.tokopedia.tokocash.autosweepmf.domain.repository;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit;

import rx.Observable;

/**
 * Data repository interface, It should be implemented by data layer
 */
public interface AutoSweepRepository {
    Observable<AutoSweepDetail> getAutoSweepDetail();

    Observable<AutoSweepLimit> postAutoSweepLimit(JsonObject body);
}
