package com.tokopedia.tokocash.autosweepmf.domain.repository;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit;

import rx.Observable;

public interface AutoSweepRepository {
    Observable<AutoSweepDetail> getAutoSweepDetail();

    Observable<AutoSweepLimit> postAutoSweepLimit(JsonObject body);
}
