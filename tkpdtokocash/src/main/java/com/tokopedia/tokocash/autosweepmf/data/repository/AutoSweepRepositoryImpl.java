package com.tokopedia.tokocash.autosweepmf.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.AutoSweepLimitDataCloud;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.AutoSweepDataDetailCloud;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.domain.repository.AutoSweepRepository;

import javax.inject.Inject;

import rx.Observable;

public class AutoSweepRepositoryImpl implements AutoSweepRepository {
    private AutoSweepDataDetailCloud mAutoSweepDataDetailCloud;
    private AutoSweepLimitDataCloud mAutoSweepLimitDataCloud;

    @Inject
    public AutoSweepRepositoryImpl(AutoSweepDataDetailCloud autoSweepDataDetailCloud,
                                   AutoSweepLimitDataCloud autoSweepLimitDataCloud) {
        this.mAutoSweepDataDetailCloud = autoSweepDataDetailCloud;
        this.mAutoSweepLimitDataCloud = autoSweepLimitDataCloud;
    }

    @Override
    public Observable<AutoSweepDetail> getAutoSweepDetail() {
        return mAutoSweepDataDetailCloud.autoSweepDetail();
    }

    @Override
    public Observable<AutoSweepLimit> postAutoSweepLimit(JsonObject body) {
        return mAutoSweepLimitDataCloud.autoSweepLimit(body);
    }
}
